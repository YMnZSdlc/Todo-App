package pl.ymz.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ymz.todoapp.logicservice.ProjectService;
import pl.ymz.todoapp.model.ProjectStep;
import pl.ymz.todoapp.model.projectiondto.ProjectWriteModel;

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
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model){
        projectService.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano projekt");
        logger.info("Dodanie projektu i potwierdzenia");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep (@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        logger.info("Dodanie kroku w projekcie");
        return "projects";
    }


}
