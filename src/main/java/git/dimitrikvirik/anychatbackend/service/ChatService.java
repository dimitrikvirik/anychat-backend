package git.dimitrikvirik.anychatbackend.service;

import git.dimitrikvirik.anychatbackend.model.domain.Chat;
import git.dimitrikvirik.anychatbackend.model.domain.Message;
import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import git.dimitrikvirik.anychatbackend.model.param.MessageParam;
import git.dimitrikvirik.anychatbackend.repository.ChatRepository;
import git.dimitrikvirik.anychatbackend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    private final UserService userService;


    public Page<Message> getMessages(Long chatId, int page, int size) {
        return messageRepository.findAllByChatId(chatId, PageRequest.of(page, size, Sort.Direction.DESC, "createdAt"));
    }

    public Message saveMessage(Long id, MessageParam messageParam, String name) {

        Chat chat = chatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        UserAccount user = userService.getByUsername(name).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setText(messageParam.getText());

       return messageRepository.save(message);
    }
}
