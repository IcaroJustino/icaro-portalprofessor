package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.dto.CreatedTurmaDTO;
import portaldoprofessor.backend.dto.TurmaDTO;
import portaldoprofessor.backend.dto.UpdateTurmaDTO;
import portaldoprofessor.backend.entity.*;
import portaldoprofessor.backend.exception.EntityNotFoundException;
import portaldoprofessor.backend.exception.PasswordInvalidException;
import portaldoprofessor.backend.repository.AlunoRepository;
import portaldoprofessor.backend.repository.AvaliacaoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;
import portaldoprofessor.backend.repository.UserRepository;
import portaldoprofessor.backend.security.UserContextService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final UserContextService userContext;
    private final UserContextService userContextService;


    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final UserRepository userRepository;

    private String gerarCodigo() {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }

    @Transactional
    public TurmaDTO criarTurma(CreatedTurmaDTO dto) {
        User professor = userContextService.getLoggedUser();

        String codigo;
        do {
            codigo = gerarCodigo();
        } while (turmaRepository.existsByCodigo(codigo));

        Turma turma = Turma.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .codigo(codigo)
                .professor(professor)
                .build();

        Turma salva = turmaRepository.save(turma);
        return toDTO(salva);
    }

    public TurmaDTO editarTurma(Long id, UpdateTurmaDTO dto) {
        Turma turma = pegarTurmaEntity(id);

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            turma.setNome(dto.getNome());
        }

        if (dto.getDescricao() != null) {
            turma.setDescricao(dto.getDescricao());
        }

        turmaRepository.save(turma);
        return toDTO(turma);
    }

    @Transactional
    public void deletarTurma(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        validarPermissaoProfessorOuAdmin(turma);
        turmaRepository.delete(turma);
    }

    public List<TurmaDTO> listarTodas() {
        List<Turma> turmas = turmaRepository.findAll();

        // força inicialização do professor para evitar proxy lazy
        turmas.forEach(t -> {
            if (t.getProfessor() != null) {
                t.getProfessor().getName();
            }
        });

        return turmas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<TurmaDTO> listarTurmasDoProfessor(Long professorId) {
        var user = userContext.getLoggedUser();

        if ((!userContext.isProfessor() && !userContext.isAdmin())) {
            throw new SecurityException("Apenas professores ou administradores podem visualizar turmas do professor.");
        }
        if(!userContext.getLoggedUser().getId().equals(professorId)){
            throw new SecurityException("Acesso negado para acessar uma professor.");
        }
        User professor = userRepository.findById(professorId).stream().findFirst().orElseThrow();

        List<TurmaDTO> minhasTurmas  = new ArrayList<>();
        List<Turma> turmasGerais = turmaRepository.findByProfessor(user);
        turmasGerais.forEach(t -> {
            if (t.getProfessor() != null) {
                minhasTurmas.add(toDTO(t));

            }
        });
        return minhasTurmas;
    }

    public TurmaDTO detalharTurma(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        return toDTO(turma);
    }

    public Turma pegarTurmaEntity(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
    }


    public List<Aluno> listarAlunosDaTurma(Long turmaId) {
        Turma turma = pegarTurmaEntity(turmaId);
        return List.copyOf(turma.getAlunos());
    }

    public List<Avaliacao> listarAvaliacoesDaTurma(Long turmaId) {
        Turma turma = pegarTurmaEntity(turmaId);
        return avaliacaoRepository.findByTurma(turma);
    }

    public void validarPesoAvaliacoes(Turma turma) {
        double totalPeso = turma.getAvaliacoes().stream()
                .mapToDouble(Avaliacao::getPeso)
                .sum();
        if (Math.abs(totalPeso - 100) > 0.001) {
            throw new PasswordInvalidException("A soma dos pesos das avaliações deve ser 100%");
        }
    }

    private void validarPermissaoProfessorOuAdmin(Turma turma) {
        User user = userContext.getLoggedUser();

        if (userContext.isAdmin()) return; // Admin sempre pode
        if (userContext.isProfessor() && turma.getProfessor().getId().equals(user.getId())) return;

        throw new SecurityException("Você não tem permissão para modificar esta turma.");
    }

    private TurmaDTO toDTO(Turma turma) {
        return TurmaDTO.builder()
                .id(turma.getId())
                .nome(turma.getNome())
                .descricao(turma.getDescricao())
                .codigo(turma.getCodigo())
                .professorNome(turma.getProfessor().getName())
                .createdAt(turma.getCreatedAt())
                .build();
    }
}
