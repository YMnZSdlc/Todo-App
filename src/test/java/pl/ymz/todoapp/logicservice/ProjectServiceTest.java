package pl.ymz.todoapp.logicservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.ymz.todoapp.TaskConfigurationProperties;
import pl.ymz.todoapp.model.Project;
import pl.ymz.todoapp.model.ProjectStep;
import pl.ymz.todoapp.model.TaskGroup;
import pl.ymz.todoapp.model.projectiondto.GroupReadModel;
import pl.ymz.todoapp.rpository.ProjectRepository;
import pl.ymz.todoapp.rpository.TaskGroupRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        var toTest = new ProjectService(mockRepository, null, mockConfig, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nie znaleziono projektu z podanym id");
    }

    @Test
    @DisplayName("Powinien stworzyć nową grupę zadań z projektu")
    void createGroup_configOk_existingProjectCreateAndSavesNewGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("ipsum", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(project));
        //and
        InMemoryTaskGroupRepository inMemoryGroupRepository = inMemoryTaskGroupRepository();
        int countBeforeCall = inMemoryGroupRepository.count();
        //and
        TaskConfigurationProperties mockConfig = getMockConfigurationProperties(true);
        //system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepository, mockConfig, null);

        //when
        GroupReadModel result = toTest.createGroup(today, 1);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("description", "ipsum");
        assertThat(result.getDescription()).isEqualTo("ipsum");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("lorem"));

        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepository.count());
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("lorem");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
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
        return new ProjectService(null, mockTaskGroupRepository, mockConfig, null);
    }

    private InMemoryTaskGroupRepository inMemoryTaskGroupRepository() {
        return new InMemoryTaskGroupRepository();
    }

    private static class InMemoryTaskGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException();
                }
            }
            map.put(++index, entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }

    }
}