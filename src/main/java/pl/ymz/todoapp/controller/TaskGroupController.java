package pl.ymz.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ymz.todoapp.logicservice.TaskGroupService;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.projectiondto.GroupReadModel;
import pl.ymz.todoapp.model.projectiondto.GroupWriteModel;
import pl.ymz.todoapp.rpository.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    TaskGroupController(final TaskGroupService taskGroupService, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskGroupService = taskGroupService;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel groupToCreate) {
        logger.info("Próba utworzenia grupy zadań");
//        GroupReadModel taskGroup = taskGroupService.createGroup(groupToCreate);
//        return ResponseEntity.created(URI.create("/")).body(taskGroup);
        return ResponseEntity.created(URI.create("/")).body(taskGroupService.createGroup(groupToCreate));
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Odczyt wszystkich grup zadań");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        logger.info("Kontroler z parametrami stron");
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }
}
