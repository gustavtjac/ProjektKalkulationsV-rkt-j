package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProjectRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProjectService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.http.HttpResponse;
import java.util.List;

@Controller
public class PMController {

    //Vi dependency injecter serviceklasser der skal benyttes i controlleren
    private final ProfileService profileService;
    private final ProjectService projectService;
    private final TaskService taskService;

    public PMController(ProfileService profileService, ProjectService projectService, TaskService taskService) {
        this.profileService = profileService;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    //Denne metoder sender en client til login siden hvis de ikke er logget ind i forvejen
    @GetMapping("/")
    public String showLoginPage(HttpSession session, @RequestParam(required = false) String wrongLogin, Model model){
        if (session.getAttribute("profile")!=null){
            return "redirect:/dashboard";
        }
        model.addAttribute("wrongLogin",wrongLogin);
        return "loginpage";
    }



    //Denne postmapping håndterer når en bruger prøver at logge ind på hjemmesiden
    @PostMapping("/loginrequest")
    public String loginRequest(@RequestParam String username, @RequestParam String password, HttpSession session,RedirectAttributes redirectAttributes){
       Profile loggedInProfile = profileService.AuthenticateLogin(username,password);
       if(loggedInProfile!=null){
session.setAttribute("profile",loggedInProfile);
session.setMaxInactiveInterval(1800);
           return "redirect:/dashboard";
       }else {
           //Hvis man prøver at logge ind men skriver forkert tilføjer vi her en fejlmeddelelse
           redirectAttributes.addAttribute("wrongLogin","Invalid Username or Password");
           return "redirect:/";
       }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }




    @GetMapping("/dashboard")
    public String showDashBoard(HttpSession session,Model model){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile==null){
            return "redirect:/";
        } if (loggedInProfile.getAuthCode()==2) {

        }
        model.addAttribute("projects",projectService.getAllProjectsFromProfile(loggedInProfile));
        model.addAttribute("profile",loggedInProfile);
        return "dashboard";
    }

    @GetMapping("/showcreatenewproject")
    public String showCreateProject(HttpSession session, Model model) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        if (loggedInProfile==null ||loggedInProfile.getAuthCode() != 1) {
            return "redirect:/dashboard";
        }
        else {
            List<Profile> allProfiles = profileService.findAllProfiles();
            model.addAttribute("project", new ProjectDTO());
            model.addAttribute("profile",loggedInProfile);
            model.addAttribute("allProfiles", allProfiles);
            return "showcreatenewproject";
        }

    }
    @GetMapping("/showcreatenewtask")
    public String showCreateTask(HttpSession session, Model model,@RequestParam String projectID) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        if (loggedInProfile==null ||loggedInProfile.getAuthCode() != 1) {
            return "redirect:/dashboard";
        }
        else {
            model.addAttribute("projectID",projectID);
            model.addAttribute("task", new Task());
            model.addAttribute("profile",loggedInProfile);
            return "showcreatenewtask";
        }

    }

    @PostMapping("/createnewtask")
    public String createNewTask(@ModelAttribute Task task, RedirectAttributes redirectAttributes, HttpSession session) {
        Profile profile = ((Profile) session.getAttribute("profile"));

        if (projectService.checkIfProfileOwnsProject(task.getProjectID(),profile.getUsername())){
            Task newtask = taskService.createNewTask(task);
            if (newtask == null) {
                redirectAttributes.addAttribute("error", "Something Went Wrong Try Again");
                return "redirect:/showcreatenewproject";
            }

            return "redirect:/dashboard/" + task.getProjectID();

        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to create a task for this project");
        }


    }





    @PostMapping("/createnewproject")
    public String createNewProject(@ModelAttribute ProjectDTO projectDTO, RedirectAttributes redirectAttributes) {

        ProjectDTO newProject = projectService.createNewProject(projectDTO);
        if (newProject == null) {
            redirectAttributes.addAttribute("error", "Something Went Wrong Try Again");
            return "redirect:/showcreatenewproject";
        }

        return "redirect:/dashboard";
    }


    @GetMapping("/dashboard/{projectid}")
    public String showProject(@PathVariable String projectid, HttpSession session,Model model){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile==null){
            return "redirect:/";
        }

        Project project = projectService.findById(projectid);
        model.addAttribute("project",project);
        model.addAttribute("profile",loggedInProfile);

        if(!projectService.checkIfProfileIsAssignedProject(loggedInProfile,project)){
            return "redirect:/";
        }

        return "viewProject";
    }

    //Slet funktioner til projekter, tasks og subtasks

    @PostMapping("/deleteproject")
    public String deleteProject(@RequestParam String projectID ,@RequestParam String username){
        if(projectService.checkIfProfileOwnsProject(projectID,username)){ //Tjekker først om brugeren ejer projektet
            projectService.deleteProject(projectID); //Sletter projektet
            return "redirect:/dashboard"; //Redirecter tilbage til dashboardet
        }
        return "redirect:/";
    }

    @PostMapping("/deletetask")
    public String deleteTask(@RequestParam String taskID, HttpSession session) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        Task foundTask = taskService.findByID(taskID); //Finder taskID
        if (loggedInProfile==null|| !projectService.checkIfProfileOwnsProject(foundTask.getProjectID(), loggedInProfile.getUsername()) ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed");
        }else {
            projectService.deleteTask(taskID);
            return "redirect:/dashboard/" + foundTask.getProjectID();
        }

    }



}
