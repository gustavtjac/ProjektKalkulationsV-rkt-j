package gruppe6.kea.projektkalkulationeksamensprojekt.Repository;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.SubtaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SubtaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
@SpringBootTest
public class SubtaskRepositoryTest {

    @Autowired
    private SubtaskRepository subtaskRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // "Arrange" I h2init findes der Profile, Project, Task og Subtask som jeg bruger til mine test

    @Test
    public void findAllSubtaskByTaskID() {
        // Arrange Se linje 29

        // Act
        List<Subtask> subtasks = subtaskRepository.findAllSubtaskByTaskID("task-123");

        // Assert
        assertNotNull(subtasks);
        assertEquals(1,subtasks.size()); // Tjekker om listen ikke er tom

        Subtask subtask = subtasks.get(0);
        assertEquals("test-id-123", subtask.getId());
        assertEquals("Test Subtask", subtask.getName());
        assertEquals(5.0, subtask.getTime());
        assertEquals(1, subtask.getStatus());
    }

    @Test
    public void createNewSubtask() {
        // Arrange
        SubtaskDTO dto = new SubtaskDTO();
        dto.setTaskId("task-123"); // Den skal eksistere i databasen
        dto.setName("New Subtask");
        dto.setDescription("This is a new Subtask for testing.");
        dto.setTime(3.5);
        dto.setAssignedProfiles(List.of("admin")); // Forudsætter at 'admin' eksisterer i h2init

        // Act
        Subtask newSubtask = subtaskRepository.createNewSubtask(dto);

        // Assert
        assertNotNull(newSubtask); // Tjek at subtask blev returneret
        assertNotNull(newSubtask.getId()); // Tjek at subtask for tildelt et UUID
        assertEquals(dto.getName(), newSubtask.getName());
        assertEquals(dto.getDescription(), newSubtask.getDescription());
        assertEquals(dto.getTime(), newSubtask.getTime());
        assertEquals(1, newSubtask.getStatus());
    }

    @Test
    public void findById() {
        // Arrange Se linje 29

        // Act
        Subtask subtask = subtaskRepository.findByID("test-id-123");

        // Assert
        assertNotNull(subtask);
        assertEquals("Test Subtask", subtask.getName());
        assertEquals(5, subtask.getTime());
        assertEquals(1, subtask.getStatus());
    }

    @Test
    public void deleteSubtask() {
        // Arrange Se linje 29

        // Act
        Subtask deleted = subtaskRepository.deleteSubtask("test-id-123");

        // Assert
        assertNotNull(deleted);
        assertEquals("test-id-123", deleted.getId());
        assertEquals("Test Subtask", deleted.getName());

        // Tjek om den er slættet
        // Tæller hvor mange rækker der har SUBTASK_ID = 'test-id-123'
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Subtask WHERE SUBTASK_ID = ?",
                Integer.class,
                "test-id-123"
        );
        // Tjekker at rækken er tom
        assertEquals(0, count);
    }

}
