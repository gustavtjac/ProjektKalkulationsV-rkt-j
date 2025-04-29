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
        String sql = "select * from subtask where subtask_task_ID = ?";
        return jdbcTemplate.query(sql,new SubtaskRowmapper(),taskID);
    }

    @Override
    public Subtask findAll() {
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
