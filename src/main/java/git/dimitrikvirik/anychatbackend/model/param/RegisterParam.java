package git.dimitrikvirik.anychatbackend.model.param;


import git.dimitrikvirik.anychatbackend.validation.ValidEmail;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Valid
public class RegisterParam {

    @Length(min = 3, max = 30)
    @NotBlank
    private String username;

    @ValidEmail
    @NotBlank
    private String email;

    @Length(min = 8, max = 20)
    @NotBlank
    private String password;
}
