package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.entity.Aluno;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.entity.User;
import portaldoprofessor.backend.exception.EntityNotFoundException;
import portaldoprofessor.backend.exception.PasswordInvalidException;
import portaldoprofessor.backend.repository.AlunoRepository;
import portaldoprofessor.backend.repository.AvaliacaoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;
import portaldoprofessor.backend.security.UserContextService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final UserContextService userContext;

    @Transactional
    public Turma criarTurma(Turma turma) {
        User professor = userContext.getLoggedUser();

        if (!userContext.isProfessor() && !userContext.isAdmin()) {
            throw new SecurityException("Apenas professores ou administradores podem criar turmas.");
        }

        turma.setProfessor(professor);
        return turmaRepository.save(turma);
    }

    @Transactional
    public Turma editarTurma(Long id, Turma detalhes) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        validarPermissaoProfessorOuAdmin(turma);

        turma.setNome(detalhes.getNome());
        turma.setDescricao(detalhes.getDescricao());
        turma.setCodigo(detalhes.getCodigo());

        return turmaRepository.save(turma);
    }

    @Transactional
    public void deletarTurma(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        validarPermissaoProfessorOuAdmin(turma);
        turmaRepository.delete(turma);
    }

    public List<Turma> listarTodas() {
        return turmaRepository.findAll();
    }


    public List<Turma> listarTurmasDoProfessor() {
        var user = userContext.getLoggedUser();

        if (!userContext.isProfessor() && !userContext.isAdmin()) {
            throw new SecurityException("Apenas professores ou administradores podem visualizar turmas do professor.");
        }

        if (userContext.isAdmin()) {
            return turmaRepository.findAll();
        }

        return turmaRepository.findByProfessor(user);
    }

    public Turma pegarTurma(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
    }

    public List<Aluno> listarAlunosDaTurma(Long turmaId) {
        Turma turma = pegarTurma(turmaId);
        return List.copyOf(turma.getAlunos());
    }

    public List<Avaliacao> listarAvaliacoesDaTurma(Long turmaId) {
        Turma turma = pegarTurma(turmaId);
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
}
