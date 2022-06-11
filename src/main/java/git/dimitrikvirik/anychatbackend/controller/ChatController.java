package git.dimitrikvirik.anychatbackend.controller;

import git.dimitrikvirik.anychatbackend.model.domain.Message;
import git.dimitrikvirik.anychatbackend.model.param.MessageParam;
import git.dimitrikvirik.anychatbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;


    @GetMapping("/{id}/messages")
    public ResponseEntity<Page<Message>> getMessages(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chatService.getMessages(id, page, size));
    }


    @MessageMapping("/{id}/messages")
    @SendTo("/topic/{id}/messages")
    public Message greeting(@DestinationVariable Long id, @Payload MessageParam messageParam, Principal principal) {
        return chatService.saveMessage(id, messageParam, principal.getName());

    }


}
