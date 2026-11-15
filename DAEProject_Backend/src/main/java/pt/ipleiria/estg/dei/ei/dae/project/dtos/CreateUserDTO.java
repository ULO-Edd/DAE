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
public class CreateUserDTO implements Serializable {

    @NonNull private String name;
    @NonNull private String email;
    @NonNull private String password;
    @NonNull private Role role;


    public static CreateUserDTO from(User user) {
        return new CreateUserDTO  (
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    public static List<CreateUserDTO> from(List<User> users) {
        return users.stream().map(CreateUserDTO::from).collect(Collectors.toList());
    }
}
