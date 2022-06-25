package git.dimitrikvirik.anychatbackend.model.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateParam {


    private String firstname;


    private String lastname;

    private String about;

}
