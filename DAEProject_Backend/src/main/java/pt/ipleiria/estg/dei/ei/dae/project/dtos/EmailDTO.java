package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class EmailDTO implements Serializable {
    @NonNull
    @NotBlank
    private String subject;
    @NonNull
    @NotBlank
    private String body;
}
