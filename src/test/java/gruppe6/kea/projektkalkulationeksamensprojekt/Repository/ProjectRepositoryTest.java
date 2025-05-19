package gruppe6.kea.projektkalkulationeksamensprojekt.Repository;



import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
@SpringBootTest
public class ProjectRepositoryTest {

@Autowired
   private ProjectRepository  projectRepository;
@Test
public void projectFindByIdAndCreateNewProjectTest(){
    //arrange
    ProjectDTO newProject = new ProjectDTO();
    newProject.setName("testproject");
    newProject.setProjectMembers(new ArrayList<>());
    //admin er en user der bliver oprettet sammen med databasen
    newProject.setProjectOwner("admin");
    newProject.setDescription("dette er et projekt");
    newProject.setEndDate(new Date());
    newProject.setMaxPrice(10000);
    newProject.setMaxTime(10000);
   Project craftedProject = projectRepository.createNewProject(newProject);
    //Act
    Project foundProject = projectRepository.findByID(craftedProject.getId());
    //Assert
    assertNotNull(foundProject);
    Assertions.assertEquals("testproject", foundProject.getName());
    Assertions.assertEquals(craftedProject.getId(), foundProject.getId());
    Assertions.assertEquals("admin", foundProject.getProjectOwner().getUsername());
    Assertions.assertEquals(10000, foundProject.getMaxTime());
    Assertions.assertEquals(10000, foundProject.getMaxPrice());
}
    @Test
    public void getAllProjectsFromProfile_returnsOwnerProjects() {
        // Arrange
        Profile owner = new Profile();
        owner.setUsername("admin");
        owner.setAuthCode(1); // Owner
        ProjectDTO newProject = new ProjectDTO();
        newProject.setName("OwnerProject");
        newProject.setProjectMembers(new ArrayList<>());
        newProject.setProjectOwner("admin");
        newProject.setDescription("Owned by admin");
        newProject.setEndDate(new Date());
        newProject.setMaxPrice(5000);
        newProject.setMaxTime(50);

        projectRepository.createNewProject(newProject);

        // Act
        var projects = projectRepository.getAllProjectsFromProfile(owner);
        // Assert
        Assertions.assertFalse(projects.isEmpty());
        Assertions.assertEquals("admin", projects.get(0).getProjectOwner().getUsername());
    }

    @Test
    public void save_updatesProjectDetails() {
        // Arrange
        ProjectDTO project = new ProjectDTO();
        project.setName("BeforeUpdate");
        project.setProjectMembers(new ArrayList<>());
        project.setProjectOwner("admin");
        project.setDescription("Before");
        project.setEndDate(new Date());
        project.setMaxPrice(2000);
        project.setMaxTime(20);

        Project created = projectRepository.createNewProject(project);

        project.setId(created.getId());
        project.setName("AfterUpdate");
        project.setDescription("Updated description");
        project.setMaxTime(100);
        project.setMaxPrice(1000);

        // Act
        Project updated = projectRepository.save(project);

        // Assert
        Assertions.assertEquals("AfterUpdate", updated.getName());
        Assertions.assertEquals("Updated description", updated.getDescription());
        Assertions.assertEquals(100, updated.getMaxTime());
        Assertions.assertEquals(1000, updated.getMaxPrice());
    }


    @Test
    public void deleteProject_removesProjectFromDatabase() {
        // Arrange
        ProjectDTO project = new ProjectDTO();
        project.setName("DeleteMe");
        project.setProjectMembers(new ArrayList<>());
        project.setProjectOwner("admin");
        project.setDescription("To be deleted");
        project.setEndDate(new Date());
        project.setMaxPrice(100);
        project.setMaxTime(10);

        Project created = projectRepository.createNewProject(project);

        // Act
        projectRepository.deleteProject(created.getId());

        // Assert
        Assertions.assertThrows(Exception.class, () -> {
            projectRepository.findByID(created.getId());
        });
    }






}
