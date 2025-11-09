package portaldoprofessor.backend.service;

import portaldoprofessor.backend.dto.CreateUserDTO;
import portaldoprofessor.backend.dto.UpdateUserDTO;
import portaldoprofessor.backend.dto.UserDTO;
import portaldoprofessor.backend.entity.Role;
import portaldoprofessor.backend.entity.User;
import portaldoprofessor.backend.exception.EntityNotFoundException;
import portaldoprofessor.backend.exception.UsernameUniqueViolationException;
import portaldoprofessor.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import portaldoprofessor.backend.security.UserContextService;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserContextService userContextService;

    private String gerarMatricula() {
        Random random = new Random();
        String currentYear = String.valueOf(LocalDate.now().getYear());
        String turma = String.valueOf(LocalDate.now().getMonthValue());
        int randMatricula = random.nextInt(1000);
        String matricula = String.format("%05d", randMatricula);
        return currentYear + turma + matricula;
    }

    public UserDTO create(CreateUserDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UsernameUniqueViolationException("Já existe um usuário com esse e-mail cadastrado.");
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .matricula(gerarMatricula())
                .role(request.getRole())
                .active(true)
                .build();

        userRepository.save(newUser);
        return toDTO(newUser);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .matricula(user.getMatricula())
                        .role(user.getRole())
                        .active(user.isActive())
                        .createdAt(user.getCreatedAt())
                        .build())
                .toList(); // usando Java 16+ toList()
    }

    public java.util.Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    public UserDTO update(Long id, UpdateUserDTO request) {
        if (!userContextService.hasAnyRole(Role.ADMIN, Role.TEACHER)) {
            throw new RuntimeException("Acesso negado: apenas ADMIN ou PROFESSOR podem atualizar usuários");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            userRepository.findByEmail(request.getEmail())
                    .filter(existing -> !existing.getId().equals(user.getId()))
                    .ifPresent(existing -> {
                        throw new UsernameUniqueViolationException("E-mail já está sendo usado por outro usuário.");
                    });
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        userRepository.save(user);
        return toDTO(user);
    }

    public void delete(Long id) {
        if (!userContextService.hasAnyRole(Role.ADMIN)) {
            throw new RuntimeException("Acesso negado: apenas ADMIN ou PROFESSOR podem deletar usuários");
        }
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado para exclusão.");
        }
        if (
                userContextService.getLoggedUser().getId().equals(id)
        ) {
            throw new UsernameUniqueViolationException("Usuario não pode se deletar");
        }
        userRepository.deleteById(id);
    }

    public UserDTO toggleActive(Long id, boolean active) {
        if (!userContextService.hasAnyRole(Role.ADMIN)) {
            throw new RuntimeException("Acesso negado: apenas ADMIN ou PROFESSOR podem desativar usuários");
        }




        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(userContextService.getLoggedUser().getId().equals(id)) {
            throw new UsernameUniqueViolationException("Usuario não pode se deletar");
        }
        user.setActive(active);
        userRepository.save(user);
        return toDTO(user);
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .matricula(user.getMatricula())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
