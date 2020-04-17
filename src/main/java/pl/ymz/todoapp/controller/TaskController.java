package pl.ymz.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.TaskRepository;

import java.util.List;

@RestController
class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    //    @RequestMapping(method = RequestMethod.GET, path = "/tasks")
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    //taka adnotacja zamiast tej wyżej jeśli i tak metoda GET
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Kontroler z /tasks z wyłączeniem parametrów sort page i size");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
        //taka adnotacja zamiast tej wyżej jeśli i tak metoda GET
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Kontroler z parametrami stron");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }
}
