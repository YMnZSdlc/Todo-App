package pl.ymz.todoapp.rpository;

import pl.ymz.todoapp.model.TaskGroup;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity) throws NoSuchFieldException;

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
