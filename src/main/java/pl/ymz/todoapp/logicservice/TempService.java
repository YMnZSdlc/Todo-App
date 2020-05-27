package pl.ymz.todoapp.logicservice;

import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.model.TaskGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

// Wyłączony serwis. Służył tylko do pokazania problemu N+1
//@Service
public class TempService {

    //    @Autowired
    List<String> temp(TaskGroupRepository repository) {
        // FIXME: n + 1
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getDescription)
                .collect(Collectors.toList());
    }
}
