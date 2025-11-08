package portaldoprofessor.backend.repository;

import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    Optional<Avaliacao> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Avaliacao> findByTurma(Turma turma);
}
