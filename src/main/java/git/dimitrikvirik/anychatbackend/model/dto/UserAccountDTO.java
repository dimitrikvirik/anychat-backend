package git.dimitrikvirik.anychatbackend.model.dto;


import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import lombok.Data;

@Data
public class UserAccountDTO {

    private String email;

    private String profile;

    private String firstname;

    private String lastname;

    private String username;

    private String about;

    public static UserAccountDTO from(UserAccount userAccount) {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setProfile(userAccount.getPhoto());
        userAccountDTO.setFirstname(userAccount.getFirstname());
        userAccountDTO.setLastname(userAccount.getLastname());
        userAccountDTO.setEmail(userAccount.getEmail());
        userAccountDTO.setUsername(userAccount.getUsername());
        userAccountDTO.setAbout(userAccount.getAbout());
        return userAccountDTO;
    }
}
