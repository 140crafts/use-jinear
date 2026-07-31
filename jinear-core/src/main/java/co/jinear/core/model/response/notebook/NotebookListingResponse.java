package co.jinear.core.model.response.notebook;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotebookListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<NotebookDto> notebookDtoPageDto;
}
