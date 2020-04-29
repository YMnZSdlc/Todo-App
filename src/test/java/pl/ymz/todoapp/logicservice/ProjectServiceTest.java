package pl.ymz.todoapp.logicservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.rpository.TaskGroupRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {



    @Test
    @DisplayName("Powinien rzucać wyjątek IllegalStateException, kiedy konfiguracja pozwala na 1 grupę i nieukończona grupa istnieje." +
            "Should throw IllegalStateException when configurated to allow just 1 group and other undone group exist.")
    void createGroup_noMultipleGroupsConfigAndUndoneGroupExist_throwsIllegalStateException() {
        //given
        var mockGroupRepository = new TaskGroupRepository() {

            @Override
            public List<TaskGroup> findAll() {
                return null;
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.empty();
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                return null;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return false;
            }
        };

        //when

        //then

    }

//    @Test
//    void createGroup() {
//
//    }

}