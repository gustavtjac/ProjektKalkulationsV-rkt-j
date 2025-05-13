package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SkillService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    private ProfileService profileService;
    private SkillService skillService;
    private ProfileController profileController;
    private HttpSession session;
    private Model model;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        skillService = mock(SkillService.class);
        session = mock(HttpSession.class);
        model = mock(Model.class);
        redirectAttributes = mock(RedirectAttributes.class);
        profileController = new ProfileController(profileService, skillService);
    }

    // -----------------------------
    // GET /
    // -----------------------------

    @Test
    void testShowLoginPageRedirectsIfAlreadyLoggedIn() {
        // ARRANGE
        when(session.getAttribute("profile")).thenReturn(new Profile());

        // ACT
        String view = profileController.showLoginPage(session, null, model);

        // ASSERT
        assertEquals("redirect:/dashboard", view);
    }

    @Test
    void testShowLoginPageReturnsLoginViewIfNotLoggedIn() {
        // ARRANGE
        when(session.getAttribute("profile")).thenReturn(null);

        // ACT
        String view = profileController.showLoginPage(session, null, model);

        // ASSERT
        assertEquals("loginpage", view);
    }

    // -----------------------------
    // POST /loginrequest
    // -----------------------------

    @Test
    void testLoginSuccessRedirectsToDashboard() {
        // ARRANGE
        Profile profile = new Profile();
        when(profileService.AuthenticateLogin("test", "pass")).thenReturn(profile);

        // ACT
        String view = profileController.loginRequest("test", "pass", session, redirectAttributes);

        // ASSERT
        assertEquals("redirect:/dashboard", view);
        verify(session).setAttribute("profile", profile);
    }

    @Test
    void testLoginFailureRedirectsToLoginWithError() {
        // ARRANGE
        when(profileService.AuthenticateLogin("test", "fail")).thenReturn(null);

        // ACT
        String view = profileController.loginRequest("test", "fail", session, redirectAttributes);

        // ASSERT
        assertEquals("redirect:/", view);
        verify(redirectAttributes).addAttribute("wrongLogin", "Invalid Username or Password");
    }

    // -----------------------------
    // GET /logout
    // -----------------------------

    @Test
    void testLogoutRedirectsToLoginPage() {
        // ACT
        String view = profileController.logout(session);

        // ASSERT
        assertEquals("redirect:/", view);
        verify(session).invalidate();
    }

    // -----------------------------
    // GET /manageemployees
    // -----------------------------

    @Test
    void testShowManageEmployeesAsAdminReturnsView() {
        // ARRANGE
        Profile profile = new Profile();
        profile.setAuthCode(0);
        when(session.getAttribute("profile")).thenReturn(profile);
        when(profileService.findAllProfiles()).thenReturn(new ArrayList<>());

        // ACT
        String view = profileController.showManageEmployees(session, model, null);

        // ASSERT
        assertEquals("manageemployees", view);
    }

    @Test
    void testShowManageEmployeesUnauthorizedThrowsException() {
        // ARRANGE
        Profile profile = new Profile();
        profile.setAuthCode(1);
        when(session.getAttribute("profile")).thenReturn(profile);

        // ACT & ASSERT
        try {
            profileController.showManageEmployees(session, model, null);
        } catch (Exception e) {
            assertEquals("401 UNAUTHORIZED \"User is not allowed on this page\"", e.getMessage());
        }
    }

    // -----------------------------
    // Nytilføjede tests
    // -----------------------------


}
