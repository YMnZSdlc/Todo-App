package pl.ymz.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Opis grupy zadań wymagany")
    private String description;
    private boolean done;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @Embedded
    private Audit audit = new Audit();

    public TaskGroup() {
    }

    public void updateFrom(final TaskGroup source) {
        description = source.description;
        done = source.done;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    Project getProject() {
        return project;
    }

    void setProject(Project project) {
        this.project = project;
    }
}
