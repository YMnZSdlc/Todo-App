package pl.ymz.todoapp.rpository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.ymz.todoapp.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    Task save(Task entity);

    List<Task> findByDone(boolean done);
}
