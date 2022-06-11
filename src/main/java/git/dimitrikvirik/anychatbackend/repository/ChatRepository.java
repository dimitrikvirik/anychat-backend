package git.dimitrikvirik.anychatbackend.repository;

import git.dimitrikvirik.anychatbackend.model.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {



}