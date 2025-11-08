package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.entity.Aluno;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.repository.AlunoRepository;
import portaldoprofessor.backend.repository.AvaliacaoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public Turma criarTurma(Turma turma) {
        return turmaRepository.save(turma);
    }

    public Turma editarTurma(Long id, Turma detalhes) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        turma.setNome(detalhes.getNome());
        turma.setDescricao(detalhes.getDescricao());
        turma.setCodigo(detalhes.getCodigo());
        return turmaRepository.save(turma);
    }

    public void deletarTurma(Long id) {
        turmaRepository.deleteById(id);
    }

    public List<Turma> listarTodas() {
        return turmaRepository.findAll();
    }

    public Turma pegarTurma(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
    }

    public List<Aluno> listarAlunosDaTurma(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        return List.copyOf(turma.getAlunos());
    }

    public List<Avaliacao> listarAvaliacoesDaTurma(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        return avaliacaoRepository.findByTurma(turma);
    }

    // Valida se a soma dos pesos das avaliações é 100%
    public void validarPesoAvaliacoes(Turma turma) {
        Double totalPeso = turma.getAvaliacoes().stream()
                .mapToDouble(Avaliacao::getPeso)
                .sum();
        if (totalPeso != 100) {
            throw new RuntimeException("A soma dos pesos das avaliações deve ser 100%");
        }
    }
}
