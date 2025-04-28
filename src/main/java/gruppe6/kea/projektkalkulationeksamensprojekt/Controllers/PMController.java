package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PMController {



    @GetMapping("/")
    public String showLoginPage(){

        return "loginPage";
    }


}
