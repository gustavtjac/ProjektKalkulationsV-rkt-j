package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.ProfileRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
public class ProfileRepository implements CrudMethods<Profile, String> {


    private final JdbcTemplate jdbcTemplate;
    private final ProfileRowMapper profileRowMapper;

    public ProfileRepository(JdbcTemplate jdbcTemplate, ProfileRowMapper profileRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.profileRowMapper = profileRowMapper;
    }


    //denne metoder indsamler alle de rækker hvor username og password er = med input
    public Profile AuthenticateLogin(String username, String password) {

        String sqlString = "Select * from Profile where PROFILE_USERNAME = ? and PROFILE_PASSWORD = ?";
        List<Profile> matchedProfile = jdbcTemplate.query(sqlString, profileRowMapper, username, password);
        if (matchedProfile.isEmpty()) {
            return null;
        } else {
            return matchedProfile.getFirst();
        }


    }

    public List<Profile> getAllMembersOfProjectFromProjectID(String projectID) {
        String sql = """
                SELECT s.*
                FROM Profile_Project ps
                JOIN Profile s ON ps.PROFILE_USERNAME = s.PROFILE_USERNAME
                WHERE ps.PROJECT_ID = ?
                """;
        return jdbcTemplate.query(sql, profileRowMapper, projectID);
    }

    public Profile getProfileFromUsername(String username) {
        String sql = "Select * from Profile where PROFILE_USERNAME = ?";

        try {
            return jdbcTemplate.queryForObject(sql, profileRowMapper, username);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for username: " + username, e);
        }


    }


    public Profile createNewProfile(ProfileDTO profileDTO) {
        String newProfile = "insert into Profile (PROFILE_USERNAME,PROFILE_NAME,PROFILE_PASSWORD,PROFILE_AUTH_CODE,PROFILE_SALARY) VALUES (?,?,?,?,?)";
        String insertSkills = "INSERT INTO Profile_Skill (PROFILE_USERNAME,SKILL_ID) VALUES (?,?)";


        int profileRowsAffected = jdbcTemplate.update(newProfile, profileDTO.getUsername(), profileDTO.getName(), profileDTO.getPassword(), profileDTO.getAuthCode(), profileDTO.getSalary());
        if (profileRowsAffected > 0) {
            for (String skillID : profileDTO.getSkills()) {
                int skillRowsAffected = jdbcTemplate.update(insertSkills, profileDTO.getUsername(), skillID);
                if (skillRowsAffected == 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill was not found");
                }
            }
            return findByID(profileDTO.getUsername());
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Username Already exists");
    }


    @Override
    public List<Profile> findAll() {
        String sql = "SELECT * FROM Profile";
        return jdbcTemplate.query(sql, profileRowMapper);
    }

    @Override
    public Profile findByID(String s) {
        String sql = "select * from Profile where PROFILE_USERNAME = ?";

        try {
            return jdbcTemplate.queryForObject(sql, profileRowMapper, s);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username doesnt exist");
        }

    }

    @Override
    public Profile save(Profile object) {
        return null;
    }

    public Profile save(Profile object, String oldUsername) {
        String sql = "UPDATE Profile SET PROFILE_NAME = ?, PROFILE_USERNAME = ?, PROFILE_PASSWORD = ?, PROFILE_AUTH_CODE = ?, PROFILE_SALARY = ? WHERE PROFILE_USERNAME = ?";
        String deleteOldSkillsSql = "delete from profile_skill where profile_USERNAME = ?";
        String insertNewSkillsSql = "insert into profile_skill (PROFILE_USERNAME,skill_ID) values (?,?)";

        try {
            jdbcTemplate.update(sql, object.getName(), object.getUsername(), object.getPassword(), object.getAuthCode(), object.getSalary(), oldUsername);
            jdbcTemplate.update(deleteOldSkillsSql, object.getUsername());
            for (Skill skill : object.getSkills()) {
                jdbcTemplate.update(insertNewSkillsSql, object.getUsername(), skill.getId());
            }
            return object;
        } catch (DataAccessException e) {
            throw new RuntimeException("Could not save the edited employee");
        }


    }

    public boolean checkIfUsernameExists(String username) {
        String sql = "Select * from Profile where PROFILE_USERNAME = ?";
        List<Profile> profiles = jdbcTemplate.query(sql, profileRowMapper, username);
        return !profiles.isEmpty();
    }


    public Profile deleteFromId(String id) {

        Profile profile = findByID(id);

        String sql = "delete from Profile where PROFILE_USERNAME = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected == 0) {
            return null;
        } else {
            return profile;
        }

    }


    public List<Profile> getAllprofilesAssginedToSubtask(String subtaskID) {
        String sql = "SELECT p.* FROM Profile p JOIN Subtask_Profile sp ON p.PROFILE_USERNAME = sp.PROFILE_USERNAME WHERE sp.SUBTASK_ID = ?";
        return jdbcTemplate.query(sql, profileRowMapper, subtaskID);
    }
}
