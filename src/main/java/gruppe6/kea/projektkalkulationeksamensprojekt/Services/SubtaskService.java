package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.SubtaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SubtaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskService taskService;
    private final ProfileService profileService;

    public SubtaskService(SubtaskRepository subtaskRepository, TaskService taskService, ProfileService profileService) {
        this.subtaskRepository = subtaskRepository;
        this.taskService = taskService;
        this.profileService = profileService;
    }

    public Subtask findByID(String id) {
        Subtask subtask = subtaskRepository.findByID(id);
        subtask.setTask(taskService.findByID(subtask.getTaskId()));
        return subtask;
    }


    public List<Subtask> findAllSubtaskByProjectID(String projectid) {
        List<Task> taskList = taskService.getTaskFromProjectID(projectid);
        List<Subtask> subtaskList = new ArrayList<>();


        for (Task task : taskList) {
            List<Subtask> tempSubTasakList = findAllSubtaskByTaskID(task.getId());
            subtaskList.addAll(tempSubTasakList);
        }
        return subtaskList;
    }


    public double getTimeSpentOnSubtaskForProject(String projectID) {
        List<Subtask> subtaskList = findAllSubtaskByProjectID(projectID);
        double timeSpent = 0;
        for (Subtask subtask : subtaskList) {

            if (subtask.getStatus() <= 2) {
                continue;
            }
            timeSpent += subtask.getTime();
        }

        return timeSpent;
    }

    public double getTimeSpentOnSubtaskForTask(String taskID) {
        double timeSpent = 0;
        List<Subtask> subtaskList = findAllSubtaskByTaskID(taskID);

        for (Subtask subtask : subtaskList) {

            if (subtask.getStatus() <= 2) {
                continue;
            }
            timeSpent += subtask.getTime();
        }

        return timeSpent;

    }


    public double getTimeMoneySpentOnSubtasksForProject(String projectID) {
        double salarySpentOnSubtask = 0;
        List<Subtask> subtaskList = findAllSubtaskByProjectID(projectID);

        for (Subtask subtask : subtaskList) {
            double timeSpent = subtask.getTime();
            List<Profile> employeesOnSubtask = profileService.getAllprofilesAssginedToSubtask(subtask.getId());

            if (employeesOnSubtask.isEmpty()) {
                continue;
            }

            double totalSalary = 0;
            for (Profile profile : employeesOnSubtask) {
                totalSalary += profile.getSalary();
            }

            double avgSalary = totalSalary / employeesOnSubtask.size();
            double cost = (subtask.getStatus() <= 2) ? 0 : avgSalary * timeSpent;

            salarySpentOnSubtask += cost;
        }

        return salarySpentOnSubtask;
    }


    public double getTimeMoneySpentOnSubtasksForTask(String taskID) {
        double salarySpentOnSubtask = 0;
        List<Subtask> subtaskList = subtaskRepository.findAllSubtaskByTaskID(taskID);
        for (Subtask subtask : subtaskList) {
            List<Profile> assignedEmployees = subtask.getAssignedProfiles();
            double avgSalary = 0;
            if (subtask.getStatus() <= 2) {
                continue;
            }

            if (assignedEmployees.isEmpty()) {
                continue;
            }

            for (Profile employee : assignedEmployees) {
                avgSalary += employee.getSalary();
            }
            avgSalary = avgSalary / assignedEmployees.size();
            salarySpentOnSubtask += avgSalary * subtask.getTime();
        }

        return salarySpentOnSubtask;
    }

    public List<Subtask> getAllSubtaskFromProfile(Profile profile) {
        List<Subtask> subtaskList = subtaskRepository.findAllSubtaskFromProfile(profile);
        for (Subtask subtask : subtaskList) {
            subtask.setTask(taskService.findByID(subtask.getTaskId()));
        }
        return subtaskList;
    }

    public List<Subtask> findAllSubtaskByTaskID(String id) {
        List<Subtask> subtaskList = subtaskRepository.findAllSubtaskByTaskID(id);
        for (Subtask subtask : subtaskList) {
            subtask.setTask(taskService.findByID(subtask.getTaskId()));
        }
        return subtaskList;

    }

    public Subtask createNewSubtask(SubtaskDTO subtaskDTO) {
        Subtask subtask = subtaskRepository.createNewSubtask(subtaskDTO);
        subtask.setTask(taskService.findByID(subtask.getTaskId()));
        return subtask;

    }

    public Subtask findById(String id) {
        Subtask subtask = subtaskRepository.findByID(id);
        subtask.setTask(taskService.findByID(subtask.getTaskId()));
        return subtask;
    }

    public Subtask deleteSubtask(String id) {
        return subtaskRepository.deleteSubtask(id);
    }

    public Subtask saveSubtask(SubtaskDTO subtaskDTO) {
        return subtaskRepository.saveSubtask(subtaskDTO);
    }


}
