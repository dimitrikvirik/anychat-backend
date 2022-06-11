package git.dimitrikvirik.anychatbackend.model.domain;


import lombok.Data;

import javax.persistence.*;

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

}
