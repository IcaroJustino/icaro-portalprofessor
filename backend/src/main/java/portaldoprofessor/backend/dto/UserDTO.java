package portaldoprofessor.backend.dto;

import portaldoprofessor.backend.entity.Role;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String matricula;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
}
