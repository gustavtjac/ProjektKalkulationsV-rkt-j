package gruppe6.kea.projektkalkulationeksamensprojekt.Repository;



import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
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
@Transactional
@Rollback(true)
public class ProjectRepositoryTest {

@Autowired
   private ProjectRepository  projectRepository;
@Test
public void projectFindByIdTest(){
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




}
