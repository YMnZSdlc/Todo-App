package pl.ymz.todoapp.logicservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.model.TaskGroupRepository;
import pl.ymz.todoapp.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("Powinien rzucać wyjątek, kiedy nieukończone zadania.")
    void toggleGroup_undoneTasks_throwsIllegalStateException() {
        //given
        TaskRepository mockTaskRepository = getMockTaskRepository(true);
        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("nieskończone zadania.");
    }

    @Test
    @DisplayName("Powinien rzucać wyjątek, kiedy nie ma grupy.")
    void toggleGroup_wrongId_throwsIllegalArgumentException() {
        //given
        TaskRepository mockTaskRepository = getMockTaskRepository(false);
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("o podanym IP");
    }

    @Test
    @DisplayName("Powinien przełączyć status wykonania grupy.")
    void toggleGroup_workAsExpected() {
        //given
        TaskRepository mockTaskRepository = getMockTaskRepository(false);
        //and
        var mockTaskGroup = new TaskGroup();
        var beforeToggle = mockTaskGroup.isDone();
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(mockTaskGroup));
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(1);

        //then
        assertThat(mockTaskGroup.isDone()).isEqualTo(!beforeToggle);
    }
    
    private TaskRepository getMockTaskRepository(final boolean b) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(b);
        return mockTaskRepository;
    }
}