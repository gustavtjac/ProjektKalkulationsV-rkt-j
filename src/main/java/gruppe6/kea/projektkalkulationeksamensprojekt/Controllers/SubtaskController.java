package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.SubtaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProjectService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SubtaskService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubtaskController {
    private final ProjectService projectService;
    private final SubtaskService subtaskService;
    private final TaskService taskService;
    private final ProfileService profileService;

    public SubtaskController(ProjectService projectService, SubtaskService subtaskService, TaskService taskService, ProfileService profileService) {
        this.projectService = projectService;
        this.subtaskService = subtaskService;
        this.taskService = taskService;
        this.profileService = profileService;
    }

    @GetMapping("/redirecttosubtask")
    public String redirectToSubtask(HttpSession session, @RequestParam String subtaskID) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        if (loggedInProfile == null) {
            return "redirect:/dashboard";
        }
        String projectID = projectService.findById(taskService.findByID(subtaskService.findByID(subtaskID).getTaskId()).getProjectID()).getId();
        String taskID = subtaskService.findById(subtaskID).getTaskId();


        return "redirect:/dashboard/" + projectID + "/" + taskID + "/" + subtaskID;
    }


    @GetMapping("/dashboard/{projectid}/{taskid}/{subtaskid}")
    public String showSubtask(@PathVariable String subtaskid, @PathVariable String taskid, @PathVariable String projectid, HttpSession session, Model model, @RequestParam(required = false) String msg) {

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

        Subtask subtask = subtaskService.findByID(subtaskid);

        if (!subtask.getTaskId().equals(taskid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subtask does not belong to the task.");
        }

        model.addAttribute("message", msg);
        model.addAttribute("profile", loggedInProfile);
        model.addAttribute("project", project);
        model.addAttribute("task", task);
        model.addAttribute("subtask", subtask);

        return "viewSubtask";
    }


    @GetMapping("/createnewsubtask")
    public String showCreateNewSubtask(HttpSession session, Model model, @RequestParam String taskID, @RequestParam(required = false) String msg) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(taskService.findByID(taskID).getProjectID(), loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not assigned to this project");
        } else {
            model.addAttribute("message", msg);
            model.addAttribute("emptySubtask", new SubtaskDTO());
            model.addAttribute("task", taskService.findByID(taskID));
            model.addAttribute("employees", profileService.getAllProfilesAssignedToProject(taskService.findByID(taskID).getProjectID()));
            return "showcreatenewsubtask";
        }
    }


    @GetMapping("/editsubtask")
    public String showEditSubtask(HttpSession session, Model model, @RequestParam(required = false) String subtaskID, @RequestParam(required = false) String message) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        } else {

            Subtask chosenSubtask = subtaskService.findById(subtaskID);
            model.addAttribute("message", message);
            model.addAttribute("subtaskDTO", new SubtaskDTO());
            model.addAttribute("allprofiles", profileService.getAllProfilesAssignedToProject(projectService.findById(taskService.findByID(chosenSubtask.getTaskId()).getProjectID()).getId()));
            model.addAttribute("profile", loggedInProfile);
            model.addAttribute("chosenSubtask", chosenSubtask);
            return "editsubtask";
        }
    }

    @PostMapping("/editsubtask")
    public String editSubtask(@ModelAttribute SubtaskDTO subtaskDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        } else {
            try {
                subtaskService.saveSubtask(subtaskDTO);
                redirectAttributes.addAttribute("message", "Subtask has been updated");
                return "redirect:/dashboard/" + projectService.findById(taskService.findByID(subtaskDTO.getTaskId()).getProjectID()).getId() + "/" + taskService.findByID(subtaskDTO.getTaskId()).getId();

            } catch (RuntimeException e) {
                redirectAttributes.addAttribute("message", "Subtask could not be updated");
                return "redirect:/editsubtask";
            }
        }

    }


    @PostMapping("/createnewsubtask")
    public String createNewSubtask(@ModelAttribute SubtaskDTO subtaskDTO, RedirectAttributes redirectAttributes, HttpSession session) {

        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        Task task = taskService.findByID(subtaskDTO.getTaskId());


        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(task.getProjectID(), loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to create new tasks");
        }
        Subtask subtask = subtaskService.createNewSubtask(subtaskDTO);
        redirectAttributes.addAttribute("msg", "Subtask created");
        return "redirect:/dashboard/" + task.getProjectID() + "/" + subtask.getTaskId();


    }


    @PostMapping("/deletesubtask")
    public String deleteSubtask(@RequestParam String subtaskID, HttpSession session, RedirectAttributes redirectAttributes) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        String taskID = subtaskService.findById(subtaskID).getTaskId();
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(taskService.findByID(subtaskService.findById(subtaskID).getTaskId()).getProjectID(), loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to delete subtasks");
        } else {
            try {
                Subtask deletedSubtask = subtaskService.deleteSubtask(subtaskID);

                redirectAttributes.addAttribute("msg", deletedSubtask.getName() + " has been deleted");
            } catch (RuntimeException e) {

                redirectAttributes.addAttribute("msg", e.getMessage());
            }

            return "redirect:/dashboard/" + taskService.findByID(taskID).getProjectID() + "/" + taskID;
        }
    }

}
