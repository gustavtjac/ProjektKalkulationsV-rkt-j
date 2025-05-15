package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.TaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.TaskRowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Repository
public class TaskRepository implements CrudMethods<Task, String> {
    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper taskRowMapper;

    public TaskRepository(JdbcTemplate jdbcTemplate, TaskRowMapper taskRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskRowMapper = taskRowMapper;
    }

    public Task createNewTask(Task task) {
        String taskid = UUID.randomUUID().toString();
        task.setId(taskid);
        String sql = "Insert into Task (TASK_ID,TASK_PROJECT_ID,TASK_NAME,TASK_DESC,TASK_MAX_TIME,TASK_MAX_PRICE) VALUES (?,?,?,?,?,?)";
        int rowAffectred = jdbcTemplate.update(sql, task.getId(), task.getProjectID(), task.getName(), task.getDescription(), task.getMaxTime(), task.getMaxPrice());

        if (rowAffectred == 0) {
            return null;
        } else {
            return task;
        }


    }

    public List<Task> getTaskFromProjectID(String projectID) {
        String sql = "select * from Task where TASK_PROJECT_ID = ?";
        return jdbcTemplate.query(sql, taskRowMapper, projectID);
    }

    @Override

    public List<Task> findAll() {
        return null;
    }

    @Override
    public Task findByID(String taskID) {
        String SQL = "SELECT * FROM Task WHERE TASK_ID = ?";
        try {
            return jdbcTemplate.queryForObject(SQL, taskRowMapper, taskID);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for task id: " + taskID, e);
        }
    }

    public void deleteTask(String taskID) {
        String deleteSQL = "DELETE FROM Task WHERE task_ID = ?";
        jdbcTemplate.update(deleteSQL, taskID);
    }

    @Override
    public Task save(Task object) {
        return null;
    }

    public Task save(TaskDTO object) {
        String sqlTask = "UPDATE Task SET TASK_NAME = ?, TASK_DESC = ?, TASK_MAX_TIME = ?, TASK_MAX_PRICE = ? WHERE TASK_ID = ?";
        jdbcTemplate.update(sqlTask, object.getName(), object.getDescription(), object.getMaxTime(), object.getMaxPrice(), object.getId());
        return findByID(object.getId());
    }
}
