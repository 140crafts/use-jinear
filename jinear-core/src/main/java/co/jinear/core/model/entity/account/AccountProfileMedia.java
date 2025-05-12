package co.jinear.core.model.entity.account;

import co.jinear.core.model.entity.media.Media;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_account_profile_media")
public class AccountProfileMedia {

    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "media_id")
    private String mediaId;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "media_id", insertable = false, updatable = false)
    private Media media;
}
