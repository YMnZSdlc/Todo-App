package pl.ymz.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    TaskController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Kontroler z /tasks z wyłączeniem parametrów sort page i size");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Kontroler z parametrami stron");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTaskById(@PathVariable int id) {
        logger.info("Odczyt konkretnego zadania po ID:" + id);
        return taskRepository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable("id") int taskId, @RequestBody @Valid Task taskToUpdate) {
        if (!taskRepository.existsById(taskId)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(taskId)
                .ifPresent(task -> {
                    task.updateFrom(taskToUpdate);
                    taskRepository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task taskToCreate) {
        logger.info("Próba utworzenia zadania");
        Task task = taskRepository.save(taskToCreate);
        logger.info("Utworzono zadanie o nr ID:" + taskToCreate.getId());
        return ResponseEntity.created(URI.create("/" + task.getId()))
                .body(task);
    }
}
