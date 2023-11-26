package co.jinear.core.system.gcloud.googleapis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveBatchRequestVo {

    private String userId;
    private String resourceId;
}
