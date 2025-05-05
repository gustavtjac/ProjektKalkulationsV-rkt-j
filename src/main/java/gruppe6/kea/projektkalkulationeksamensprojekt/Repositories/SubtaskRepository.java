package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.SubtaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.SubtaskRowmapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public class SubtaskRepository implements CrudMethods<Subtask,String>{

    private final JdbcTemplate jdbcTemplate;
    private final SubtaskRowmapper subtaskRowmapper;

    public SubtaskRepository(JdbcTemplate jdbcTemplate, SubtaskRowmapper subtaskRowmapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.subtaskRowmapper = subtaskRowmapper;
    }



    public List<Subtask> getSubtasksFromTaskID(String taskID){
        String sql = "select * from subtask where subtask_task_ID = ?";
        return jdbcTemplate.query(sql,subtaskRowmapper,taskID);
    }

    public List<Subtask> findAllSubtaskByTaskID(String id) {
        String sql = "SELECT * FROM Subtask WHERE SUBTASK_TASK_ID = ?";
        return jdbcTemplate.query(sql, subtaskRowmapper,id);

    }
 public Subtask createNewSubtask(SubtaskDTO subtaskDTO){
        subtaskDTO.setId(UUID.randomUUID().toString());
        subtaskDTO.setStatus(1);


        String sql = "insert into subtask (SUBTASK_ID, SUBTASK_TASK_ID,SUBTASK_NAME,SUBTASK_DESC,SUBTASK_TIME,SUBTASK_STATUS) values(?,?,?,?,?,?)";

        String insertProfiles = "insert into SUBTASK_PROFILE (SUBTASK_ID,PROFILE_USERNAME) values (?,?)";

        try{
            jdbcTemplate.update(sql,subtaskDTO.getId(),subtaskDTO.getTaskId(),subtaskDTO.getName(),subtaskDTO.getDescription(),subtaskDTO.getTime(),subtaskDTO.getStatus());


        } catch (DataAccessException e) {
            System.out.println("failer her 1");
            throw new RuntimeException("could not create subtask");

        }


        try {
            System.out.println(subtaskDTO.getAssignedProfiles());
            for (String username : subtaskDTO.getAssignedProfiles()){
                jdbcTemplate.update(insertProfiles,subtaskDTO.getId(),username);
            }
        } catch (DataAccessException e) {
            System.out.println("failer her 2");
            throw new RuntimeException("could not insert users");
        }



        return findByID(subtaskDTO.getId());
 }


    @Override
    public List<Subtask> findAll() {
        return null;
    }

    @Override
    public Subtask findByID(String s) {


        String sql = "select * from Subtask where subtask_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql,subtaskRowmapper,s);
        } catch (DataAccessException e) {
            throw new RuntimeException("could not find subtask");
        }


    }

    @Override
    public Subtask save(Subtask object) {
        return null;
    }
}
