package git.dimitrikvirik.anychatbackend.model.domain;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "photo")
    private String photo;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name ="keycloakId", unique = true )
    private String keycloakId;


    @Column(name = "about", length = 5000)
    private String about;




}
