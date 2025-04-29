package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.ProjectRowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository implements CrudMethods<Project,String> {

    private final JdbcTemplate jdbcTemplate;
    private final ProjectRowMapper projectRowMapper;


    public ProjectRepository(JdbcTemplate jdbcTemplate, ProjectRowMapper projectRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectRowMapper = projectRowMapper;
    }

    public List<Project> getAllProjectsFromProfile(Profile profile){
        if (profile.getAuthCode()==0){
            return new ArrayList<>();
        }
        else if(profile.getAuthCode()==1){
            String getOwnersProjects = "select * from project where PROJECT_OWNER_PROFILE_USERNAME = ?";
            return jdbcTemplate.query(getOwnersProjects,projectRowMapper,profile.getUsername());


        }else {
            String getAssignedProjects = """
            SELECT p.*
            FROM Project p
            JOIN Profile_Project pp ON p.PROJECT_ID = pp.PROJECT_ID
            WHERE pp.PROFILE_USERNAME = ?
            """;
            return jdbcTemplate.query(getAssignedProjects, projectRowMapper, profile.getUsername());
        }



    }


    @Override
    public Project findAll() {
        return null;
    }

    @Override
    public Project findByID(String id) {
        String sql = "select * from Project where Project_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql,projectRowMapper,id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found for project id: " + id, e);
        }


    }

    @Override
    public Project save(Project object) {
        return null;
    }




}
