package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.TaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProjectService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TaskController {

    private final ProjectService projectService;
    private final TaskService taskService;

    public TaskController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @GetMapping("/showcreatenewtask")
    public String showCreateTask(HttpSession session, Model model, @RequestParam String projectID) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(projectID, loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed on this page");
        } else {
            model.addAttribute("projectID", projectID);
            model.addAttribute("task", new Task());
            model.addAttribute("profile", loggedInProfile);
            return "showcreatenewtask";
        }

    }

    // Metode viser siden til alle tasks, sub-task
    @GetMapping("/dashboard/{projectid}/{taskid}")
    public String showTask(@PathVariable String taskid, @PathVariable String projectid, HttpSession session, Model model, @RequestParam(required = false) String msg) {

        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null) {
            return "redirect:/";
        }
        Project project = projectService.findById(projectid);
        if (loggedInProfile.getAuthCode() == 1) {
            if (!projectService.checkIfProfileOwnsProject(project.getId(), loggedInProfile.getUsername())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont own this project");
            }

        }
        if (loggedInProfile.getAuthCode() == 2) {
            if (!projectService.checkIfProfileIsAssignedProject(loggedInProfile, project)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not assigned to this project");
            }
        }
        Task task = taskService.findByID(taskid);
        List<Subtask> subtasks = task.getSubtasks();

        model.addAttribute("message", msg);
        model.addAttribute("profile", loggedInProfile);
        model.addAttribute("project", project);
        model.addAttribute("task", task);
        model.addAttribute("subtasks", subtasks);

        return "viewTask";
    }


    @GetMapping("/edittask")
    public String showEditTask(@RequestParam(required = false) String taskID, HttpSession session, Model model, @RequestParam(required = false) String message) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
        } else {
            Task chosenTask = taskService.findByID(taskID);
            model.addAttribute("message", message);
            model.addAttribute("taskDTO", new TaskDTO());
            model.addAttribute("chosenTask", chosenTask);
            return "edittask";
        }
    }


    @PostMapping("/edittask")
    public String editTask(@ModelAttribute TaskDTO taskDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        } else {
            try {
                Task editedTask = taskService.saveTask(taskDTO);
                redirectAttributes.addAttribute("message", "Task has been updated");
                return "redirect:/dashboard/" + taskDTO.getProjectID();
            } catch (RuntimeException e) {
                redirectAttributes.addAttribute("taskID", taskDTO.getId());
                redirectAttributes.addAttribute("message", "Task could not be updated");
                return "redirect:/edittask";
            }
        }
    }


    @PostMapping("/deletetask")
    public String deleteTask(@RequestParam String taskID, HttpSession session) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile")); //Henter en gemt profil instans fra sessionen så den kan bruges
        Task foundTask = taskService.findByID(taskID); //Finder taskID
        if (loggedInProfile == null || !projectService.checkIfProfileOwnsProject(foundTask.getProjectID(), loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed");
        } else {
            projectService.deleteTask(taskID);
            return "redirect:/dashboard/" + foundTask.getProjectID();
        }

    }


    @PostMapping("/createnewtask")
    public String createNewTask(@ModelAttribute Task task, RedirectAttributes redirectAttributes, HttpSession
            session) {
        Profile profile = ((Profile) session.getAttribute("profile"));

        if (projectService.checkIfProfileOwnsProject(task.getProjectID(), profile.getUsername())) {
            Task newtask = taskService.createNewTask(task);
            if (newtask == null) {
                redirectAttributes.addAttribute("error", "Something Went Wrong Try Again");
                return "redirect:/showcreatenewproject";
            }

            return "redirect:/dashboard/" + task.getProjectID();

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to create a task for this project");
        }

    }


}
