package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserDTO {
    private String name;
    private String email;
    private Role role;

    @JsonProperty("is_active")
    private Boolean isActive;
}