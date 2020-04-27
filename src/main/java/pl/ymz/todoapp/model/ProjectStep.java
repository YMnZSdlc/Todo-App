package pl.ymz.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "project_steps")
public class ProjectStep {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Opis szablonu zadania wymagany")
    private String description;
    private int daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @Embedded
    private Audit audit = new Audit();

    public ProjectStep() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDaysToDeadline() {
        return daysToDeadline;
    }

    void setDaysToDeadline(int daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }

    Project getProject() {
        return project;
    }

    void setProject(Project project) {
        this.project = project;
    }
}
