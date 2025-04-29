package gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
public class ProjectRowMapper implements RowMapper<Project> {


    private final ProfileRepository profileRepository;

    public ProjectRowMapper(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
       Project project = new Project();
       project.setId(rs.getString("PROJECT_ID"));
       project.setName(rs.getString("PROJECT_NAME"));
      project.setProjectOwner(rs.getString("PROJECT_OWNER_PROFILE_USERNAME"));
      project.setDescription(rs.getString("PROJECT_DESC"));
      project.setMaxTime(rs.getDouble("PROJECT_MAX_TIME"));
      project.setMaxPrice(rs.getDouble("PROJECT_MAX_PRICE"));
      project.setEndDate(rs.getDate("PROJECT_ENDDATE"));
      List<Profile> memberlist = profileRepository.getAllMembersOfProjectFromProjectID(project.getId());
       project.setProjectMembers(memberlist);

        return project;
    }
}
