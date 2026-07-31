package co.jinear.core.model.response.notebook;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notebook.NotebookMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotebookMemberPaginatedResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<NotebookMemberDto> notebookMemberDtoPageDto;
}
