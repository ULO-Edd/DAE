package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class AuthDTO implements Serializable {
    @NotBlank
    @NonNull
    private String email;
    @NotBlank
    @NonNull
    private String password;
}
