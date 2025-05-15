package gruppe6.kea.projektkalkulationeksamensprojekt.Services;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SubtaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class SubtaskServiceTest {

    private SubtaskService subtaskService;
    private SubtaskRepository subtaskRepository;
    private TaskService taskService;
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
         subtaskRepository = mock(SubtaskRepository.class);
         taskService = mock(TaskService.class);
         profileService = mock(ProfileService.class);
         subtaskService = new SubtaskService(subtaskRepository,taskService,profileService);
    }

    @Test
    void findByID_shouldReturnSubtaskWithTaskSet() {

        // Arrange Laver Dummy Subtask og Task
        Subtask mockSubtask = new Subtask();
        mockSubtask.setId("sub-123");
        mockSubtask.setTaskId("task-456");

        Task mockTask = new Task();
        mockTask.setId("task-456");
        mockTask.setName("Test Task");

        when(subtaskRepository.findByID("sub-123")).thenReturn(mockSubtask);
        when(taskService.findByID("task-456")).thenReturn(mockTask);

        // Act
        Subtask result = subtaskService.findById("sub-123");

        // Assert
        assertNotNull(result);
        assertEquals("sub-123", result.getId());
        assertNotNull(result.getTask());
        assertEquals("task-456", result.getTask().getId());
        assertEquals("Test Task", result.getTask().getName());

        // Verify method calls
        verify(subtaskRepository, times(1)).findByID("sub-123");
        verify(taskService, times(1)).findByID("task-456");
    }
}


