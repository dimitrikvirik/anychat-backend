package git.dimitrikvirik.anychatbackend.model.dto;

import lombok.Data;

@Data
public class MessageDTO {

    private String text;
    private String username;
    private String profile;
    private String createdAt;

}
