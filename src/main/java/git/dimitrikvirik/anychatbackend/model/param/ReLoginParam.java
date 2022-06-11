package git.dimitrikvirik.anychatbackend.model.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Valid
public class ReLoginParam {


    @NotBlank
    private String refresh_token;

}
