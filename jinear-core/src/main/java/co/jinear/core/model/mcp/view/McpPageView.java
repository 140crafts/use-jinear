package co.jinear.core.model.mcp.view;

import co.jinear.core.model.dto.PageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * One page of payloads, as every paging MCP tool returns it. The matching JSON Schema comes
 * from {@code McpSchemaGenerator.page(...)}.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpPageView<T extends McpToolPayload> implements McpToolPayload {

    private List<T> items;
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;

    /**
     * Keeps the source page's paging fields while carrying an item list the caller already
     * mapped and narrowed.
     */
    public static <S, T extends McpToolPayload> McpPageView<T> of(PageDto<S> pageDto, List<T> items) {
        McpPageView<T> view = new McpPageView<>();
        view.setItems(items);
        view.setPage(pageDto.getNumber());
        view.setPageSize(pageDto.getSize());
        view.setTotalElements(pageDto.getTotalElements());
        view.setTotalPages(pageDto.getTotalPages());
        view.setHasNext(pageDto.isHasNext());
        return view;
    }

    public static <S, T extends McpToolPayload> McpPageView<T> of(PageDto<S> pageDto, Function<S, T> mapper) {
        McpPageView<T> view = new McpPageView<>();
        view.setItems(Objects.isNull(pageDto.getContent())
                ? List.of()
                : pageDto.getContent().stream().map(mapper).toList());
        view.setPage(pageDto.getNumber());
        view.setPageSize(pageDto.getSize());
        view.setTotalElements(pageDto.getTotalElements());
        view.setTotalPages(pageDto.getTotalPages());
        view.setHasNext(pageDto.isHasNext());
        return view;
    }
}
