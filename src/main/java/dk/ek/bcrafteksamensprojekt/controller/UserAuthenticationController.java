package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.model.User;
import dk.ek.bcrafteksamensprojekt.service.UserAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {
    private final UserAuthenticationService userAuthenticationService;

    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletRequest request) {
        var maybeUser = userAuthenticationService.findByUsername(req.username());
        if (maybeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        var user = maybeUser.get();
        if (!userAuthenticationService.verifyPassword(user, req.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        HttpSession session = request.getSession(true);

        // create user object that only stores id and username (no password)
        User userSession = new User(user.getId(), user.getUsername());
        session.setAttribute("user", userSession);

        return ResponseEntity.ok(Map.of("message", "Logged in"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }


}

