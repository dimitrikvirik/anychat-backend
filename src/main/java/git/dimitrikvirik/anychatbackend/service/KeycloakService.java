package git.dimitrikvirik.anychatbackend.service;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import git.dimitrikvirik.anychatbackend.config.KeycloakClient;
import git.dimitrikvirik.anychatbackend.model.enums.KeycloakRole;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author dito
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)
@RequiredArgsConstructor
public class KeycloakService {
    @Value("${common.keycloak.token-uri}")
    private String tokenUri;

    private final KeycloakClient keycloakClient;

    private Keycloak keycloak;

    private String realm;

    private String clientId;

    private String clientSecret;

    @PostConstruct
    private void init() {
        keycloak = keycloakClient.getKeycloak();

        realm = keycloakClient.getRealm();

        clientId = keycloakClient.getName();

        clientSecret = keycloakClient.getSecret();
    }






    public UserRepresentation create(UserRepresentation userRepresentation) {
        keycloak.realm(realm).users().create(userRepresentation);
        return keycloak.realm(realm).users().search(userRepresentation.getUsername()).get(0);
    }

    public void update(UserRepresentation userRepresentation) {
        keycloak.realm(realm).users().get(userRepresentation.getId()).update(userRepresentation);
    }

    public void resetPassword(String keycloakId, CredentialRepresentation credentialRepresentation) {
        keycloak.realm(realm).users().get(keycloakId).resetPassword(credentialRepresentation);
    }

    public UserResource get(String keycloakId) {
        return keycloak.realm(realm).users().get(keycloakId);
    }

    public List<KeycloakRole> getRoles(String keycloakId) {
        RealmResource resource = keycloak.realm(realm);
        RoleScopeResource roleScopeResource = resource.users().get(keycloakId).roles().realmLevel();
        List<RoleRepresentation> effective = roleScopeResource.listEffective();
        return getRoleList(effective);
    }

    public List<KeycloakRole> getRoleList(List<RoleRepresentation> effective) {
        return Arrays.stream(KeycloakRole.values()).filter(
                (role -> effective.stream().anyMatch(
                        (e) -> e.getName().toUpperCase().equals(role.name())))
        ).collect(Collectors.toList());
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public List<RoleRepresentation> giveRole(String keycloakId, KeycloakRole role) {
        RealmResource resource = keycloak.realm(realm);
        RoleScopeResource roleScopeResource = resource.users().get(keycloakId).roles().realmLevel();
        List<RoleRepresentation> effective = roleScopeResource.listEffective();
        RoleRepresentation e = resource.roles().get(role.name().toLowerCase()).toRepresentation();
        effective.add(e);
        roleScopeResource.add(effective);
        return effective;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<RoleRepresentation> deleteRole(String keycloakId, KeycloakRole role) {
        RealmResource resource = keycloak.realm(realm);
        UserResource userResource = resource.users().get(keycloakId);
        RoleScopeResource roleScopeResource = userResource.roles().realmLevel();
        List<RoleRepresentation> list = roleScopeResource.listEffective()
                .stream().filter(
                        (ef) -> ef.getName().equals(role.name().toLowerCase())
                ).collect(Collectors.toList());
        roleScopeResource.remove(list);
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public UserSessionRepresentation getSession(String keycloakId, String sessionId) {

        return keycloak.realm(realm).users().get(keycloakId).getUserSessions().stream().filter((u -> u.getId().equals(sessionId))).findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User Session not found!")
        );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteSession(String sessionId) {
        try {
            keycloak.realm(realm).deleteSession(sessionId);
        } catch (NotFoundException notFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("session with id %s not found", sessionId));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void logout(String keycloakId) {
        keycloak.realm(realm).users().get(keycloakId).logout();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public HttpResponse<String> login(@NotNull  String username, @NotNull String password, @NotNull Boolean rememberMe) throws UnirestException {
        Unirest.setTimeouts(0, 0);

        String scope = "profile";
        if(rememberMe){
            scope += " offline_access";
        }

        return Unirest.post(tokenUri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("username", username)
                .field("password", password)
                .field("client_id", clientId)
                .field("grant_type", "password")
                .field("client_secret", clientSecret)
                .field("scope", scope)
                .asString();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public HttpResponse<String> reLogin(String refresh_token) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        return Unirest.post(tokenUri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("client_id", clientId)
                .field("client_secret", clientSecret)
                .field("grant_type", "refresh_token")
                .field("refresh_token", refresh_token)
                .asString();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void sendVerifyEmail(String keycloakId) {
        keycloak.realm(realm).users().get(keycloakId).sendVerifyEmail();
    }


}
