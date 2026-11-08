package co.jinear.core.model.entity.note;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "mv_note_fts")
public class NoteFts {

    @Id
    @Column(name = "note_id")
    private String noteId;

    @Column(name = "workspace_id")
    private String workspaceId;
}
