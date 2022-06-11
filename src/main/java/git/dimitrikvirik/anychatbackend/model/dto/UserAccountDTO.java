package git.dimitrikvirik.anychatbackend.model.dto;


import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import lombok.Data;

@Data
public class UserAccountDTO {

    private String email;

    private String username;



    private String about;

    public static UserAccountDTO from(UserAccount userAccount) {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setEmail(userAccount.getEmail());
        userAccountDTO.setUsername(userAccount.getUsername());
        userAccountDTO.setAbout(userAccount.getAbout());
        return userAccountDTO;
    }
}
