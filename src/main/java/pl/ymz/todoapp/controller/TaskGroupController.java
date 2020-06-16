package pl.ymz.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.ymz.todoapp.logicservice.TaskGroupService;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.TaskRepository;
import pl.ymz.todoapp.model.projectiondto.GroupReadModel;
import pl.ymz.todoapp.model.projectiondto.GroupTaskWriteModel;
import pl.ymz.todoapp.model.projectiondto.GroupWriteModel;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    TaskGroupController(final TaskGroupService taskGroupService, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskGroupService = taskGroupService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute("group") @Valid GroupWriteModel current,
                    BindingResult bindingResult,
                    Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Błąd walidacji dodawanych kroków");
            return "groups";
        }
        taskGroupService.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę");
        logger.info("Dodanie projektu i potwierdzenia");
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("task") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        logger.info("Dodanie zadania do grupy zadań");
        return "groups";
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel groupToCreate) {
        logger.info("Próba utworzenia grupy zadań");
        GroupReadModel taskGroup = taskGroupService.createGroup(groupToCreate);
        return ResponseEntity.created(URI.create("/" + taskGroup.getId())).body(taskGroup);
//        return ResponseEntity.created(URI.create("/")).body(taskGroupService.createGroup(groupToCreate));
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Odczyt wszystkich grup zadań");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        logger.info("Kontroler z parametrami stron");
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        logger.info("Pobranie listy z grupami zadań");
        return taskGroupService.readAll();
    }
}
