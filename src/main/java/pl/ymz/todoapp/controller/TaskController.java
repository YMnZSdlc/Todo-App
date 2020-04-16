package pl.ymz.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import pl.ymz.todoapp.model.TaskRepository;

@RepositoryRestController
class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    TaskController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    ResponseEntity<?> readAllTasks() {
        logger.warn("Uważaj na wszystkie zadania");
        return ResponseEntity.ok(taskRepository.findAll());
    }
}
