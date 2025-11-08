package portaldoprofessor.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.repository.AvaliacaoRepository;
import portaldoprofessor.backend.repository.TurmaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final TurmaRepository turmaRepository;

    @Transactional
    public Avaliacao criarAvaliacao(Avaliacao avaliacao, Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        turma.getAvaliacoes().add(avaliacao);
        avaliacao.setTurma(turma);

        Double totalPeso = turma.getAvaliacoes().stream().mapToDouble(Avaliacao::getPeso).sum();
        if (totalPeso > 100) {
            throw new RuntimeException("A soma dos pesos das avaliações não pode exceder 100%");
        }

        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao editarAvaliacao(Long id, Avaliacao detalhes) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        avaliacao.setNome(detalhes.getNome());
        avaliacao.setPeso(detalhes.getPeso());
        avaliacao.setPrazo(detalhes.getPrazo());
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao estenderPrazo(Long id, LocalDateTime novoPrazo) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        avaliacao.setPrazo(novoPrazo);
        return avaliacao;
    }

    public void deletarAvaliacao(Long id) {
        avaliacaoRepository.deleteById(id);
    }

    public Avaliacao pegarAvaliacao(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
    }

    public List<Avaliacao> listarAvaliacoesDaTurma(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        return avaliacaoRepository.findByTurma(turma);
    }
}
