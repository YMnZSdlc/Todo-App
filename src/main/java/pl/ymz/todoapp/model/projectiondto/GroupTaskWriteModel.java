package pl.ymz.todoapp.model.projectiondto;

import org.springframework.format.annotation.DateTimeFormat;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.TaskGroup;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

//To jest w sumie taki DTO-data transfer object, do zapisu.
public class GroupTaskWriteModel {

    @NotBlank(message = "Opis zadania wymagany")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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

    public Task toTask(final TaskGroup group) {
        return new Task(description, deadline, group);
    }
}
