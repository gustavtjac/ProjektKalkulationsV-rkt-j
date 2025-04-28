package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.ProfileRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepository {
    JdbcTemplate jdbcTemplate;

    public ProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    //denne metoder indsamler alle de rækker hvor username og password er = med input
    public Profile AuthenticateLogin(String username, String password){
        String sqlString = "Select * from PROFILE where PROFILE_USERNAME = ? and PROFILE_PASSWORD = ?";
        return jdbcTemplate.queryForObject(sqlString,new ProfileRowMapper());
    }


}
