package git.dimitrikvirik.anychatbackend.controller;


import git.dimitrikvirik.anychatbackend.facade.AuthFacade;
import git.dimitrikvirik.anychatbackend.model.dto.CodeDTO;
import git.dimitrikvirik.anychatbackend.model.dto.TokenDTO;
import git.dimitrikvirik.anychatbackend.model.param.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthFacade authFacade;


    @GetMapping("/is-user/{username}")
    public ResponseEntity<Boolean> isUser(@PathVariable String username) {
        return ResponseEntity.ok(authFacade.isUser(username));
    }

    @GetMapping("/is-email/{email}")
    public ResponseEntity<Boolean> isEmail(@PathVariable String email) {
        return ResponseEntity.ok(authFacade.isEmail(email));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody @Valid RegisterParam registerParam){
      return new ResponseEntity<>(authFacade.register(registerParam),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginParam loginParam){
      return new ResponseEntity<>(authFacade.login(loginParam), HttpStatus.OK);
    }

    @PostMapping("/reset-password-code")
    public ResponseEntity<Void> resetPasswordCode(@RequestBody @Valid ResetPasswordCodeParam resetPasswordCodeParam){
         authFacade.resetPasswordCOde(resetPasswordCodeParam);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordParam resetPasswordParam) {
        authFacade.resetPassword(resetPasswordParam);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/re-login")
    public ResponseEntity<String> reLogin(@RequestBody @Valid ReLoginParam reLoginParam){
        return new ResponseEntity<>(authFacade.reLogin(reLoginParam), HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        return new ResponseEntity<>(authFacade.logout(), HttpStatus.OK);
    }


}
