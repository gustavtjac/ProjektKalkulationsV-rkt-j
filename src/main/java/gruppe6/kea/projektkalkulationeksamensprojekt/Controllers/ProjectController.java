package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProjectService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SubtaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProjectController {

    private final ProfileService profileService;
    private final ProjectService projectService;
    private final SubtaskService subtaskService;

    public ProjectController(ProfileService profileService, ProjectService projectService, SubtaskService subtaskService) {
        this.profileService = profileService;
        this.projectService = projectService;
        this.subtaskService = subtaskService;
    }

    // Denne metode viser forskellige dashbaord ift. hvilken profile er logget ind
    @GetMapping("/dashboard")
    public String showDashBoard(HttpSession session, Model model, @ModelAttribute Subtask subtask) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null) {
            return "redirect:/";
        }
        if (loggedInProfile.getAuthCode() == 2) {

        }

        Map<String, Double> timeSpentMap = new HashMap<>();
        for (Project project : projectService.getAllProjectsFromProfile(loggedInProfile)) {
            double timeSpent = subtaskService.getTimeSpentOnSubtaskForProject(project.getId());
            timeSpentMap.put(project.getId(), timeSpent);
        }

        Map<String, Double> budgetSpentMap = new HashMap<>();
        for (Project project : projectService.getAllProjectsFromProfile(loggedInProfile)) {
            double budgetSpent = subtaskService.getTimeMoneySpentOnSubtasksForProject(project.getId());
            budgetSpentMap.put(project.getId(), budgetSpent);
        }

        Map<String, String> deadlineColorMap = new HashMap<>();
        for (Project p : projectService.getAllProjectsFromProfile(loggedInProfile)) {
            deadlineColorMap.put(p.getId(), projectService.getDeadlineClass(p.getEndDate()));
        }
        model.addAttribute("deadlineColorMap", deadlineColorMap);

        model.addAttribute("timeSpentMap", timeSpentMap);
        model.addAttribute("budgetSpentMap", budgetSpentMap);
        model.addAttribute("projects", projectService.getAllProjectsFromProfile(loggedInProfile));
        model.addAttribute("profile", loggedInProfile);
        model.addAttribute("subtasks", subtaskService.getAllSubtaskFromProfile(loggedInProfile));
        System.out.println(subtaskService.getAllSubtaskFromProfile(loggedInProfile));
        System.out.println(projectService.getAllProjectsFromProfile(loggedInProfile));
        return "dashboard";
    }


    // Denne metode viser siden til at oprette et nyt projekt
    @GetMapping("/showcreatenewproject")
    public String showCreateProject(HttpSession session, Model model) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1) {
            return "redirect:/dashboard";
        } else {
            List<Profile> allProfiles = profileService.findAllProfiles();
            model.addAttribute("project", new ProjectDTO());
            model.addAttribute("profile", loggedInProfile);
            model.addAttribute("allProfiles", allProfiles);
            return "showcreatenewproject";
        }

    }

    // Metode viser siden til alle projektets task

    @GetMapping("/dashboard/{projectid}")
    public String showProject(@PathVariable String projectid, HttpSession session, Model model) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null) {
            return "redirect:/";
        }


        Project project = projectService.findById(projectid);

        Map<String, Double> timeSpentMap = new HashMap<>();
        for (Task task : project.getTasks()) {
            double timeSpent = subtaskService.getTimeSpentOnSubtaskForTask(task.getId());
            timeSpentMap.put(task.getId(), timeSpent);
        }


        Map<String, Double> budgetSpentMap = new HashMap<>();
        for (Task task : project.getTasks()) {
            double budgetSpent = subtaskService.getTimeMoneySpentOnSubtasksForTask(task.getId());
            budgetSpentMap.put(task.getId(), budgetSpent);
        }


        model.addAttribute("budgetSpentMap", budgetSpentMap);
        model.addAttribute("timeSpentMap", timeSpentMap);
        model.addAttribute("project", project);
        model.addAttribute("profile", loggedInProfile);

        if (!projectService.checkIfProfileIsAssignedProject(loggedInProfile, project)) {
            return "redirect:/";
        }

        return "viewProject";
    }


    @GetMapping("/editproject")
    public String showEditProject(@RequestParam String projectid, HttpSession session, Model model) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() == 2) {
            return "redirect:/";
        }
        List<Profile> allProfiles = profileService.findAllProfiles();
        Project projectToBeEditied = projectService.findById(projectid);
        model.addAttribute("projectDTO", new ProjectDTO());
        model.addAttribute("projectEdit", projectToBeEditied);
        model.addAttribute("profile", loggedInProfile);
        model.addAttribute("allProfiles", allProfiles);

        return "editProject";
    }


    @PostMapping("/editproject")
    public String editProject(@ModelAttribute ProjectDTO projectDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        Profile loggedInProfile = (Profile) session.getAttribute("profile");
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(projectDTO.getId(), loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This user is not allowed to edit project");
        } else {

            try {
                projectService.save(projectDTO);
                redirectAttributes.addAttribute("msg", "Project has been updated");
                return "redirect:/dashboard";
            } catch (RuntimeException e) {
                redirectAttributes.addAttribute("msg", "Project could not be updated");
                return "redirect:/editProject";
            }
        }
    }


    // Denne metode håndtere oprettelsen af projektet og gemmer det i SQL
    @PostMapping("/createnewproject")
    public String createNewProject(@ModelAttribute ProjectDTO projectDTO, RedirectAttributes redirectAttributes) {

        Project newProject = projectService.createNewProject(projectDTO);
        if (newProject == null) {
            redirectAttributes.addAttribute("error", "Something Went Wrong Try Again");
            return "redirect:/showcreatenewproject";
        }

        return "redirect:/dashboard";
    }


    //Slet funktioner til projekter, tasks og subtasks
    @PostMapping("/deleteproject")
    public String deleteProject(@RequestParam String projectID, @RequestParam String username) {
        if (projectService.checkIfProfileOwnsProject(projectID, username)) { //Tjekker først om brugeren ejer projektet
            projectService.deleteProject(projectID); //Sletter projektet
            return "redirect:/dashboard"; //Redirecter tilbage til dashboardet
        }
        return "redirect:/";
    }

}
