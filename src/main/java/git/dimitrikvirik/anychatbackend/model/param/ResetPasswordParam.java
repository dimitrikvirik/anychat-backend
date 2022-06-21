package git.dimitrikvirik.anychatbackend.model.param;

import lombok.Data;

import javax.validation.Valid;

@Data
@Valid
public class ResetPasswordParam {

    private String email;

    private String code;

    private String password;

}
