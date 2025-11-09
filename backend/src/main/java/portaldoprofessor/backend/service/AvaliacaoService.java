package portaldoprofessor.backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.dto.UpdateAvaliacaoDTO;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.entity.Role;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.repository.AvaliacaoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;
import portaldoprofessor.backend.security.UserContextService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final TurmaRepository turmaRepository;
    private final UserContextService userContextService;

    private static final String ALFANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    private String gerarCodigoUnico() {
        String codigo;
        do {
            StringBuilder sb = new StringBuilder(5);
            for (int i = 0; i < 5; i++) {
                sb.append(ALFANUM.charAt(random.nextInt(ALFANUM.length())));
            }
            codigo = sb.toString();
        } while (avaliacaoRepository.existsByCodigo(codigo));
        return codigo;
    }

    public Avaliacao pegarAvaliacao(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
    }

    @Transactional
    public List<Avaliacao> criarAvaliacoes(List<Avaliacao> avaliacoes, Long turmaId) {
        if (!userContextService.hasAnyRole(Role.ADMIN, Role.TEACHER)) {
            throw new SecurityException("Acesso negado: apenas ADMIN ou PROFESSOR podem gerenciar avaliações");
        }

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        List<Avaliacao> existentes = avaliacaoRepository.findByTurma(turma);

        double somaExistente = existentes.stream()
                .mapToDouble(Avaliacao::getPeso)
                .sum();

        double somaNovas = avaliacoes.stream()
                .mapToDouble(Avaliacao::getPeso)
                .sum();

        double somaTotal = somaExistente + somaNovas;

        if (Math.abs(somaTotal - 100.0) > 0.001) {
            throw new RuntimeException(
                    "A soma dos pesos das avaliações da turma deve ser 100% (atual: " + somaTotal + "%)"
            );
        }

        for (Avaliacao avaliacao : avaliacoes) {
            avaliacao.setCodigo(gerarCodigoUnico());
            avaliacao.setTurma(turma);
        }

        List<Avaliacao> salvas = avaliacaoRepository.saveAll(avaliacoes);

        turmaRepository.flush();

        return salvas;
    }


    @Transactional
    public Avaliacao editarAvaliacao(Long id, UpdateAvaliacaoDTO dto) {
        if (!userContextService.hasAnyRole(Role.ADMIN, Role.TEACHER)) {
            throw new SecurityException("Acesso negado: apenas ADMIN ou PROFESSOR podem gerenciar avaliações");
        }

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            avaliacao.setNome(dto.getNome());
        }

        return avaliacaoRepository.save(avaliacao);
    }


    @Transactional
    public void deletarAvaliacao(Long id) {
        if (!userContextService.hasAnyRole(Role.ADMIN, Role.TEACHER)) {
            throw new SecurityException("Acesso negado: apenas ADMIN ou PROFESSOR podem gerenciar avaliações");
        }

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        Turma turma = avaliacao.getTurma();

        List<Avaliacao> avaliacoes = new ArrayList<>(avaliacaoRepository.findByTurma(turma));

        if (avaliacoes.size() <= 1) {
            throw new RuntimeException("Não é possível remover a única avaliação da turma.");
        }

        avaliacoes.removeIf(a -> a.getId().equals(id));

        avaliacaoRepository.delete(avaliacao);

        double novoPeso = 100.0 / avaliacoes.size();
        for (Avaliacao restante : avaliacoes) {
            restante.setPeso(novoPeso);
        }

        avaliacaoRepository.saveAll(avaliacoes);
    }




}
