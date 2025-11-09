package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.dto.*;
import portaldoprofessor.backend.entity.*;
import portaldoprofessor.backend.exception.EntityNotFoundException;
import portaldoprofessor.backend.repository.AlunoRepository;
import portaldoprofessor.backend.repository.AvaliacaoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;
import portaldoprofessor.backend.security.UserContextService;
import java.security.SecureRandom;
import java.util.*;
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
    public void deletarTurma(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        turma.removerReferencias();

        turmaRepository.delete(turma);
    }







    public List<TurmaDTO> listarTodas() {
        List<Turma> turmas = turmaRepository.findAll();

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


    @Transactional(readOnly = true)
    public List<AlunoDTO> listarAlunosDaTurma(Long turmaId) {
        Turma turma = pegarTurmaEntity(turmaId);

        return turma.getAlunos().stream()
                .map(aluno -> AlunoDTO.builder()
                        .id(aluno.getId())
                        .name(aluno.getName())
                        .email(aluno.getEmail())
                        .matricula(aluno.getMatricula())
                        .active(aluno.isActive())
                        .createdAt(aluno.getCreatedAt())
                        .turmaIds(aluno.getTurmas().stream()
                                .map(Turma::getId)
                                .collect(Collectors.toSet()))
                        .build())
                .toList();
    }



    public List<Avaliacao> listarAvaliacoesDaTurma(Long turmaId) {
        Turma turma = pegarTurmaEntity(turmaId);
        return avaliacaoRepository.findByTurma(turma);
    }

    private void validarPermissaoProfessorOuAdmin(Turma turma) {
        User user = userContext.getLoggedUser();

        if (userContext.isAdmin()) return; // Admin sempre pode
        if (userContext.isProfessor() && turma.getProfessor().getId().equals(user.getId())) return;

        throw new SecurityException("Você não tem permissão para modificar esta turma.");
    }

    @Transactional
    public TurmaDTO vincularAlunos(VincularAlunosDTO dto) {
        Turma turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        validarPermissaoProfessorOuAdmin(turma);

        List<Aluno> alunos = alunoRepository.findAllById(dto.getAlunosIds());
        if (alunos.isEmpty()) {
            throw new RuntimeException("Nenhum aluno encontrado para os IDs informados.");
        }

        Set<Aluno> alunosParaAdicionar = alunos.stream()
                .filter(aluno -> !turma.getAlunos().contains(aluno))
                .collect(Collectors.toSet());

        for (Aluno aluno : alunosParaAdicionar) {
            turma.getAlunos().add(aluno);
            aluno.getTurmas().add(turma);
        }

        turmaRepository.save(turma);
        alunoRepository.saveAll(alunosParaAdicionar);

        return toDTO(turma);
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
