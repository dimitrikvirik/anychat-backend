package git.dimitrikvirik.anychatbackend.repository;


import git.dimitrikvirik.anychatbackend.model.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByKeycloakId(String keycloakId);

}