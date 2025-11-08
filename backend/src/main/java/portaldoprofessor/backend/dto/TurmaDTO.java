package portaldoprofessor.backend.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurmaDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String codigo;
    private Set<Long> alunoIds; // ids dos alunos
    private Set<Long> avaliacaoIds; // ids das avaliações
}
