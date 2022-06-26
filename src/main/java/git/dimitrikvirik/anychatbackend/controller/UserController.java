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

import javax.validation.Valid;

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

    @PostMapping("/{username}/photo")
    public ResponseEntity<String> uploadPhoto(@RequestPart MultipartFile file)
    {
        return new ResponseEntity<>(userFacade.uploadPhoto(file), HttpStatus.OK);
    }



}
