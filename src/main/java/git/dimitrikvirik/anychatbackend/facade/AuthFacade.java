package git.dimitrikvirik.anychatbackend.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import git.dimitrikvirik.anychatbackend.model.dto.TokenDTO;
import git.dimitrikvirik.anychatbackend.model.mapper.KeycloakMapper;
import git.dimitrikvirik.anychatbackend.model.param.LoginParam;
import git.dimitrikvirik.anychatbackend.model.param.ReLoginParam;
import git.dimitrikvirik.anychatbackend.model.param.RegisterParam;
import git.dimitrikvirik.anychatbackend.service.AuthService;
import git.dimitrikvirik.anychatbackend.service.KeycloakService;
import git.dimitrikvirik.anychatbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final UserService userService;
    private final KeycloakService keycloakService;





    public TokenDTO register(RegisterParam registerParam) {

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(registerParam.getEmail());
        userAccount.setUsername(registerParam.getUsername());

        UserAccount userAccountSaved = userService.save(userAccount);
        UserRepresentation userRepresentation = keycloakService.create(KeycloakMapper.toRepresentation(
                registerParam.getUsername(),
                registerParam.getUsername(),
                registerParam.getUsername(),
                registerParam.getPassword(),
                userAccountSaved.getId()
        ));

        userAccount.setKeycloakId(userRepresentation.getId());
        userService.save(userAccount);
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername(registerParam.getUsername());
        loginParam.setPassword(registerParam.getPassword());

        return login(loginParam);
    }


    public TokenDTO login(LoginParam loginParam) {
        try {
            HttpResponse<String> login = keycloakService.login(loginParam.getUsername(), loginParam.getPassword(), loginParam.getRememberMe());
            if (login.getStatus() == 401) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


            return   objectMapper.readValue(login.getBody(), TokenDTO.class);
        }
        catch (UnirestException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Server error! please try again or contact the administration!");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String reLogin(ReLoginParam reLoginParam) {
        try {
            HttpResponse<String> reLogin = keycloakService.reLogin(reLoginParam.getRefresh_token());
            if (reLogin.getStatus() == 401) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong refresh token ");
            }
            return reLogin.getBody();
        }
        catch (UnirestException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Server error! please try again or contact the administration!");
        }
    }

    public Void logout() {
        authService.logout(authService.getSessionId());
        return null;
    }
}
