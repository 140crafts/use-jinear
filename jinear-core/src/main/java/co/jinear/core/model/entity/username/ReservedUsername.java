package co.jinear.core.model.entity.username;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "reserved_username")
public class ReservedUsername {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reserved_username_id")
    private int reservedUsernameId;

    @Column(name = "username")
    private String username;
}
