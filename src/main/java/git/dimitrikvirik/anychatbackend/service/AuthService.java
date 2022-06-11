package git.dimitrikvirik.anychatbackend.service;


import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakService keycloakService;
    private final UserService userService;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getLoggedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt credentials = (Jwt) authentication.getCredentials();
        Long database_id = (Long) credentials.getClaims().get("database_id");
        if (database_id == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Empty user id to database in authorization token." +
                        "please contact to administrator!");
        return database_id;
    }
    public boolean sameUsername(String username){
     return getLoggedUserAccount().getUsername().equals(username);
    }


    @Transactional(readOnly = true)
    public UserAccount getLoggedUserAccount() {
        long loggedUserId = getLoggedUserId();
        return userService.get(loggedUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with id %d can't be find in database, but was specified in authorization service." +
                                "please contact to administrator!", loggedUserId))
        );
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserRepresentation getLoggedUserKeycloak() {
        return keycloakService.get(getKeycloakId()).toRepresentation();
    }

    @Transactional
    public void logout(String sessionId) {
        keycloakService.deleteSession(sessionId);
    }


    @Transactional
    public void logoutAll(String keycloakId, long userId) {
        keycloakService.logout(keycloakId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getSessionId() {
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String sessionId = (String) jwt.getTokenAttributes().getOrDefault("session_state", "NOT");
        if (sessionId.equals("NOT"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find sessionId in token");
        return sessionId;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getKeycloakId() {
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return jwt.getName();
    }
}
