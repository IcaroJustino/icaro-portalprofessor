package portaldoprofessor.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoDTO {

    private Long id;
    private String nome;
    private double peso;
    private String codigo;
    private LocalDateTime prazo;
    private Long turmaId;
}
