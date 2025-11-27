package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.model.User;
import dk.ek.bcrafteksamensprojekt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserAuthenticationServiceTest {

    private UserRepository userRepository;
    private UserAuthenticationService userAuthenticationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userAuthenticationService = new UserAuthenticationService(userRepository);
    }

    // ---------- findByUsername ----------

    @Test
    void findByUsername_returnsUserWhenFound() {
        // Arrange
        String username = "admin";
        User u = new User();
        u.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(u));

        // Act
        Optional<User> result = userAuthenticationService.findByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_returnsEmptyWhenNotFound() {
        // Arrange
        String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userAuthenticationService.findByUsername(username);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByUsername(username);
    }

    // ---------- verifyPassword ----------

    @Test
    void verifyPassword_returnsTrueForCorrectPassword() {
        // Arrange
        String plainPassword = "secret123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(plainPassword);

        User u = new User();
        u.setPassword(hash);

        // Act
        boolean result = userAuthenticationService.verifyPassword(u, plainPassword);

        // Assert
        assertTrue(result);
    }

    @Test
    void verifyPassword_returnsFalseForWrongPassword() {
        // Arrange
        String plainPassword = "secret123";
        String wrongPassword = "wrongPassword";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(plainPassword);

        User u = new User();
        u.setPassword(hash);

        // Act
        boolean result = userAuthenticationService.verifyPassword(u, wrongPassword);

        // Assert
        assertFalse(result);
    }

    // ---------- createUser ----------

    @Test
    void createUser_encodesPasswordAndSavesUser() {
        // Arrange
        String username = "admin";
        String plainPassword = "secret123";

        // capture the user that is saved
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // We can just return the same user we capture
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userAuthenticationService.createUser(username, plainPassword);

        // Assert: repository was called
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(username, savedUser.getUsername());

        // Password should NOT be stored in plain text
        assertNotEquals(plainPassword, savedUser.getPassword());

        // But the hash should still match when using BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(plainPassword, savedUser.getPassword()));

        // And result should be the same as saved user
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getPassword(), result.getPassword());
    }
}
