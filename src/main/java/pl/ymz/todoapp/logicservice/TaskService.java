package pl.ymz.todoapp.logicservice;

import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.rpository.TaskRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync() {
        logger.info("Obsługa asynchroniczna");
        return CompletableFuture.supplyAsync(taskRepository::findAll);
    }
}
