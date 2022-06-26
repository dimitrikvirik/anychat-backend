package git.dimitrikvirik.anychatbackend.controller;


import git.dimitrikvirik.anychatbackend.facade.UserFacade;
import git.dimitrikvirik.anychatbackend.model.dto.UserAccountDTO;
import git.dimitrikvirik.anychatbackend.model.param.UserUpdateParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/{username}")
    public ResponseEntity<UserAccountDTO> getUser(@PathVariable String username){
        UserAccountDTO user = userFacade.getUser(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserAccountDTO> updateUser(@PathVariable String username ,@Valid @RequestBody UserUpdateParam userUpdateParam){
        return new ResponseEntity<>(userFacade.updateUser(username, userUpdateParam), HttpStatus.OK);
    }

    @PostMapping("/photo")
    public ResponseEntity<Void> uploadPhoto(@RequestPart MultipartFile file)
    {
        userFacade.uploadPhoto(file);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/photo/{imageid}")
    public void showImage(@PathVariable String imageid,HttpServletResponse response) throws Exception {

        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

        try {
            BufferedImage image = ImageIO.read(new File("photos/" + imageid));
            ImageIO.write(image, "jpeg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        byte[] imgByte = jpegOutputStream.toByteArray();

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(imgByte);
        responseOutputStream.flush();
        responseOutputStream.close();
    }



}
