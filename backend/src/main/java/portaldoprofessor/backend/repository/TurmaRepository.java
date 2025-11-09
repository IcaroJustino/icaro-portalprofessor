package portaldoprofessor.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.entity.User;
import java.util.Optional;
import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    Optional<Turma> findByCodigo(String codigo);

    List<Turma> findByProfessor(User professor);

    boolean existsByCodigo(String codigo);
}
