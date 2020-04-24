package pl.ymz.todoapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Opis zadania wymagany")
    private String description;
    private Boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();

    public void updateFrom(final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
    }
}
