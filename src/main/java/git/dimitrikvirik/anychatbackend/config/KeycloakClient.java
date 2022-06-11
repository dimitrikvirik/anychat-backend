package git.dimitrikvirik.anychatbackend.config;

import lombok.Data;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
public class KeycloakClient {

    private final Keycloak keycloak;

    @Value("${keycloak.resource}")
    private String name;

    @Value("${keycloak.realm}")
    private String realm;

    private String id;

    private ClientRepresentation representation;

    private String secret;

    @PostConstruct
    private void initSecret() {
        representation = keycloak.realm(realm).clients().findByClientId(name).get(0);
        id = representation.getId();
        secret = keycloak.realm(realm).clients().get(id).getSecret().getValue();
    }


}
