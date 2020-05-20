package pl.ymz.todoapp.model.projectiondto;

import pl.ymz.todoapp.model.Task;

//DTO-data transfer object do czytania
public class GroupTaskReadModel {

    private String description;
    private boolean done;

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
