package portaldoprofessor.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurmaDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String codigo;
    private String professorNome;
    private LocalDateTime createdAt;
}
