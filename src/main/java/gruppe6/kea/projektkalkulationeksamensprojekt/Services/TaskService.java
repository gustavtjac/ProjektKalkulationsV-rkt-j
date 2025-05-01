package gruppe6.kea.projektkalkulationeksamensprojekt.Services;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {


    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createNewTask(Task task){
        return taskRepository.createNewTask(task);
    }

    public Task findByID(String taskID){
        return taskRepository.findByID(taskID);
    }

}
