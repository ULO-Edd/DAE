package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDTO implements Serializable {
    private String name;
    private String email;
}
