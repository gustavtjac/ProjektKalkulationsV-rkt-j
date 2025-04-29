package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PMController {

    //Vi dependency injecter serviceklasser der skal benyttes i controlleren
    private final ProfileService profileService;
    private final ProjectService projectService;

    public PMController(ProfileService profileService, ProjectService projectService) {
        this.profileService = profileService;
        this.projectService = projectService;
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



}
