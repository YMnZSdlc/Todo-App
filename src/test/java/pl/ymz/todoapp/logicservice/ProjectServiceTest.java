package pl.ymz.todoapp.logicservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.ymz.todoapp.TaskConfigurationProperties;
import pl.ymz.todoapp.rpository.TaskGroupRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {


    @Test
    @DisplayName("Powinien rzucać wyjątek IllegalStateException, kiedy konfiguracja pozwala na 1 grupę i nieukończona grupa istnieje.")
    void createGroup_noMultipleGroupsConfigAndUndoneGroupExist_throwsIllegalStateException() {
        //given
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(false);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        //system under test
        var toTest = new ProjectService(null,mockGroupRepository,mockConfig);

        //when
        toTest.createGroup(LocalDateTime.now(),0);

        //then
    }

//    @Test
//    void createGroup() {
//
//    }

}