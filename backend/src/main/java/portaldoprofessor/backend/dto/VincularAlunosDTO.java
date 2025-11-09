package portaldoprofessor.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class VincularAlunosDTO {
    private Long turmaId;
    private List<Long> alunosIds;
}
