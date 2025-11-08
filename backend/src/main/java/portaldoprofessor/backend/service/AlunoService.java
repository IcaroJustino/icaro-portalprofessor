package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.entity.Aluno;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.repository.AlunoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public Aluno criarAluno(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public Aluno editarAluno(Long id, Aluno alunoDetalhes) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        aluno.setName(alunoDetalhes.getName());
        aluno.setEmail(alunoDetalhes.getEmail());
        aluno.setMatricula(alunoDetalhes.getMatricula());
        return alunoRepository.save(aluno);
    }

    public void deletarAluno(Long id) {
        alunoRepository.deleteById(id);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno pegarAluno(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    @Transactional
    public Aluno vincularAlunoATurma(Long alunoId, Long turmaId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        aluno.getTurmas().add(turma);
        turma.getAlunos().add(aluno);

        return aluno;
    }

    // Desativar login do aluno (somente admin pode fazer)
    public Aluno desativarAluno(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        aluno.setActive(false);
        return aluno;
    }
}
