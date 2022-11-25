package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.vo.task.TaskInitializeVo;
import co.jinear.core.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskInitializeService {

    private final TaskRetrieveService taskRetrieveService;
    private final TaskRepository taskRepository;
    private final TaskLockService taskLockService;
    private final ModelMapper modelMapper;

    public TaskDto initializeTask(TaskInitializeVo taskInitializeVo) {
        log.info("Initialize task has started. taskInitializeVo: {}", taskInitializeVo);
        taskLockService.lockTopicForTaskInitialization(taskInitializeVo.getTopicId());
        Task task = modelMapper.map(taskInitializeVo, Task.class);
        assignTaskNo(task);
        Task saved = taskRepository.save(task);
        taskLockService.unlockTopicForTaskInitialization(taskInitializeVo.getTopicId());
        return modelMapper.map(saved, TaskDto.class);
    }

    private void assignTaskNo(Task task) {
        Long count = taskRetrieveService.countAllByTopicId(task.getTopicId());
        task.setTagNo(count.intValue() + 1);
    }
}
