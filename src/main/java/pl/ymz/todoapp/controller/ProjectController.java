package pl.ymz.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.ymz.todoapp.logicservice.ProjectService;
import pl.ymz.todoapp.model.Project;
import pl.ymz.todoapp.model.ProjectStep;
import pl.ymz.todoapp.model.projectiondto.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String showProjects(Model model) {
        var projectToEdit = new ProjectWriteModel();
        projectToEdit.setDescription("testowy opis");
        model.addAttribute("project", projectToEdit);
        logger.info("Odczyt Projektu");
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current,
                      BindingResult bindingResult,
                      Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Błąd walidacji dodawanych kroków");
            return "projects";
        }
        projectService.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt");
        logger.info("Dodanie projektu i potwierdzenia");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        logger.info("Dodanie kroku w projekcie");
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {
        try {
            projectService.createGroup(deadline, id);
            logger.info("Dodano grupę");
            model.addAttribute("message", "Dodano grupę");
        } catch (IllegalStateException | IllegalArgumentException e) {
            logger.info("Błąd podczas tworzenia grupy");
            model.addAttribute("message", "Błąd podczas tworzenia grupy");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        logger.info("Pobranie listy projektów");
        return projectService.readAll();
    }
}
