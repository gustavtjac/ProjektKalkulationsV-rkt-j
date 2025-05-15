package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.TaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    public TaskService(TaskRepository taskRepository, ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
    }

    public Task createNewTask(Task task) {
        Task newTask = taskRepository.createNewTask(task);
        newTask.setProject(projectService.findById(newTask.getProjectID()));
        return newTask;
    }

    public Task findByID(String taskID) {
        Task task = taskRepository.findByID(taskID);
        task.setProject(projectService.findById(task.getProjectID()));
        return task;
    }

    public Task saveTask(TaskDTO taskDTO) {
        Task task = taskRepository.save(taskDTO);
        task.setProject(projectService.findById(task.getProjectID()));
        return task;
    }

    public void deleteTask(String taskID) {
        taskRepository.deleteTask(taskID);
    }

    public List<Task> getTaskFromProjectID(String projectID) {
        List<Task> taskList = taskRepository.getTaskFromProjectID(projectID);

        for (Task task : taskList) {
            task.setProject(projectService.findById(task.getProjectID()));
        }
        return taskList;
    }

}
