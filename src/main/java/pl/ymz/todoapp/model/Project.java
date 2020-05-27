package pl.ymz.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Opis projektu grupy zadań wymagany")
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;
    @Embedded
    private Audit audit = new Audit();

    public Project() {
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

    void setDescription(final String description) {
        this.description = description;
    }

    Set<TaskGroup> getGroups() {
        return groups;
    }

    void setGroups(final Set<TaskGroup> taskGroups) {
        this.groups = taskGroups;
    }

    public Set<ProjectStep> getSteps() {
        return steps;
    }

    void setSteps(final Set<ProjectStep> projectSteps) {
        this.steps = projectSteps;
    }
}
