package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Users.UserMapper;
import dk.ek.bcrafteksamensprojekt.dto.Users.UserRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Users.UserResponseDTO;
import dk.ek.bcrafteksamensprojekt.exceptions.AlreadyExistsException;
import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.User;
import dk.ek.bcrafteksamensprojekt.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // --- Register new user ---
    public UserResponseDTO register(UserRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())){
            throw new AlreadyExistsException("Der findes allerede en bruger med dette brugernavn");
        }

        User user = userMapper.toEntity(dto);

        // Hash password before saving
        String hashed = BCrypt.hashpw(dto.password(), BCrypt.gensalt());
        user.setPassword(hashed);

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    // --- Authenticate user ---
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean verifyPassword(User user, String rawPassword) {
        return BCrypt.checkpw(rawPassword, user.getPassword());
    }

    // --- Fetch user DTO ---
    public UserResponseDTO getById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        return userMapper.toDto(u);
    }

    public boolean validateLogin(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        return session.getAttribute("user") != null;
    }
}
