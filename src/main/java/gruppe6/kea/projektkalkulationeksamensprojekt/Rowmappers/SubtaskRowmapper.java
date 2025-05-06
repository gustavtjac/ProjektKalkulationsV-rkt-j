package gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.ProfileService;
import gruppe6.kea.projektkalkulationeksamensprojekt.Services.SubtaskService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class SubtaskRowmapper implements RowMapper<Subtask> {

    private final ProfileRepository profileRepository;

    public SubtaskRowmapper(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Subtask mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subtask subtask = new Subtask();
        subtask.setDescription(rs.getString("SUBTASK_DESC"));
        subtask.setName(rs.getString("SUBTASK_NAME"));
        subtask.setId(rs.getString("SUBTASK_ID"));
        subtask.setTaskId(rs.getString("SUBTASK_TASK_ID"));
        subtask.setTime(rs.getDouble("SUBTASK_TIME"));
        subtask.setStatus(rs.getInt("SUBTASK_STATUS"));
        subtask.setAssignedProfiles(profileRepository.getAllprofilesAssginedToSubtask(subtask.getId()));
        return subtask;
    }
}
