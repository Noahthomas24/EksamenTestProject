package dk.ek.bcrafteksamensprojekt.dto.Users;

import dk.ek.bcrafteksamensprojekt.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        User u = new User();
        u.setUsername(dto.username());
        u.setPassword(dto.password()); // hashing will occur in service
        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        return u;
    }

    public UserResponseDTO toDto(User u) {
        return new UserResponseDTO(
                u.getId(),
                u.getUsername(),
                u.getFirstName(),
                u.getLastName()
        );
    }
}