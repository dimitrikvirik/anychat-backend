package git.dimitrikvirik.anychatbackend.facade;


import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import git.dimitrikvirik.anychatbackend.model.dto.UserAccountDTO;
import git.dimitrikvirik.anychatbackend.model.param.UserUpdateParam;
import git.dimitrikvirik.anychatbackend.service.AuthService;
import git.dimitrikvirik.anychatbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final AuthService authService;


    public UserAccountDTO getUser(String username) {
        UserAccount userAccount = userService.getByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not found")
        );
        return UserAccountDTO.from(userAccount);
    }

    public UserAccountDTO updateUser(String username, UserUpdateParam userUpdateParam) {
       if(!authService.sameUsername(username)){
           throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not same user!");
       }

        UserAccount userAccount = userService.getByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not found"));

        userAccount.setFirstname(userUpdateParam.getFirstname());
        userAccount.setLastname(userUpdateParam.getLastname());
        userAccount.setAbout(userUpdateParam.getAbout());
        userService.save(userAccount);
        return UserAccountDTO.from(userAccount);
    }
}
