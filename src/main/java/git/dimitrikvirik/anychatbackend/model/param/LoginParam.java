package git.dimitrikvirik.anychatbackend.model.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class LoginParam {

    @NotBlank
    @Length(min = 3, max = 30)
    private String Username;

    @NotBlank
    @Length(min = 8, max = 20)
    private String Password;

    @NotNull
    private Boolean rememberMe = false;

}
