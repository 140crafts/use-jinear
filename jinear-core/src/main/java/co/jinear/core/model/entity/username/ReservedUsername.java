package co.jinear.core.model.entity.username;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reserved_username")
public class ReservedUsername {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserved_username_id")
    private int reservedUsernameId;

    @Column(name = "username")
    private String username;
}
