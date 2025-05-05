package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.SubtaskRowmapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SubtaskRepository implements CrudMethods<Subtask,String>{

    private final JdbcTemplate jdbcTemplate;

    public SubtaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public List<Subtask> getSubtasksFromTaskID(String taskID){
        String sql = "SELECT * FROM subtask WHERE subtask_task_ID = ?";

        return jdbcTemplate.query(sql,new SubtaskRowmapper(),taskID);
    }

    public List<Subtask> findAllSubtaskByTaskID(String id) {
        String sql = "SELECT * FROM Subtask WHERE SUBTASK_TASK_ID = ?";
        return jdbcTemplate.query(sql, new SubtaskRowmapper(),id);

    }

    @Override
    public List<Subtask> findAll() {
        return null;
    }

    @Override
    public Subtask findByID(String s) {
        return null;
    }

    @Override
    public Subtask save(Subtask object) {
        return null;
    }
}
