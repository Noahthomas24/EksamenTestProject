package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Users.UserMapper;
import dk.ek.bcrafteksamensprojekt.dto.Users.UserRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Users.UserResponseDTO;
import dk.ek.bcrafteksamensprojekt.exceptions.AlreadyExistsException;
import dk.ek.bcrafteksamensprojekt.model.User;
import dk.ek.bcrafteksamensprojekt.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    @Test
    void register_shouldCreateUserWithHashedPassword() {
        // Arrange
        UserRequestDTO dto = new UserRequestDTO(
                "jackd",
                "password123",
                "jack",
                "daniels"
        );

        User user = new User();
        user.setUsername("jackd");

        when(userRepository.existsByUsername("jackd")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(mock(UserResponseDTO.class));

        // Act
        UserResponseDTO result = userAuthenticationService.register(dto);

        // Assert
        assertNotNull(result);
        assertNotEquals("password123", user.getPassword()); // password is hashed
        verify(userRepository).save(user);
    }

    @Test
    void register_shouldThrowException_whenUsernameExists() {
        // Arrange
        UserRequestDTO dto = new UserRequestDTO(
                "existingUser",
                "password",
                "demo",
                "demo"
        );

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        // Act + Assert
        assertThrows(AlreadyExistsException.class,
                () -> userAuthenticationService.register(dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void findByUsername_shouldReturnUserOptional() {
        // Arrange
        User user = new User();
        user.setUsername("john");

        // Act
        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        Optional<User> result =
                userAuthenticationService.findByUsername("john");

        // Assert
        assertTrue(result.isPresent());
    }
}