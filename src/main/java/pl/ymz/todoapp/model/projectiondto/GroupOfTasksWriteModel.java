package pl.ymz.todoapp.model.projectiondto;

import pl.ymz.todoapp.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupOfTasksWriteModel {

    private String description;
    private Set<TaskInGroupWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TaskInGroupWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskInGroupWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup() {
        var result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                .map(TaskInGroupWriteModel::toTask)
                .collect(Collectors.toSet()));
        return result;
    }
}
