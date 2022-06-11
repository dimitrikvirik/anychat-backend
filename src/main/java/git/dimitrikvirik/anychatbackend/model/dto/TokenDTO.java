package git.dimitrikvirik.anychatbackend.model.dto;

import lombok.Data;

@Data
public class TokenDTO {

    private String access_token;

    private String expires_in;

    private String refresh_token;

    private String refresh_expires_in;
}
