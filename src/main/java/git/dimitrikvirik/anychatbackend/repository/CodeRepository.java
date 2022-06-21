package git.dimitrikvirik.anychatbackend.repository;

import git.dimitrikvirik.anychatbackend.model.domain.Chat;
import git.dimitrikvirik.anychatbackend.model.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Long> {

    void deleteAllByEmail(String email);

    boolean existsByEmailAndCode(String email, String code);



}