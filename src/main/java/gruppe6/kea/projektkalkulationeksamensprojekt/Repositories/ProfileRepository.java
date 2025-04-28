package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.ProfileRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileRepository {




    private final JdbcTemplate jdbcTemplate;
private final ProfileRowMapper profileRowMapper;
    public ProfileRepository(JdbcTemplate jdbcTemplate, ProfileRowMapper profileRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.profileRowMapper = profileRowMapper;
    }



    //denne metoder indsamler alle de rækker hvor username og password er = med input
    public Profile AuthenticateLogin(String username, String password){
        String sqlString = "Select * from PROFILE where PROFILE_USERNAME = ? and PROFILE_PASSWORD = ?";
        List<Profile> matchedProfile = jdbcTemplate.query(sqlString,profileRowMapper,username,password);
        if (matchedProfile.isEmpty()){
            return null;
        }
        else {
            return matchedProfile.getFirst();
        }


    }


}
