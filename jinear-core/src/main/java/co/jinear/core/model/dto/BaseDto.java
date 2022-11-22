package co.jinear.core.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class BaseDto {

    private Date createdDate;
    private Date lastUpdatedDate;
    private String passiveId;
}
