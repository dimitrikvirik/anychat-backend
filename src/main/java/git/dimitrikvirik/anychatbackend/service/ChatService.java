package git.dimitrikvirik.anychatbackend.service;

import git.dimitrikvirik.anychatbackend.model.domain.Chat;
import git.dimitrikvirik.anychatbackend.model.domain.Message;
import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import git.dimitrikvirik.anychatbackend.model.dto.MessageDTO;
import git.dimitrikvirik.anychatbackend.model.param.MessageParam;
import git.dimitrikvirik.anychatbackend.repository.ChatRepository;
import git.dimitrikvirik.anychatbackend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    private final UserService userService;


    public Page<MessageDTO> getMessages(Long chatId, int page, int size) {
        return messageRepository.findAllByChatId(chatId, PageRequest.of(page, size, Sort.Direction.DESC, "createdAt")).map(
                message -> {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setUsername(message.getUser().getUsername());
                    messageDTO.setText(message.getText());
                    messageDTO.setProfile(message.getUser().getPhoto());
                    messageDTO.setCreatedAt(message.getCreatedAt().toString());
                    return messageDTO;
                }
        );
    }

    public Message saveMessage(Long id, String messageText, String username) {

        Chat chat = chatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        UserAccount user = userService.getByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setText(messageText);

       return messageRepository.save(message);
    }

    @Transactional
    public long addOnlineUser(Long id ,String keycloakId){
        UserAccount userAccount = userService.getByKeycloakId(keycloakId);
        Chat chat = chatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        if(chat.getOnlineUsers().stream().filter(u -> u.getKeycloakId().equals(keycloakId)).findAny().isEmpty()) {
            chat.getOnlineUsers().add(userAccount);
            chatRepository.save(chat);
            System.out.println("Online now : " + chat.getOnlineUsers().size());
        }
        return chat.getOnlineUsers().size();

    }

    @Transactional
    public long removeOnlineUser(long id, String keycloakId) {

        Chat chat = chatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        chat.getOnlineUsers().removeIf(u -> u.getKeycloakId().equals(keycloakId));
        chatRepository.save(chat);
        System.out.println("Online now : "  + chat.getOnlineUsers().size());
        return chat.getOnlineUsers().size();
    }

    public int getOnline(Long id) {
        Chat chat = chatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        return chat.getOnlineUsers().size();
    }
}
