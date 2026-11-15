package pt.ipleiria.estg.dei.ei.dae.project.dtos;

// Adiciona este import (já o tens no projeto por causa do passo anterior)
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserDTO implements Serializable {

    @NonNull private long id;
    @NonNull private String name;
    @NonNull private String email;
    @NonNull private Role role;

    @JsonProperty("is_active") 
    @NonNull private boolean isActive;

    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }
}
