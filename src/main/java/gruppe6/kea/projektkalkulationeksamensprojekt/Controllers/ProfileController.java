package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SkillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    //Vi dependency injecter serviceklasser der skal benyttes i controlleren
    private final ProfileService profileService;
    private final SkillService skillService;

    public ProfileController(ProfileService profileService, SkillService skillService) {
        this.profileService = profileService;
        this.skillService = skillService;
    }


    @GetMapping("/user/{username}")
    public String viewProfile(HttpSession session, Model model, @PathVariable String username) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have to be logged in to view profiles");
        }
        model.addAttribute("profile", profileService.findById(username));
        return "viewprofile";
    }


    //Denne metoder sender en client til login siden hvis de ikke er logget ind i forvejen
    @GetMapping("/")
    public String showLoginPage(HttpSession session, @RequestParam(required = false) String wrongLogin, Model model) {
        if (session.getAttribute("profile") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("wrongLogin", wrongLogin);
        return "loginpage";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/editemployee")
    public String showEditProfile(@RequestParam(required = false) String profileID, HttpSession session, Model model, @RequestParam(required = false) String msg) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
        } else {
            Profile profileToBeEditied = profileService.findById(profileID);
            model.addAttribute("message", msg);
            model.addAttribute("profileDTO", new ProfileDTO());
            model.addAttribute("profileToBeEdited", profileToBeEditied);
            model.addAttribute("skills", skillService.findAll());
            return "editprofile";
        }
    }


    @GetMapping("/manageemployees")
    public String showManageEmployees(HttpSession session, Model model, @RequestParam(required = false) String msg) {
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
    public String showCreateNewEmployee(HttpSession session, Model model, @RequestParam(required = false) String
            msg) {
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


    @GetMapping("/manageskills")
    public String showManageSkills(HttpSession session, Model model, @RequestParam(required = false) String msg) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
        } else {
            model.addAttribute("message", msg);
            model.addAttribute("skilllist", skillService.findAll());
            return "manageskills";
        }

    }

    @GetMapping("/editskill")
    public String showEditSkill(@RequestParam String skillID, HttpSession session, Model model, @RequestParam(required = false) String msg) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
        } else {
            Skill skillToBeEditied = skillService.findByID(skillID);
            model.addAttribute("message", msg);
            model.addAttribute("skillToBeEdited", skillToBeEditied);
            return "editskill";
        }
    }


    @GetMapping("/createnewskill")
    public String showCreateNewSkill(HttpSession session, Model model, @RequestParam(required = false) String msg) {
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
    public String CreateNewSkill(HttpSession session, @ModelAttribute Skill skill, RedirectAttributes
            redirectAttributes) {

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
    public String deleteSkill(@RequestParam String skillID, HttpSession session, RedirectAttributes redirectAttributes) {
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


    @PostMapping("/editskill")
    public String editSkill(@ModelAttribute Skill skill, HttpSession session, RedirectAttributes redirectAttributes) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed on this page");
        } else {
            try {
                Skill savedSkill = skillService.save(skill);
                redirectAttributes.addAttribute("msg", "Skill has been updated");
                return "redirect:/manageskills";
            } catch (RuntimeException e) {

                redirectAttributes.addAttribute("msg", "Skill could not be updated, name probably already exists");
                redirectAttributes.addAttribute("skillID", skill.getId());
                return "redirect:/editskill";
            }

        }
    }


    @PostMapping("/editemployee")
    public String editProfile(@ModelAttribute ProfileDTO profileDTO, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String oldusername) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to edit employees");
        } else {
            try {
                Profile editedProfile = profileService.saveDTO(profileDTO, oldusername);
                redirectAttributes.addAttribute("msg", "Profile has been updated");
                return "redirect:/manageemployees";
            } catch (RuntimeException e) {

                redirectAttributes.addAttribute("msg", "Employee could not be updated, username probably already exists");
                redirectAttributes.addAttribute("profileID", oldusername);
                return "redirect:/editemployee";
            }

        }
    }

    //Denne postmapping håndterer når en bruger prøver at logge ind på hjemmesiden
    @PostMapping("/loginrequest")
    public String loginRequest(@RequestParam String username, @RequestParam String password, HttpSession
            session, RedirectAttributes redirectAttributes) {
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

    @PostMapping("/createnewemployee")
    public String CreateNewEmployee(HttpSession session, @ModelAttribute ProfileDTO profileDTO, RedirectAttributes
            redirectAttributes) {

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
    public String deleteEmployee(@RequestParam String profileID, HttpSession session, RedirectAttributes
            redirectAttributes) {
        Profile loggedInProfile = ((Profile) session.getAttribute("profile"));
        if (loggedInProfile == null || loggedInProfile.getAuthCode() != 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not allowed to delete profiles");
        } else {
            Profile deletedProfile = profileService.deleteFromId(profileID);
            redirectAttributes.addAttribute("msg", "User with username " + deletedProfile.getUsername() + " has been deleted");
            return "redirect:/manageemployees";
        }

    }


}

