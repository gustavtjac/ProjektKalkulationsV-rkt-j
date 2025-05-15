package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProjectService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SubtaskService;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    private ProfileService profileService;
    private ProjectService projectService;
    private ProjectController projectController;
    private HttpSession session;
    private Model model;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        projectService = mock(ProjectService.class);
        SubtaskService subtaskService = mock(SubtaskService.class);
        session = mock(HttpSession.class);
        model = mock(Model.class);
        redirectAttributes = mock(RedirectAttributes.class);
        projectController = new ProjectController(profileService, projectService, subtaskService);
    }

    // -----------------------------
    // GET /dashboard
    // -----------------------------

    @Test
    void testDashboardRedirectsWhenNotLoggedIn() {
        //ARRANGE
        when(session.getAttribute("profile")).thenReturn(null);
        //ACT
        String viewName = projectController.showDashBoard(session, model);
        //ASSERT
        assertEquals("redirect:/", viewName);
    }

    @Test
    void testDashboardRedirectsWhenLoggedIn() {
        //ARRANGE
        Profile testUser = new Profile();
        when(session.getAttribute("profile")).thenReturn(testUser);
        //ACT
        String viewName = projectController.showDashBoard(session, model);
        //ASSERT
        assertEquals("dashboard", viewName);
    }

    // -----------------------------
    // GET /showcreatenewproject
    // -----------------------------

    @Test
    void testShowCreateProjectRedirectsIfUnauthorized() {
        //ARRANGE
        Profile profile = new Profile();
        profile.setAuthCode(0); // Ikke en projektleder
        when(session.getAttribute("profile")).thenReturn(profile);
        //ACT
        String view = projectController.showCreateProject(session, model);
        assertEquals("redirect:/dashboard", view);
        //ASSERT
    }

    @Test
    void testShowCreateProjectReturnsViewForProjectLeader() {
        //ARRANGE
        Profile profile = new Profile();
        profile.setAuthCode(1);
        when(session.getAttribute("profile")).thenReturn(profile);
        when(profileService.findAllProfiles()).thenReturn(new ArrayList<>());
        //ACT
        String view = projectController.showCreateProject(session, model);
        //ASSERT
        assertEquals("showcreatenewproject", view);
    }

    // -----------------------------
    // GET /dashboard/{projectid}
    // -----------------------------

    @Test
    void testShowProjectRedirectsIfNotLoggedIn() {
        //ARRANGE
        when(session.getAttribute("profile")).thenReturn(null);
        //ACT
        String view = projectController.showProject("p1", session, model);
        //ASSERT
        assertEquals("redirect:/", view);
    }

    @Test
    void testShowProjectRedirectsIfNotAssigned() {
        //ARRANGE
        Profile profile = new Profile();
        Project project = new Project();
        project.setTasks(new ArrayList<>());
        when(session.getAttribute("profile")).thenReturn(profile);
        when(projectService.findById("p1")).thenReturn(project);
        when(projectService.checkIfProfileIsAssignedProject(profile, project)).thenReturn(false);
        //ACT
        String view = projectController.showProject("p1", session, model);
        //ASSERT
        assertEquals("redirect:/", view);
    }

    // -----------------------------
    // GET /editproject
    // -----------------------------

    @Test
    void testEditProjectRedirectsWhenUnauthorized() {
        //ARRANGE
        Profile profile = new Profile();
        profile.setAuthCode(2); // Lowest permission level
        when(session.getAttribute("profile")).thenReturn(profile);
        //ACT
        String viewName = projectController.showEditProject("p1", session, model);
        //ASSERT
        assertEquals("redirect:/", viewName);
    }

    @Test
    void testEditProjectReturnsEditViewForAuthorizedUser() {
        //ARRANGE
        Profile profile = new Profile();
        profile.setAuthCode(1);
        when(session.getAttribute("profile")).thenReturn(profile);
        when(profileService.findAllProfiles()).thenReturn(new ArrayList<>());
        when(projectService.findById("p1")).thenReturn(new Project());
        //ACT
        String viewName = projectController.showEditProject("p1", session, model);
        //ASSERT
        assertEquals("editProject", viewName);
    }

    // -----------------------------
    // POST /createnewproject
    // -----------------------------

    @Test
    void testCreateNewProjectRedirectsOnSuccess() {
        //ARRANGE
        ProjectDTO dto = new ProjectDTO();
        Project project = new Project();
        when(projectService.createNewProject(dto)).thenReturn(project);
        //ACT
        String view = projectController.createNewProject(dto, redirectAttributes);
        //ASSERT
        assertEquals("redirect:/dashboard", view);
    }

    @Test
    void testCreateNewProjectRedirectsOnFailure() {
        //ARRANGE
        ProjectDTO dto = new ProjectDTO();
        when(projectService.createNewProject(dto)).thenReturn(null);
        //ACT
        String view = projectController.createNewProject(dto, redirectAttributes);
        //ASSERT
        assertEquals("redirect:/showcreatenewproject", view);
    }

    // -----------------------------
    // POST /deleteproject
    // -----------------------------

    @Test
    void testDeleteProjectWhenUserOwnsIt() {
        //ARRANGE
        when(projectService.checkIfProfileOwnsProject("p1", "user")).thenReturn(true);
        //ACT
        String result = projectController.deleteProject("p1", "user");
        //ASSERT
        assertEquals("redirect:/dashboard", result);
        verify(projectService, times(1)).deleteProject("p1");
    }

    @Test
    void testDeleteProjectWhenUserDoesNotOwnIt() {
        //ARRANGE
        when(projectService.checkIfProfileOwnsProject("p1", "user")).thenReturn(false);
        //ACT
        String result = projectController.deleteProject("p1", "user");
        //ASSERT
        assertEquals("redirect:/", result);
        verify(projectService, never()).deleteProject(any());
    }
}
