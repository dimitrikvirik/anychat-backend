package git.dimitrikvirik.anychatbackend.service;


import git.dimitrikvirik.anychatbackend.model.domain.Code;
import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import git.dimitrikvirik.anychatbackend.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakService keycloakService;
    private final UserService userService;

    private final CodeRepository codeRepository;

    private final JavaMailSender javaMailSender;

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

    public boolean sameUsername(String username) {
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


    @Transactional

    public Code resetPasswordCode(String email, String username) {

        codeRepository.deleteAllByEmail(email);
        //random 6 digit code
        String code = String.valueOf(Math.random()).substring(2, 8);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom("test@mrecords.me");
            helper.setTo(email);
            helper.setSubject("Reset password code");
            helper.setText("Your code for username " + username + "  is: " + code);
        javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        Code codeEntity = new Code();
        codeEntity.setEmail(email);
        codeEntity.setCode(code);
        return codeRepository.save(codeEntity);
    }

    public boolean checkCode(String email, String code) {
        return codeRepository.existsByEmailAndCode(email, code);
    }
}
