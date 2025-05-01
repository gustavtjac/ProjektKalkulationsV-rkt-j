package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.TaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository implements CrudMethods<Task,String>{
    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper taskRowMapper;

    public TaskRepository(JdbcTemplate jdbcTemplate, TaskRowMapper taskRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskRowMapper = taskRowMapper;
    }


    public List<Task> getTaskFromProjectID(String projectID){
        String sql = "select * from Task where TASK_PROJECT_ID = ?";
        return jdbcTemplate.query(sql,taskRowMapper,projectID);
    }


    @Override
    public List<Task> findAll() {
        return null;
    }

    @Override
    public Task findByID(String s) {
        return null;
    }

    @Override
    public Task save(Task object) {
        return null;
    }
}
