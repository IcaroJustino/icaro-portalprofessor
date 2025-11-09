package portaldoprofessor.backend.controller;

import portaldoprofessor.backend.dto.UpdateUserDTO;
import portaldoprofessor.backend.dto.UserDTO;
import portaldoprofessor.backend.exception.EntityNotFoundException;
import portaldoprofessor.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> listAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/byid")
    public ResponseEntity<UserDTO> getById(@RequestParam Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody UpdateUserDTO request
    ) {
        UserDTO updated = userService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<UserDTO> deactivateUser(
            @RequestParam Long id,
            @RequestBody(required = false) Map<String, Boolean> body
    ) {
        boolean active = body != null && body.containsKey("active") ? body.get("active") : false;
        UserDTO updated = userService.toggleActive(id, active);
        return ResponseEntity.ok(updated);
    }
}
