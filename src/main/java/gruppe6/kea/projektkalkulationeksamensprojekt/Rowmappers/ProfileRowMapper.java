package gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SkillRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileRowMapper implements RowMapper<Profile> {

    private final SkillRepository skillRepository;

    // Inject the SkillRepository using constructor injection
    public ProfileRowMapper(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile profile = new Profile();
        profile.setUsername(rs.getString("PROFILE_USERNAME"));
        profile.setName(rs.getString("PROFILE_NAME"));
        profile.setAuthCode(rs.getInt("PROFILE_AUTH_CODE"));
        profile.setPassword(rs.getString("PROFILE_PASSWORD"));
        profile.setSalary(rs.getDouble("PROFILE_SALARY"));
        List<Skill> skillList = skillRepository.getAllAssignedSkillsFromUsername(profile.getUsername());
        if (skillList.isEmpty()){
            profile.setSkills(new ArrayList<>());
        }
        else {
            profile.setSkills(skillList);
        }


        return profile;
    }
}