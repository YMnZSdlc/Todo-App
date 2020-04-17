package pl.ymz.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@RepositoryRestResource //(path = "todos", collectionResourceRel = "tudus" ) // adnotacje do zmian w url itp
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

//    @Override
//    @RestResource(exported = false) //blokuje użycie delete
//    void deleteById(Integer integer);
//
//    @Override
//    @RestResource(exported = false) //blokuje użycie delete
//    void delete(Task task);

//    @RestResource(path = "isdone", rel= "isdone")
//    List<Task> findByDoneIsTrue();
//
//    @RestResource(path = "done", rel = "done")
//    List<Task> findByDone(@Param("state") boolean done);
}
