package pl.ymz.todoapp.model.projection;

import pl.ymz.todoapp.model.Task;

import java.time.LocalDateTime;

//To jest w sumie taki DTO-data transfer object, do zapisu.
public class GroupTaskWriteModel {

    private String description;
    private LocalDateTime deadline;

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

    public Task toTask() {
        return new Task(description, deadline);
    }
}
