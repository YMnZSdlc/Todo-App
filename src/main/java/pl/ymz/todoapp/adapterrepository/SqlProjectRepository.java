package pl.ymz.todoapp.adapterrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.ymz.todoapp.model.Project;
import pl.ymz.todoapp.rpository.ProjectRepository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    //Zapytanie oparte na encjach, nazywamy to HQL (JPQL). Możemy se odpuścić "select *"
    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();
}
