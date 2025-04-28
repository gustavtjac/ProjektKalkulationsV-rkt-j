package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PMController {

    //Vi dependency injecter serviceklasser der skal benyttes i controlleren
    ProfileService profileService;

    public PMController(ProfileService profileService) {
        this.profileService = profileService;
    }

    //Denne metoder sender en client til login siden hvis de ikke er logget ind i forvejen
    @GetMapping("/")
    public String showLoginPage(HttpSession session, @RequestParam(required = false) String wrongLogin){
        if (session.getAttribute("profile")!=null){
            return "redirect:/dashboard";
        }
        return "loginpage";
    }



    //Denne postmapping håndterer når en bruger prøver at logge ind på hjemmesiden
    @PostMapping("/loginrequest")
    public String loginRequest(@RequestParam String username, @RequestParam String password, HttpSession session,RedirectAttributes redirectAttributes){
       Profile loggedInProfile = profileService.AuthenticateLogin(username,password);
       if(loggedInProfile!=null){
session.setAttribute("profile",loggedInProfile);
           return "redirect:/dashboard";
       }else {
           //Hvis man prøver at logge ind men skriver forkert tilføjer vi her en fejlmeddelelse
           redirectAttributes.addFlashAttribute("wrongLogin","Invalid Username or Password");
           return "redirect:/";
       }



    }




    @GetMapping("/{username}")
    public String showDashBoard(@PathVariable String username){
        return "dashboard";
    }



}
