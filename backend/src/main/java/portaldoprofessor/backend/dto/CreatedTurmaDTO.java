package portaldoprofessor.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedTurmaDTO {
    private String nome;
    private String descricao;
}
