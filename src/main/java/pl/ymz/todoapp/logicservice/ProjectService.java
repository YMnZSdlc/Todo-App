package pl.ymz.todoapp.logicservice;

import org.springframework.stereotype.Service;
import pl.ymz.todoapp.TaskConfigurationProperties;
import pl.ymz.todoapp.model.Project;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.model.projectiondto.GroupOfTasksReadModel;
import pl.ymz.todoapp.rpository.ProjectRepository;
import pl.ymz.todoapp.rpository.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    ProjectService(final ProjectRepository projectRepository,
                   final TaskGroupRepository taskGroupRepository,
                   final TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }
    
    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    Project create(final Project entity) {
        return projectRepository.save(entity);
    }

    public GroupOfTasksReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Tylko jedna nieskończona grupa z projektu jest dozwolona.");
        }

        TaskGroup targetGroup = projectRepository.findById(projectId)
                .map(project -> {
                    TaskGroup result = new TaskGroup();
                    result.setDescription(project.getDescription());
                    result.setTasks(project.getSteps()
                            .stream()
                            .map(step -> new Task(step.getDescription(), deadline.plusDays(step.getDaysToDeadline())))
                            .collect(Collectors.toSet())
                    );
                    return result;
                }).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono projektu z podanym id."));
        return new GroupOfTasksReadModel(targetGroup);
    }
}
