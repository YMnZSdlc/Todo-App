package pl.ymz.todoapp.adapterrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.rpository.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    //Zapytanie oparte na encjach, nazywamy to HQL (JPQL). Możemy se odpuścić "select *"
    @Override
    @Query("select distinct g from TaskGroup g left join fetch g.tasks")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
