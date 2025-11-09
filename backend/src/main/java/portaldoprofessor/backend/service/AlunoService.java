package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.dto.AlunoDTO;
import portaldoprofessor.backend.dto.CreatedAlunoDTO;
import portaldoprofessor.backend.entity.Aluno;
import portaldoprofessor.backend.entity.Role;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.repository.AlunoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;
import portaldoprofessor.backend.security.UserContextService;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final UserContextService userContextService;

    private String gerarMatricula() {
        Random random = new Random();
        String currentYear = String.valueOf(LocalDate.now().getYear());
        String month = String.format("%02d", LocalDate.now().getMonthValue());
        int randMatricula = random.nextInt(10000);
        return currentYear + month + String.format("%04d", randMatricula);
    }

    public AlunoDTO criarAluno(CreatedAlunoDTO dto) {
        if (!userContextService.hasAnyRole(Role.ADMIN, Role.TEACHER)) {
            throw new RuntimeException("Acesso negado: apenas ADMIN ou PROFESSOR podem cadastrar alunos");
        }

        Aluno aluno = new Aluno();
        aluno.setName(dto.getName());
        aluno.setEmail(dto.getEmail());
        aluno.setMatricula(gerarMatricula());
        aluno.setActive(true);

        // Turma é opcional
        if (dto.getTurmaIds() != null && !dto.getTurmaIds().isEmpty()) {
            Set<Turma> turmas = dto.getTurmaIds().stream()
                    .map(id -> turmaRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Turma não encontrada: " + id)))
                    .collect(Collectors.toSet());
            aluno.setTurmas(turmas);
        }

        aluno.setCreatedBy(userContextService.getLoggedUser());

        Aluno salvo = alunoRepository.save(aluno);
        return toDTO(salvo);
    }

    public AlunoDTO editarAluno(Long id, CreatedAlunoDTO dto) {
        var user = userContextService.getLoggedUser();

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setName(dto.getName());
        aluno.setEmail(dto.getEmail());
        aluno.setUpdatedBy(user);

        if (dto.getTurmaIds() != null) {
            Set<Turma> turmas = dto.getTurmaIds().stream()
                    .map(turmaId -> turmaRepository.findById(turmaId)
                            .orElseThrow(() -> new RuntimeException("Turma não encontrada: " + turmaId)))
                    .collect(Collectors.toSet());
            aluno.setTurmas(turmas);
        }

        return toDTO(alunoRepository.save(aluno));
    }

    @Transactional
    public void deletarAluno(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.getTurmas().forEach(turma -> turma.getAlunos().remove(aluno));
        aluno.getTurmas().clear();

        alunoRepository.delete(aluno);
    }

    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AlunoDTO findAluno(Long id) {
        return alunoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    @Transactional
    public AlunoDTO vincularAlunoATurma(Long alunoId, Long turmaId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        aluno.getTurmas().add(turma);
        turma.getAlunos().add(aluno);

        return toDTO(aluno);
    }

    public AlunoDTO desativarAluno(Long id) {
        if (!userContextService.isAdmin()) {
            throw new SecurityException("Apenas administradores podem desativar alunos.");
        }

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setActive(false);
        aluno.setUpdatedBy(userContextService.getLoggedUser());

        return toDTO(alunoRepository.save(aluno));
    }

    private AlunoDTO toDTO(Aluno aluno) {
        return AlunoDTO.builder()
                .id(aluno.getId())
                .name(aluno.getName())
                .email(aluno.getEmail())
                .matricula(aluno.getMatricula())
                .active(aluno.isActive())
                .createdAt(aluno.getCreatedAt())
                .turmaIds(aluno.getTurmas() != null
                        ? aluno.getTurmas().stream().map(Turma::getId).collect(Collectors.toSet())
                        : Set.of())
                .build();
    }
}
