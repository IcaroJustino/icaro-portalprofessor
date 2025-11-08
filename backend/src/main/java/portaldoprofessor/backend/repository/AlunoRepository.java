package portaldoprofessor.backend.repository;

import portaldoprofessor.backend.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Busca um aluno pelo e-mail
    Optional<Aluno> findByEmail(String email);

    // Busca um aluno pelo nome
    Optional<Aluno> findByName(String name);

    // Exemplo de busca por matrícula (se tiver no Aluno)
    Optional<Aluno> findByMatricula(String matricula);
}
