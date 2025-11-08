package portaldoprofessor.backend.repository;

import portaldoprofessor.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca por e-mail (útil para login/autenticação)
    Optional<User> findByEmail(String email);

    // Busca por matrícula (útil para alunos/professores)
    Optional<User> findByMatricula(String matricula);

    // Verifica se já existe um e-mail cadastrado
    boolean existsByEmail(String email);

    // Verifica se já existe uma matrícula cadastrada
    boolean existsByMatricula(String matricula);
}
