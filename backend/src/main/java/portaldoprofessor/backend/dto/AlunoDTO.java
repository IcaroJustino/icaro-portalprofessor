package portaldoprofessor.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoDTO {

    private Long id;
    private String name;
    private String email;
    private String matricula;
    private boolean active;
    private LocalDateTime createdAt;
    private Set<Long> turmaIds;
}
