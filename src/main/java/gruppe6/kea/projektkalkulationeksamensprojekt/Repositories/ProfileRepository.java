package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.ProfileRowMapper;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.SkillRowmapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

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

    public List<Profile> getAllMembersOfProjectFromProjectID(String projectID){
        String sql = """
        SELECT s.*
        FROM profile_project ps
        JOIN profile s ON ps.PROFILE_USERNAME = s.PROFILE_USERNAME
        WHERE ps.PROJECT_ID = ?
        """;
        return jdbcTemplate.query(sql,profileRowMapper, projectID);
    }

    public Profile getProfileFromUsername(String username){
        String sql = "Select * from Profile where PROFILE_USERNAME = ?";

        try{
           return jdbcTemplate.queryForObject(sql,profileRowMapper,username);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for username: " + username, e);
            }


    }


}
