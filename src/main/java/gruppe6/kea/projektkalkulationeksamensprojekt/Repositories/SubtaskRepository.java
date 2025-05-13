package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.SubtaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.SubtaskRowmapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Repository
public class SubtaskRepository implements CrudMethods<Subtask, String> {

    private final JdbcTemplate jdbcTemplate;
    private final SubtaskRowmapper subtaskRowmapper;

    public SubtaskRepository(JdbcTemplate jdbcTemplate, SubtaskRowmapper subtaskRowmapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.subtaskRowmapper = subtaskRowmapper;
    }


    public List<Subtask> findAllSubtaskByTaskID(String id) {
        String sql = "SELECT * FROM Subtask WHERE SUBTASK_TASK_ID = ?";
        return jdbcTemplate.query(sql, subtaskRowmapper, id);

    }

    public Subtask createNewSubtask(SubtaskDTO subtaskDTO) {
        subtaskDTO.setId(UUID.randomUUID().toString());
        subtaskDTO.setStatus(1);


        String sql = "INSERT INTO Subtask (SUBTASK_ID, SUBTASK_TASK_ID,SUBTASK_NAME,SUBTASK_DESC,SUBTASK_TIME,SUBTASK_STATUS) VALUES (?,?,?,?,?,?)";

        String insertProfiles = "INSERT INTO Subtask_Profile (SUBTASK_ID,PROFILE_USERNAME) VALUES (?,?)";

        try {
            jdbcTemplate.update(sql, subtaskDTO.getId(), subtaskDTO.getTaskId(), subtaskDTO.getName(), subtaskDTO.getDescription(), subtaskDTO.getTime(), subtaskDTO.getStatus());

        } catch (DataAccessException e) {
            throw new RuntimeException("could not create subtask");
        }

        try {
            System.out.println(subtaskDTO.getAssignedProfiles());
            for (String username : subtaskDTO.getAssignedProfiles()) {
                jdbcTemplate.update(insertProfiles, subtaskDTO.getId(), username);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("could not insert users");
        }

        return findByID(subtaskDTO.getId());
    }


    @Override
    public List<Subtask> findAll() {
        return null;
    }

    @Override
    public Subtask findByID(String subtaskID) {
        String sql = "SELECT * FROM Subtask WHERE SUBTASK_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, subtaskRowmapper, subtaskID);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask not found!");
        }

    }


    public Subtask deleteSubtask(String id) {
        Subtask deletedSubtask = findByID(id);
        String sql = "DELETE FROM Subtask WHERE SUBTASK_ID = ?";

        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Could not delete subtask");
        }

        return deletedSubtask;
    }

    public Subtask saveSubtask(SubtaskDTO subtaskDTO) {

        String sqlDeleteOldAssignedProfiles = "DELETE FROM Subtask_Profile WHERE SUBTASK_ID = ?";
        String sqlAddNewAssignedProfiles = "INSERT INTO Subtask_Profile (SUBTASK_ID, PROFILE_USERNAME) VALUES (?,?)";
        String sql = "UPDATE subtask SET SUBTASK_NAME = ?, SUBTASK_DESC = ?, SUBTASK_TIME = ?, SUBTASK_STATUS = ? WHERE SUBTASK_ID = ?";


        try {
            jdbcTemplate.update(sqlDeleteOldAssignedProfiles, subtaskDTO.getId());
            jdbcTemplate.update(sql, subtaskDTO.getName(), subtaskDTO.getDescription(), subtaskDTO.getTime(), subtaskDTO.getStatus(), subtaskDTO.getId());

            for (String username : subtaskDTO.getAssignedProfiles()) {
                jdbcTemplate.update(sqlAddNewAssignedProfiles, subtaskDTO.getId(), username);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask not found!");

        }

        return findByID(subtaskDTO.getId());
    }


    @Override
    public Subtask save(Subtask object) {
        return null;
    }


    public List<Subtask> findAllSubtaskFromProfile(Profile profile) {
        String sql = """
                SELECT s.*
                FROM Subtask_Profile sp
                JOIN Subtask s ON sp.SUBTASK_ID = s.SUBTASK_ID
                WHERE sp.PROFILE_USERNAME = ?
                """;

        return jdbcTemplate.query(sql, subtaskRowmapper, profile.getUsername());
    }
}
