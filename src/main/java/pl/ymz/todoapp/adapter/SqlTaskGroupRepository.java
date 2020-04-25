package pl.ymz.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.model.TaskGroupRepository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    //Zapytanie oparte na encjach, nazywamy to HQL (JPQL). Możemy se odpuścić "select *"
    @Override
    @Query("from TaskGroup join fetch TaskGroup.tasks")
    List<TaskGroup> findAll();
}
