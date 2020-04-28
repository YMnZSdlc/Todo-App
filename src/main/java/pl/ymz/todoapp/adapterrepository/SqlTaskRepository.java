package pl.ymz.todoapp.adapterrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.rpository.TaskRepository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    //Przykład własnego zapytania SQL do bazy na nazwach kolumn z bazy
    @Override
    @Query(nativeQuery = true, value = "select count(*)>0 from TASKS where id=:id")
    boolean existsById(@Param("id") Integer id);

    //Typowe zapytanie Spring Data
    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

}
