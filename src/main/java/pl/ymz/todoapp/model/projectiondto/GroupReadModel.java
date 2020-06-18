package pl.ymz.todoapp.model.projectiondto;

import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

//GroupReadModel w kursie
public class GroupReadModel {

    private int id;
    private String description;
    /**
     * „deadline” z ostatniego zadania w grupie
     * deadline from the last task in group
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;
    private LocalDateTime createTime;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        createTime = source.getCreatedTime();
        source.getTasks()
                .stream()
                .map(Task::getDeadline)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks()
                .stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public void setId(final int id){
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
