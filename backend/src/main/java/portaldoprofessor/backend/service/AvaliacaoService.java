package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.entity.Role;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.repository.AvaliacaoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;
import portaldoprofessor.backend.security.UserContextService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final TurmaRepository turmaRepository;
    private final UserContextService userContextService;

    @Transactional
    public List<Avaliacao> criarAvaliacoes(List<Avaliacao> avaliacoes, Long turmaId) {
        validarPermissao();

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        for (Avaliacao avaliacao : avaliacoes) {
            avaliacao.setTurma(turma);
        }

        double somaTotal = turma.getAvaliacoes().stream()
                .mapToDouble(Avaliacao::getPeso)
                .sum() + avaliacoes.stream()
                .mapToDouble(Avaliacao::getPeso)
                .sum();

        if (Math.abs(somaTotal - 100.0) > 0.001) {
            throw new RuntimeException("A soma dos pesos das avaliações da turma deve ser 100% (atual: " + somaTotal + "%)");
        }

        turma.getAvaliacoes().addAll(avaliacoes);
        turmaRepository.save(turma);

        return avaliacaoRepository.saveAll(avaliacoes);
    }

    @Transactional
    public Avaliacao editarAvaliacao(Long id, Avaliacao detalhes) {
        validarPermissao();

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        avaliacao.setNome(detalhes.getNome());
        avaliacao.setPeso(detalhes.getPeso());
        avaliacao.setPrazo(detalhes.getPrazo());
        avaliacao.setCodigo(detalhes.getCodigo());

        validarSomaPesos(avaliacao.getTurma(), avaliacao, detalhes.getPeso());

        return avaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public Avaliacao estenderPrazo(Long id, LocalDateTime novoPrazo) {
        validarPermissao();

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        avaliacao.setPrazo(novoPrazo);
        return avaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public void deletarAvaliacao(Long id) {
        validarPermissao();

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        Turma turma = avaliacao.getTurma();
        avaliacaoRepository.delete(avaliacao);

        validarSomaPosRemocao(turma);
    }

    public List<Avaliacao> listarAvaliacoesDaTurma(Long turmaId) {
        validarPermissao();

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        return avaliacaoRepository.findByTurma(turma);
    }

    public Avaliacao pegarAvaliacao(Long id) {
        validarPermissao();

        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
    }

    private void validarPermissao() {
        if (!userContextService.hasAnyRole(Role.ADMIN, Role.TEACHER)) {
            throw new SecurityException("Acesso negado: apenas ADMIN ou PROFESSOR podem gerenciar avaliações");
        }
    }

    private void validarSomaPesos(Turma turma, Avaliacao editada, double novoPeso) {
        double soma = turma.getAvaliacoes().stream()
                .filter(a -> !a.getId().equals(editada.getId()))
                .mapToDouble(Avaliacao::getPeso)
                .sum() + novoPeso;

        if (Math.abs(soma - 100.0) > 0.001) {
            throw new RuntimeException("A soma total dos pesos da turma deve ser 100% (atual: " + soma + "%)");
        }
    }

    private void validarSomaPosRemocao(Turma turma) {
        double soma = turma.getAvaliacoes().stream()
                .mapToDouble(Avaliacao::getPeso)
                .sum();

        if (Math.abs(soma - 100.0) > 0.001 && soma != 0.0) {
            throw new RuntimeException("Após remover, a soma dos pesos da turma deve continuar sendo 100% (atual: " + soma + "%)");
        }
    }
}
