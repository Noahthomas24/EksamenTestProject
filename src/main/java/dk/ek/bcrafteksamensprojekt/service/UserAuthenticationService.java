package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Users.UserMapper;
import dk.ek.bcrafteksamensprojekt.dto.Users.UserRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Users.UserResponseDTO;
import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.User;
import dk.ek.bcrafteksamensprojekt.repository.UserRepository;
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
}
