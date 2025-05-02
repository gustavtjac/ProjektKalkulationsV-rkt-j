package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.ProjectRowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public ProjectDTO createNewProject(ProjectDTO projectDTO) {
        projectDTO.setId(UUID.randomUUID().toString());
        String sql = "INSERT into Project (PROJECT_ID,PROJECT_OWNER_PROFILE_USERNAME, PROJECT_NAME, PROJECT_DESC, PROJECT_MAX_TIME, PROJECT_MAX_PRICE, PROJECT_ENDDATE) values (?,?,?,?,?,?,?)";
                int rowsAffected = jdbcTemplate.update(sql,projectDTO.getId(),projectDTO.getProjectOwner(), projectDTO.getName(), projectDTO.getDescription(), projectDTO.getMaxTime(), projectDTO.getMaxPrice(), projectDTO.getEndDate());


                String insertWorkersSQL = "Insert into PROFILE_PROJECT (PROJECT_ID,PROFILE_USERNAME) values(?,?)";
                
                for (String username : projectDTO.getProjectMembers()){
                   jdbcTemplate.update(insertWorkersSQL,projectDTO.getId(),username);
                }
    if (rowsAffected==0){
        return null;
    }
    else {
        return projectDTO;
    }
    }


    @Override
    public List<Project>findAll() {
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


    public void deleteProject(String projectID){
        String deleteSQL = "DELETE FROM project WHERE project_ID = ?";
        jdbcTemplate.update(deleteSQL, projectID);
    }


    public Project editProject(Project projectEdit) {
        String editSQL = "UPDATE Project SET PROJECT_NAME = ?, PROJECT_DESC = ?, PROJECT_MAX_TIME = ?, PROJECT_MAX_PRICE = ?, PROJECT_END_DATE = ? WHERE PROJECT_ID = ?";
        jdbcTemplate.update(editSQL,
                projectEdit.getName(),
                projectEdit.getDescription(),
                projectEdit.getMaxTime(),
                projectEdit.getMaxPrice(),
                projectEdit.getEndDate(),
                projectEdit.getId()); // Make sure editedProject has the ID set
        return projectEdit;
    }
}
