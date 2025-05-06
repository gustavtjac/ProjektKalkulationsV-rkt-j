package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.TaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.SubtaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProjectRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProjectService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SubtaskService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SkillService;
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
    private final SubtaskService subtaskService;
    private final SkillService skillService;

    public PMController(ProfileService profileService, ProjectService projectService, TaskService taskService, SubtaskService subtaskService, SkillService skillService) {
        this.profileService = profileService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.subtaskService = subtaskService;
        this.skillService = skillService;
    }

        //Denne metoder sender en client til login siden hvis de ikke er logget ind i forvejen
        @GetMapping("/")
        public String showLoginPage (HttpSession session, @RequestParam(required = false) String wrongLogin, Model model)
        {
            if (session.getAttribute("profile") != null) {
                return "redirect:/dashboard";
            }
            model.addAttribute("wrongLogin", wrongLogin);
            return "loginpage";
        }


        //Denne postmapping håndterer når en bruger prøver at logge ind på hjemmesiden
        @PostMapping("/loginrequest")
        public String loginRequest (@RequestParam String username, @RequestParam String password, HttpSession
        session, RedirectAttributes redirectAttributes){
            Profile loggedInProfile = profileService.AuthenticateLogin(username, password);
            if (loggedInProfile != null) {
                session.setAttribute("profile", loggedInProfile);
                session.setMaxInactiveInterval(1800);
                return "redirect:/dashboard";
            } else {
                //Hvis man prøver at logge ind men skriver forkert tilføjer vi her en fejlmeddelelse
                redirectAttributes.addAttribute("wrongLogin", "Invalid Username or Password");
                return "redirect:/";
            }
        }

        @GetMapping("/logout")
        public String logout (HttpSession session){
            session.invalidate();
            return "redirect:/";
        }

        // Denne metode viser forskellige dashbaord ift. hvilken profile er logget ind
        @GetMapping("/dashboard")
        public String showDashBoard (HttpSession session, Model model){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null) {
                return "redirect:/";
            }
            if (loggedInProfile.getAuthCode() == 2) {

            }
            model.addAttribute("projects", projectService.getAllProjectsFromProfile(loggedInProfile));
            model.addAttribute("profile", loggedInProfile);
            return "dashboard";
        }

        // Denne metode viser siden til at oprette et nyt projekt
        @GetMapping("/showcreatenewproject")
        public String showCreateProject (HttpSession session, Model model){
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
        @GetMapping("/showcreatenewtask")
        public String showCreateTask (HttpSession session, Model model, @RequestParam String projectID){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(projectID,loggedInProfile.getUsername())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed on this page");
            } else {
                model.addAttribute("projectID", projectID);
                model.addAttribute("task", new Task());
                model.addAttribute("profile", loggedInProfile);
                return "showcreatenewtask";
            }

        }

        @PostMapping("/createnewtask")
        public String createNewTask (@ModelAttribute Task task, RedirectAttributes redirectAttributes, HttpSession
        session){
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


        // Denne metode håndtere oprettelsen af projektet og gemmer det i SQL
        @PostMapping("/createnewproject")
        public String createNewProject (@ModelAttribute ProjectDTO projectDTO, RedirectAttributes redirectAttributes){

            ProjectDTO newProject = projectService.createNewProject(projectDTO);
            if (newProject == null) {
                redirectAttributes.addAttribute("error", "Something Went Wrong Try Again");
                return "redirect:/showcreatenewproject";
            }

            return "redirect:/dashboard";
        }


        // Metode viser siden til alle projektets task

        @GetMapping("/dashboard/{projectid}")
        public String showProject (@PathVariable String projectid, HttpSession session, Model model){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null) {
                return "redirect:/";
            }

            Project project = projectService.findById(projectid);
            model.addAttribute("project", project);
            model.addAttribute("profile", loggedInProfile);

            if (!projectService.checkIfProfileIsAssignedProject(loggedInProfile, project)) {
                return "redirect:/";
            }

            return "viewProject";
        }


        // Metode viser siden til alle tasks, sub-task
        @GetMapping("/dashboard/{projectid}/{taskid}")
        public String showTask (@PathVariable String taskid, @PathVariable String projectid, HttpSession session, Model model, @RequestParam(required = false) String msg){

            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null) {
                return "redirect:/";
            }
            Project project = projectService.findById(projectid);
            if (!projectService.checkIfProfileIsAssignedProject(loggedInProfile, project) || !projectService.checkIfProfileOwnsProject(project.getId(),loggedInProfile.getUsername())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not assigned to this project");
            }
            Task task = taskService.findByID(taskid);
            List<Subtask> subtasks = task.getSubtasks();

            model.addAttribute("message",msg);
            model.addAttribute("profile", loggedInProfile);
            model.addAttribute("project", project);
            model.addAttribute("task", task);
            model.addAttribute("subtasks", subtasks);

            return "viewTask";
        }


        @GetMapping ("/createnewsubtask")
        public String showCreateNewSubtask(HttpSession session,  Model model, @RequestParam String taskID, @RequestParam(required = false) String msg){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(taskService.findByID(taskID).getProjectID(),loggedInProfile.getUsername())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not assigned to this project");
            } else {
                model.addAttribute("message",msg);
                model.addAttribute("emptySubtask",new SubtaskDTO());
                model.addAttribute("task",taskService.findByID(taskID));
                model.addAttribute("employees",profileService.getAllProfilesAssignedToProject(taskService.findByID(taskID).getProjectID()));
                return "showcreatenewsubtask";
            }
        }
    @PostMapping("/createnewsubtask")
    public String createNewSubtask (@ModelAttribute SubtaskDTO subtaskDTO, RedirectAttributes redirectAttributes, HttpSession session){

        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
       Task task = taskService.findByID(subtaskDTO.getTaskId());



        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(task.getProjectID(),loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not allowed to create new tasks");
        }
            Subtask subtask = subtaskService.createNewSubtask(subtaskDTO);
            redirectAttributes.addAttribute("msg","Subtask created");
            return "redirect:/dashboard/" + task.getProjectID() + "/" + subtask.getTaskId();


    }




        //Slet funktioner til projekter, tasks og subtasks
        @PostMapping("/deleteproject")
        public String deleteProject (@RequestParam String projectID, @RequestParam String username){
            if (projectService.checkIfProfileOwnsProject(projectID, username)) { //Tjekker først om brugeren ejer projektet
                projectService.deleteProject(projectID); //Sletter projektet
                return "redirect:/dashboard"; //Redirecter tilbage til dashboardet
            }
            return "redirect:/";
        }

        @PostMapping("/deletetask")
        public String deleteTask (@RequestParam String taskID, HttpSession session){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile")); //Henter en gemt profil instans fra sessionen så den kan bruges
            Task foundTask = taskService.findByID(taskID); //Finder taskID
            if (loggedInProfile == null || !projectService.checkIfProfileOwnsProject(foundTask.getProjectID(), loggedInProfile.getUsername())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed");
            } else {
                projectService.deleteTask(taskID);
                return "redirect:/dashboard/" + foundTask.getProjectID();
            }

        }

        @GetMapping("/manageemployees")
        public String showManageEmployees (HttpSession session, Model model, @RequestParam(required = false) String msg)
        {
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
            } else {
                model.addAttribute("message", msg);
                model.addAttribute("employeelist", profileService.findAllProfiles());
                return "manageemployees";
            }

        }

        @GetMapping("/createnewemployee")
        public String showCreateNewEmployee (HttpSession session, Model model, @RequestParam(required = false) String
        msg ){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
            } else {
                model.addAttribute("message", msg);
                model.addAttribute("emptyEmployee", new ProfileDTO());
                model.addAttribute("skills", skillService.findAll());
                return "showcreatenewemployee";

            }

        }

        @PostMapping("/createnewemployee")
        public String CreateNewEmployee (HttpSession session, @ModelAttribute ProfileDTO profileDTO, RedirectAttributes
        redirectAttributes){

            if (profileService.checkIfUsernameExists(profileDTO.getUsername())) {

                redirectAttributes.addAttribute("msg", "The username you chose already exists");
                return "redirect:/createnewemployee";
            }

            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to create new profiles");
            } else {


                profileService.createNewProfile(profileDTO);


                return "redirect:/manageemployees";
            }

        }
        @PostMapping("/deleteemployee")
        public String deleteEmployee (@RequestParam String profileID, HttpSession session, RedirectAttributes
        redirectAttributes){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to delete profiles");
            } else {
                Profile deletedProfile = profileService.deleteFromId(profileID);
                redirectAttributes.addAttribute("msg", "User with username " + deletedProfile.getUsername() + " has been deleted");
                return "redirect:/manageemployees";
            }

        }


        @GetMapping("/manageskills")
        public String showManageSkills (HttpSession session, Model model, @RequestParam(required = false) String msg){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
            } else {
                model.addAttribute("message", msg);
                model.addAttribute("skilllist", skillService.findAll());
                return "manageskills";
            }

        }


        @GetMapping("/createnewskill")
        public String showCreateNewSkill (HttpSession session, Model model, @RequestParam(required = false) String msg )
        {
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
            } else {
                model.addAttribute("message", msg);
                model.addAttribute("emptySkill", new Skill());
                return "showcreatenewskill";
            }

        }
        @PostMapping("/createnewskill")
        public String CreateNewSkill (HttpSession session, @ModelAttribute Skill skill, RedirectAttributes
        redirectAttributes){

            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to create new skills");
            } else {

                try {
                    skillService.createNewSkill(skill);
                } catch (RuntimeException e) {
                    redirectAttributes.addAttribute("msg", e.getMessage());
                    return "redirect:/createnewskill";
                }
                redirectAttributes.addAttribute("msg", "Skill created");
                return "redirect:/manageskills";
            }

        }

        @PostMapping("/deleteskill")
        public String deleteSkill (@RequestParam String skillID, HttpSession session, RedirectAttributes redirectAttributes){
            Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
            if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to delete profiles");
            } else {

                try {
                    Skill deletedSkill = skillService.deleteFromId(skillID);
                    redirectAttributes.addAttribute("msg", deletedSkill.getName() + " has been deleted");
                } catch (RuntimeException e) {
                    redirectAttributes.addAttribute("msg", e.getMessage());
                }
                return "redirect:/manageskills";
            }
        }
