package git.dimitrikvirik.anychatbackend.repository;

import git.dimitrikvirik.anychatbackend.model.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {


    Page<Message> findAllByChatId(Long chatId,  Pageable pageable);

}