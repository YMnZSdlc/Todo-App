package pl.ymz.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.ymz.todoapp.model.Task;
import pl.ymz.todoapp.rpository.TaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TaskRepository taskRepository;

    @Test
    void httpGet_returnsAllTasks(){
        //given
        taskRepository.save(new Task("foo", LocalDateTime.now()));
        taskRepository.save(new Task("Bar", LocalDateTime.now()));

        //when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        //then
        assertThat(result).hasSize(2);
    }

}