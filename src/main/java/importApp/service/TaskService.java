package importApp.service;

import importApp.entity.TaskEntity;
import importApp.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    public String createTask(TaskEntity task) {
        taskMapper.save(task);
        return "success";
    }
}