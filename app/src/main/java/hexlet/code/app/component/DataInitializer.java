package hexlet.code.app.component;

import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.task_status.TaskStatusCreateDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    private final TaskStatusMapper taskStatusMapper;

    @Autowired
    private final LabelRepository labelRepository;

    @Autowired
    private final LabelMapper labelMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addFirstUser();
        addDefaultTaskStatuses();
        addDefaultLabels();
    }

    private void addFirstUser() {
        var email = "hexlet@example.com";
        var password = "qwerty";

        if (userRepository.findByEmail(email).isEmpty()) {
            var userData = new User();
            userData.setEmail(email);
            userData.setEncryptedPassword(password);
            userService.createUser(userData);
        }
    }

    private void addDefaultTaskStatuses() {
        var defaultStatuses = Map.of(
                "draft", "Draft",
                "to_review", "To review",
                "to_be_fixed", "To be fixed",
                "to_publish", "To publish",
                "published", "Published"
        );
        for (var entry : defaultStatuses.entrySet()) {
            if (taskStatusRepository.findBySlug(entry.getKey()).isEmpty()) {
                var taskStatusCreateDTO = new TaskStatusCreateDTO();
                taskStatusCreateDTO.setSlug(entry.getKey());
                taskStatusCreateDTO.setName(entry.getValue());
                var taskStatus = taskStatusMapper.map(taskStatusCreateDTO);
                taskStatusRepository.save(taskStatus);
            }
        }
    }

    private void addDefaultLabels() {
        var defaultLabels = List.of(
                "feature",
                "bug"
        );
        for (var labelName : defaultLabels) {
            if (labelRepository.findByName(labelName).isEmpty()) {
                var labelCreateDTO = new LabelCreateDTO();
                labelCreateDTO.setName(labelName);
                var label = labelMapper.map(labelCreateDTO);
                labelRepository.save(label);
            }
        }
    }
}
