package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.DetailedTaskSubscriptionDto;
import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.model.entity.task.TaskSubscription;
import co.jinear.core.model.response.task.TaskSubscriptionResponse;
import co.jinear.core.model.vo.task.TaskSubscriptionInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskSubscriptionConverter {

    TaskSubscription map(TaskSubscriptionInitializeVo taskSubscriptionInitializeVo);

    TaskSubscriptionDto map(TaskSubscription taskSubscription);

    DetailedTaskSubscriptionDto mapDetailed(TaskSubscription taskSubscription);

    TaskSubscriptionInitializeVo map(String accountId, String taskId);

    TaskSubscriptionResponse map(TaskSubscriptionDto taskSubscriptionDto);
}
