package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import jakarta.json.bind.annotation.JsonbProperty;
import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginDTO implements Serializable {

    @NonNull private long id;
    @NonNull private String name;
    @NonNull private String email;
    @NonNull private Role role;
    @NonNull private boolean mustChangePassword;

    public static LoginDTO from(User user) {
        return new LoginDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isMustChangePassword()
        );
    }

    public static List<LoginDTO> from(List<User> users) {
        return users.stream().map(LoginDTO::from).collect(Collectors.toList());
    }
}
