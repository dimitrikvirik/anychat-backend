package git.dimitrikvirik.anychatbackend.model.domain;


import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class Chat {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne
    private UserAccount owner;

    @ManyToMany
   @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<UserAccount> onlineUsers = new ArrayList<>();

}
