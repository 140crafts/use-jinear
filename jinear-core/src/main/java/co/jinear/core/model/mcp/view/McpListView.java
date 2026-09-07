package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * An unpaged list of payloads. The matching JSON Schema comes from
 * {@code McpSchemaGenerator.list(...)}.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpListView<T extends McpToolPayload> implements McpToolPayload {

    private List<T> items;
    private int count;

    public static <S, T extends McpToolPayload> McpListView<T> of(Collection<S> source, Function<S, T> mapper) {
        McpListView<T> view = new McpListView<>();
        view.setItems(Objects.isNull(source) ? List.of() : source.stream().map(mapper).toList());
        view.setCount(view.getItems().size());
        return view;
    }
}
