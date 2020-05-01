package pl.ymz.todoapp.logicservice;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.model.projectiondto.GroupOfTasksReadModel;
import pl.ymz.todoapp.model.projectiondto.GroupOfTasksWriteModel;
import pl.ymz.todoapp.rpository.TaskGroupRepository;
import pl.ymz.todoapp.rpository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {

    private TaskGroupRepository taskGroupRepository;
    private TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository taskGroupRepository,
                     final TaskRepository taskRepository) {
        this.taskGroupRepository = taskGroupRepository;
        this.taskRepository = taskRepository;
    }

    public GroupOfTasksReadModel createGroup(GroupOfTasksWriteModel source) {
        TaskGroup result = taskGroupRepository.save(source.toGroup());
        return new GroupOfTasksReadModel(result);
    }

    public List<GroupOfTasksReadModel> readAll() {
        return taskGroupRepository.findAll()
                .stream()
                .map(GroupOfTasksReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("W grupie są nieskończone zadania.");
        }
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono grupy o podanym IP"));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }
}
