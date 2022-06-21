package git.dimitrikvirik.anychatbackend.model.param;

import git.dimitrikvirik.anychatbackend.validation.ValidEmail;
import lombok.Data;

import javax.validation.Valid;

@Data
@Valid
public class ResetPasswordCodeParam {

    @ValidEmail
    private String email;

}
