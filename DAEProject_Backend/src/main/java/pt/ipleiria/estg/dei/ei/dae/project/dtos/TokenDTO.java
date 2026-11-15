package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class TokenDTO implements Serializable {
    @NonNull private String token;
    private LoginDTO user;

    public static TokenDTO from(String token) {
        return new TokenDTO(
                token
        );
    }
}
