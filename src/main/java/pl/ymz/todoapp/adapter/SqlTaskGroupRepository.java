package pl.ymz.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.model.TaskGroupRepository;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {
}
