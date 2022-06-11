package git.dimitrikvirik.anychatbackend.model.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateParam {

    @Length(min = 3, max = 30)
    @NotBlank
    private String Firstname;

    @Length(min = 3, max = 30)
    @NotBlank
    private String Lastname;

    private String About;

}
