package git.dimitrikvirik.anychatbackend.facade;


import com.google.common.io.Files;
import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import git.dimitrikvirik.anychatbackend.model.dto.UserAccountDTO;
import git.dimitrikvirik.anychatbackend.model.param.UserUpdateParam;
import git.dimitrikvirik.anychatbackend.service.AuthService;
import git.dimitrikvirik.anychatbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.UUID;

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

    public String uploadPhoto(MultipartFile file) {
        if (file.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "File is Empty!");

        try {
            String profileImage = UUID.randomUUID() + ".jpg";
            BufferedImage imBuff = ImageIO.read(file.getInputStream());

            UserAccount loggedUserAccount = authService.getLoggedUserAccount();
            loggedUserAccount.setPhoto(profileImage);
            ImageIO.write(imBuff, "jpg", new File("photos/" +  profileImage));
            userService.save(loggedUserAccount);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return null;

    }
}
