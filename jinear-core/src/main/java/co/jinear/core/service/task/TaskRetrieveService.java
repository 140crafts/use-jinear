package co.jinear.core.service.task;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskRetrieveService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public Task retrieveEntity(String taskId) {
        return taskRepository.findByTaskIdAndPassiveIdIsNull(taskId)
                .orElseThrow(NotFoundException::new);
    }

    public TaskDto retrieve(String taskId) {
        return taskRepository.findByTaskIdAndPassiveIdIsNull(taskId)
                .map(task -> modelMapper.map(task, TaskDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public Long countAllByTopicId(String topicId) {
        log.info("Count all by topic id has started for topicId: {}", topicId);
        Long count = taskRepository.countAllByTopicId(topicId);
        log.info("Found [{}] tasks with topicId: {}", count, topicId);
        return count;
    }


}
