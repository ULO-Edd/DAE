package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ToggleHidePayloadDTO implements Serializable {
    @NotBlank
    @NonNull
    @JsonProperty("is_hidden")
    private boolean hidden;
}
