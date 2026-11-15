package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role; 

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class UserCreateDTO implements Serializable {
    
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private String role; 
}
