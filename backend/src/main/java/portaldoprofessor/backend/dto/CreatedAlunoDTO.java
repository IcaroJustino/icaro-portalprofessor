package portaldoprofessor.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedAlunoDTO {
    private String name;
    private String email;
    private String matricula;
}
