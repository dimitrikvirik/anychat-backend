package git.dimitrikvirik.anychatbackend.controller;

import git.dimitrikvirik.anychatbackend.model.domain.Message;
import git.dimitrikvirik.anychatbackend.model.dto.MessageDTO;
import git.dimitrikvirik.anychatbackend.model.param.MessageParam;
import git.dimitrikvirik.anychatbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;


    @GetMapping("/{id}/messages")
    public ResponseEntity<Page<Message>> getMessages(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chatService.getMessages(id, page, size));
    }


    @MessageMapping("/{id}/messages")
    @SendTo("/topic/{id}/messages")
    public MessageDTO greeting(@DestinationVariable Long id, @Payload String messageText, Principal principal) {
        try {
            String username = ((Jwt) ((JwtAuthenticationToken) principal).getCredentials()).getClaim("given_name");

            Message message = chatService.saveMessage(id, messageText, username);
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setUsername(message.getUser().getUsername());
            messageDTO.setText(message.getText());
            messageDTO.setProfile(message.getUser().getPhoto());
            messageDTO.setCreatedAt(message.getCreatedAt().toString());

            return messageDTO;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't handle message");
        }
    }


}
