package co.jinear.core.model.request.workspace;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class WorkspacePrefetchTimespanRequest {

    @NotNull
    private ZonedDateTime timespanStart;
    @NotNull
    private ZonedDateTime timespanEnd;
}
