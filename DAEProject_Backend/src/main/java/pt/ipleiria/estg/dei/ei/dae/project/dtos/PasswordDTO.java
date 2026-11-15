package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO implements Serializable {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
