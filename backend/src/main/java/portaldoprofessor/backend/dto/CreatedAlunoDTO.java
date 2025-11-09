package portaldoprofessor.backend.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedAlunoDTO {
    private String name;
    private String email;
    private String matricula;
    private Set<Long> turmaIds; // pode ser nulo ou vazio
}
