package pl.ymz.todoapp.logicservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.ymz.todoapp.TaskConfigurationProperties;
import pl.ymz.todoapp.rpository.ProjectRepository;
import pl.ymz.todoapp.rpository.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {


    @Test
    @DisplayName("Powinien rzucać wyjątek IllegalStateException, kiedy konfiguracja pozwala na 1 grupę i nieukończona grupa istnieje. Sposób I.")
    void createGroup_noMultipleGroupsConfigAndUndoneGroupExist_throwsIllegalStateException_I() {
        //given
        //system under test
        ProjectService toTest = getMockProjectService();

        //when + then
        assertThatThrownBy(() ->                                    //metoda assertThatThrownBy czeka na jakikolwiek wyjątek.
                toTest.createGroup(LocalDateTime.now(), 1)  //tu właściwy test metody
        ).isInstanceOf(IllegalStateException.class);                // dopisanie isInstanceOf pozwala określić typ wyjątku.
    }

    @Test
    @DisplayName("Powinien rzucać wyjątek IllegalStateException, kiedy konfiguracja pozwala na 1 grupę i nieukończona grupa istnieje. Sposób II.")
    void createGroup_noMultipleGroupsConfigAndUndoneGroupExist_throwsIllegalStateException_II() {
        //given
        //system under test
        ProjectService toTest = getMockProjectService();

        //when + then
        //inna metoda sprawdzająca wyjątki.
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));
    }

    @Test
    @DisplayName("Powinien rzucać wyjątek IllegalStateException, kiedy konfiguracja pozwala na 1 grupę i nieukończona grupa istnieje. Sposób III.")
    void createGroup_noMultipleGroupsConfigAndUndoneGroupExist_throwsIllegalStateException_III() {
        //given
        //system under test
        ProjectService toTest = getMockProjectService();

        //when + then
        //jeszcze inna metoda
        assertThatIllegalStateException().
                isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));
    }

    @Test
    @DisplayName("Powinien rzucać wyjątek IllegalStateException, kiedy konfiguracja pozwala na 1 grupę i nieukończona grupa istnieje. Sposób IV.")
    void createGroup_noMultipleGroupsConfigAndUndoneGroupExist_throwsIllegalStateException_IV() {
        //given
        //system under test
        ProjectService toTest = getMockProjectService();

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("jedna nieskończona grupa");
    }

    @Test
    @DisplayName("Powinien rzucać wyjątek IllegalStateException, kiedy konfiguracja pozwala na 1 grupę i nieukończona grupa istnieje. Sposób IV.")
    void createGroup_configurationOkAndNoProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = getMockConfigurationProperties(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nie znaleziono projektu z podanym id");
    }

    private TaskConfigurationProperties getMockConfigurationProperties(final boolean allowMulti) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(allowMulti);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }


    private ProjectService getMockProjectService() {
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        TaskConfigurationProperties mockConfig = getMockConfigurationProperties(false);
        return new ProjectService(null, mockTaskGroupRepository, mockConfig);
    }
}