@GetMapping("/editskill")
    public String showEditSkill(@RequestParam String skillID, HttpSession session,Model model, @RequestParam(required = false) String msg){
    Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
    if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
    }else {
        Skill skillToBeEditied = skillService.findByID(skillID);
        model.addAttribute("message",msg);
        model.addAttribute("skillToBeEdited",skillToBeEditied);
        return "editskill";
    }
}

@PostMapping("/editskill")
    public String editSkill(@ModelAttribute Skill skill,HttpSession session,RedirectAttributes redirectAttributes){
    Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
    if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
    }else {
        try {
            Skill savedSkill = skillService.save(skill);
            redirectAttributes.addAttribute("msg","Skill has been updated");
            return "redirect:/manageskills";
        } catch (RuntimeException e) {

            redirectAttributes.addAttribute("msg","Skill could not be updated, name probably already exists");
            redirectAttributes.addAttribute("skillID",skill.getId());
return "redirect:/editskill";
        }

    }
}

    @GetMapping("/editemployee")
    public String showEditProfile(@RequestParam(required = false) String profileID, HttpSession session,Model model, @RequestParam(required = false) String msg){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
        }else {
            Profile profileToBeEditied = profileService.findById(profileID);
            model.addAttribute("message",msg);
            model.addAttribute("profileDTO",new ProfileDTO());
            model.addAttribute("profileToBeEdited",profileToBeEditied);
            model.addAttribute("skills",skillService.findAll());
            return "editprofile";
        }
    }


    @PostMapping("/editemployee")
    public String editProfile(@ModelAttribute ProfileDTO profileDTO,HttpSession session,RedirectAttributes redirectAttributes,@RequestParam String oldusername){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        }else {
            try {
                Profile editedProfile = profileService.saveDTO(profileDTO,oldusername);
                redirectAttributes.addAttribute("msg","Profile has been updated");
                return "redirect:/manageemployees";
            } catch (RuntimeException e) {

                redirectAttributes.addAttribute("msg","Employee could not be updated, username probably already exists");
                redirectAttributes.addAttribute("profileID",oldusername);
                return "redirect:/editemployee";
            }

        }
    }


    @GetMapping("/editproject")
    public String showEditProject(@RequestParam String projectid, HttpSession session, Model model){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile==null || loggedInProfile.getAuthCode()==2){
            return "redirect:/";
        }
        List<Profile> allProfiles = profileService.findAllProfiles();
        Project projectToBeEditied = projectService.findById(projectid);
        model.addAttribute("projectDTO",new ProjectDTO());
        model.addAttribute("projectEdit",projectToBeEditied);
        model.addAttribute("profile", loggedInProfile);
        model.addAttribute("allProfiles", allProfiles);

        return "editProject";
    }


    @PostMapping("/editproject")
    public String editProject(@ModelAttribute ProjectDTO projectDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        Profile loggedInProfile = (Profile) session.getAttribute("profile");
        if (loggedInProfile == null || loggedInProfile.getAuthCode() !=1 || !projectService.checkIfProfileOwnsProject(projectDTO.getId(),loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This user is not allowed to edit project");
        } else {

            try{
                projectService.save(projectDTO);
                redirectAttributes.addAttribute("msg","Project has been updated");
                return "redirect:/dashboard";
            } catch (RuntimeException e){
                redirectAttributes.addAttribute("msg","Project could not be updated");
                return "redirect:/editProject";
            }
        }
    }

    @GetMapping("/edittask")
    public String showEditTask(@RequestParam(required = false) String taskID, HttpSession session, Model model, @RequestParam(required = false) String message){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
        }else{
            Task chosenTask = taskService.findByID(taskID);
            model.addAttribute("message", message);
            model.addAttribute("taskDTO", new TaskDTO());
            model.addAttribute("chosenTask", chosenTask);
            return "edittask";
        }
    }

    @PostMapping("/edittask")
    public String editTask(@ModelAttribute TaskDTO taskDTO, HttpSession session, RedirectAttributes redirectAttributes){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        }else {
            try{
                Task editedTask = taskService.saveTask(taskDTO);
                redirectAttributes.addAttribute("message", "Task has been updated");
                return "redirect:/dashboard/"+taskDTO.getProjectID();
            } catch (RuntimeException e){
                redirectAttributes.addAttribute("taskID",taskDTO.getId());
                redirectAttributes.addAttribute("message", "Task could not be updated");
                return "redirect:/edittask";
            }
        }
    }


    @GetMapping("/editsubtask")
    public String showEditSubtask(HttpSession session, Model model, @RequestParam(required = false) String subtaskID, @RequestParam(required = false) String message){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        }else {

                Subtask chosenSubtask = subtaskService.findById(subtaskID);
                System.out.println("Get else problem");
                model.addAttribute("message", message);
                model.addAttribute("subtaskDTO", new SubtaskDTO());
                model.addAttribute("allprofiles", profileService.getAllProfilesAssignedToProject(projectService.findById(taskService.findByID(chosenSubtask.getTaskId()).getProjectID()).getId()));
                 model.addAttribute("profile", loggedInProfile);
                model.addAttribute("chosenSubtask", chosenSubtask);
                return "editsubtask";
        }
    }

    @PostMapping("/editsubtask")
    public String editSubtask(@ModelAttribute SubtaskDTO subtaskDTO, HttpSession session, RedirectAttributes redirectAttributes){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        }else {
            try{
                subtaskService.saveSubtask(subtaskDTO);
                System.out.println(subtaskDTO.getTaskId());
                redirectAttributes.addAttribute("message", "Subtask has been updated");
                System.out.println("Post try problem");

                return "redirect:/dashboard/" + projectService.findById(taskService.findByID(subtaskDTO.getTaskId()).getProjectID()).getId() + "/" + taskService.findByID(subtaskDTO.getTaskId()).getId() ;

            } catch (RuntimeException e){
                redirectAttributes.addAttribute("message", "Subtask could not be updated");
                e.printStackTrace();
                return "redirect:/editsubtask";
            }
        }

    }



    @PostMapping("/deletesubtask")
    public String deleteSubtask (@RequestParam String subtaskID, HttpSession session, RedirectAttributes redirectAttributes){
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));

        String taskID = subtaskService.findById(subtaskID).getTaskId();
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 1 || !projectService.checkIfProfileOwnsProject(taskService.findByID(subtaskService.findById(subtaskID).getTaskId()).getProjectID(),loggedInProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to delete subtasks");
        } else {
            try {
                Subtask deletedSubtask = subtaskService.deleteSubtask(subtaskID);

                redirectAttributes.addAttribute("msg", deletedSubtask.getName() + " has been deleted");
            } catch (RuntimeException e) {

                redirectAttributes.addAttribute("msg", e.getMessage());
            }

            return "redirect:/dashboard/"+taskService.findByID(taskID).getProjectID()+"/"+taskID;
        }
    }



    }

