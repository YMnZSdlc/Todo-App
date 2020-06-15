package pl.ymz.todoapp.logicservice;

import org.springframework.stereotype.Service;
import pl.ymz.todoapp.TaskConfigurationProperties;
import pl.ymz.todoapp.model.Project;
import pl.ymz.todoapp.model.ProjectRepository;
import pl.ymz.todoapp.model.TaskGroupRepository;
import pl.ymz.todoapp.model.projectiondto.GroupReadModel;
import pl.ymz.todoapp.model.projectiondto.GroupTaskWriteModel;
import pl.ymz.todoapp.model.projectiondto.GroupWriteModel;
import pl.ymz.todoapp.model.projectiondto.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService taskGroupService;
    private TaskConfigurationProperties config;

    ProjectService(final ProjectRepository projectRepository,
                   final TaskGroupRepository taskGroupRepository,
                   final TaskGroupService taskGroupService,
                   final TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.config = config;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

//    Project create(final Project entity) {
//        return projectRepository.save(entity);
//    }

    public Project save(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Tylko jedna nieskończona grupa z projektu jest dozwolona.");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toList())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono projektu z podanym id"));
    }
}
