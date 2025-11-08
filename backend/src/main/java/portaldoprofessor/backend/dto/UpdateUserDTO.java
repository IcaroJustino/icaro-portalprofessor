package portaldoprofessor.backend.dto;

import portaldoprofessor.backend.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {

    private String name;

    @Email(message = "Formato de e-mail inválido")
    private String email;

    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
    private String password;

    private Role role;
    private Boolean active;
}
