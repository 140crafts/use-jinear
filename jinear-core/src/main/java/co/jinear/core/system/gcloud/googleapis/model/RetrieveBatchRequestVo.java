package co.jinear.core.system.gcloud.googleapis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveBatchRequestVo {

    private String userId;
    private String resourceId;

    @Override
    public String toString() {
        return "RetrieveBatchRequestVo{" +
                "userId='" + userId + '\'' +
                ", resourceId='" + resourceId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RetrieveBatchRequestVo that = (RetrieveBatchRequestVo) o;

        if (!Objects.equals(userId, that.userId)) return false;
        return Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
        return result;
    }
}
