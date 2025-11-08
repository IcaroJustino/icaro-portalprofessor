package portaldoprofessor.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import portaldoprofessor.backend.entity.Role;
import portaldoprofessor.backend.entity.User;
import portaldoprofessor.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserContextService {

    private final UserRepository userRepository;

    /**
     * Retorna o usuário logado a partir do token.
     */
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    /**
     * Verifica se o usuário logado tem a role ADMIN.
     */
    public boolean isAdmin() {
        return getLoggedUser().getRole() == Role.ADMIN;
    }

    /**
     * Verifica se o usuário logado tem a role PROFESSOR.
     */
    public boolean isProfessor() {
        return getLoggedUser().getRole() == Role.TEACHER;
    }

    /**
     * Permite verificar se o usuário tem uma das roles necessárias.
     */
    public boolean hasAnyRole(Role... roles) {
        Role userRole = getLoggedUser().getRole();
        for (Role role : roles) {
            if (userRole == role) return true;
        }
        return false;
    }
}
