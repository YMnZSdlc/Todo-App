package pl.ymz.todoapp.logicservice;

import org.springframework.stereotype.Service;
import pl.ymz.todoapp.TaskConfigurationProperties;
import pl.ymz.todoapp.model.Project;
import pl.ymz.todoapp.model.projectiondto.GroupReadModel;
import pl.ymz.todoapp.model.projectiondto.GroupTaskWriteModel;
import pl.ymz.todoapp.model.projectiondto.GroupWriteModel;
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
    private TaskGroupService service;

    ProjectService(final ProjectRepository projectRepository,
                   final TaskGroupRepository taskGroupRepository,
                   final TaskConfigurationProperties config,
                   final TaskGroupService service) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
        this.service = service;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    Project create(final Project entity) {
        return projectRepository.save(entity);
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Tylko jedna nieskończona grupa z projektu jest dozwolona.");
        }

        GroupReadModel result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(step -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(step.getDescription());
                                                task.setDeadline(deadline.plusDays(step.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toSet())
                    );
                    return service.createGroup(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono projektu z podanym id."));
        return result;
    }
}
