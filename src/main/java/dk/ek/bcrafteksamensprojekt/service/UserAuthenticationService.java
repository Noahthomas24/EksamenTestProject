package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.model.User;
import dk.ek.bcrafteksamensprojekt.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Had to make constructor injection here as the passwordEncoder could not be recognized as a bean with RequiredArgsConstructor
    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean verifyPassword(User user, String plainPassword) {
        return bCryptPasswordEncoder.matches(plainPassword, user.getPassword());
    }

    // Will probably not be used as we only have 1 user, but good to have.
    public User createUser(String username, String plainPassword) {
        String hash = bCryptPasswordEncoder.encode(plainPassword);
        User u = new User();
        u.setUsername(username);
        u.setPassword(hash);
        return userRepository.save(u);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


}
