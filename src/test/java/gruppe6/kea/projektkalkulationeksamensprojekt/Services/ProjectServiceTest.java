package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    void testFindByIdReturnsProject() {
        // ARRANGE
        Project dummyProject = new Project();
        dummyProject.setId("123");

        //når vi kalder metoden findbyid med "123" i paramaterene vil den istedet retunere dummyProject
        when(projectRepository.findByID("123")).thenReturn(dummyProject);

        // ACT
        Project result = projectService.findById("123");

        // ASSERT
        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void testCreateNewProjectCallsRepository() {
        // ARRANGE
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectMembers(new ArrayList<>());
        Project expectedProject = new Project();
        when(projectRepository.createNewProject(dto)).thenReturn(expectedProject);

        // ACT
        Project result = projectService.createNewProject(dto);

        // ASSERT
        assertNotNull(result);
        verify(projectRepository, times(1)).createNewProject(dto);
    }
    //Tester metoden på en bruger der ejer projektet
    @Test
    void testCheckIfProfileOwnsProjectTrue() {
        // ARRANGE
        Project project = new Project();
        Profile owner = new Profile();
        owner.setUsername("admin");
        project.setProjectOwner(owner);
        when(projectRepository.findByID("project1")).thenReturn(project);

        // ACT
        boolean result = projectService.checkIfProfileOwnsProject("project1", "admin");

        // ASSERT
        assertTrue(result);
    }

    //Tester metoden på en bruger der ikke ejer projektet
    @Test
    void testCheckIfProfileOwnsProjectFalse() {
        // ARRANGE
        Project project = new Project();
        Profile owner = new Profile();
        owner.setUsername("notadmin");
        project.setProjectOwner(owner);
        when(projectRepository.findByID("project1")).thenReturn(project);

        // ACT
        boolean result = projectService.checkIfProfileOwnsProject("project1", "admin");

        // ASSERT
        assertFalse(result);
    }


    //tester om vores metode til dynamisk farve-visning på dashboard fungerer. vi sætter tiden til i går og den burde derfor blive rød
    @Test
    void testGetDeadlineClassRedText() {
        // ARRANGE
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); // i går

        // ACT
        String result = projectService.getDeadlineClass(calendar.getTime());

        // ASSERT
        assertEquals("red-text", result);
    }


    //Her burde den retunerer gul
    @Test
    void testGetDeadlineClassYellowText() {
        // ARRANGE
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 6); // 3 days ahead

        // ACT
        String result = projectService.getDeadlineClass(calendar.getTime());

        // ASSERT
        assertEquals("yellow-text", result);
    }
    //når den er empty har den bare standardfarve- det burde den have indtil der er under en uge tilbage
    @Test
    void testGetDeadlineClassEmpty() {
        // ARRANGE
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 10); // 10 days ahead

        // ACT
        String result = projectService.getDeadlineClass(calendar.getTime());

        // ASSERT
        assertEquals("", result);
    }

    @Test
    void testDeleteProjectCallsRepository() {
        // ARRANGE
        //intet setup her

        // ACT
        projectService.deleteProject("123");

        // ASSERT
        verify(projectRepository, times(1)).deleteProject("123");
    }
}
