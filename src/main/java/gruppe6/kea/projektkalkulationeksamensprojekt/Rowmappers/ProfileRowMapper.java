package gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileRowMapper implements RowMapper<Profile> {

    SkillRepository skillRepository = new SkillRepository(new JdbcTemplate());


    //for at kunne rowmappe en profil bliver vi nødt til at benytte os af flere forskellige repositories da en profil indeholder elementer både fra PROFIL skemaet men også SKill skemaet
    @Override
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile profile = new Profile();
        profile.setUsername(rs.getString("PROFILE_USERNAME"));
        profile.setName("PROFILE_NAME");
        profile.setAuthCode(rs.getInt("PROFILE_AUTH_CODE"));
        profile.setSkills(skillRepository.getAllSkillsFromUsername(profile.getUsername()));
        return profile;
    }
}
