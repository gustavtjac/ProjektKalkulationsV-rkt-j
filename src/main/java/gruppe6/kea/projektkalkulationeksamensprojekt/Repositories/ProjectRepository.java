package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.ProjectRowMapper;
import org.springframework.dao.DataAccessException;
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
            String getOwnersProjects = "select * from Project where PROJECT_OWNER_PROFILE_USERNAME = ?";
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

    public Project createNewProject(ProjectDTO projectDTO) {
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
        return findByID(projectDTO.getId());
    }
    }


    @Override
    public List<Project>findAll() {
        return null;
    }

    @Override
    public Project findByID(String id) {
        String sql = "select * from Project where PROJECT_ID = ?";
        try {
            System.out.println(id);
            return jdbcTemplate.queryForObject(sql,projectRowMapper,id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found", e);
        }

    }

    @Override
    public Project save(Project object) {
        return null;
    }

    public Project save(ProjectDTO object) {

        String sql = "UPDATE Project SET PROJECT_NAME = ?, PROJECT_DESC = ?, PROJECT_MAX_TIME = ?, PROJECT_MAX_PRICE = ?, PROJECT_ENDDATE = ?, PROJECT_OWNER_PROFILE_USERNAME = ? WHERE PROJECT_ID = ?";
        String deleteOldMemberSql = "delete from Profile_Project where PROJECT_ID = ?";
        String insertNewMemberSql = "insert into Profile_Project (PROJECT_ID,PROFILE_USERNAME) values (?,?)";

        try {
            jdbcTemplate.update(sql, object.getName(), object.getDescription(), object.getMaxTime(), object.getMaxPrice(), object.getEndDate(),object.getProjectOwner(), object.getId());
            jdbcTemplate.update(deleteOldMemberSql, object.getId());
            for (String profile : object.getProjectMembers()) {
                jdbcTemplate.update(insertNewMemberSql, object.getId(), profile);
            }

            return findByID(object.getId());
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save changes to project");
        }
    }



    public void deleteProject(String projectID){
        String deleteSQL = "DELETE FROM Project WHERE PROJECT_ID = ?";
        jdbcTemplate.update(deleteSQL, projectID);
    }


    public Project editProject(Project projectEdit) {
        String sql = "UPDATE Project SET PROJECT_NAME = ?, PROJECT_DESC = ?, PROJECT_MAX_TIME = ?, PROJECT_MAX_PRICE = ?, PROJECT_END_DATE = ? WHERE PROJECT_ID = ?";
        jdbcTemplate.update(sql, projectEdit.getName(), projectEdit.getDescription(), projectEdit.getMaxTime(), projectEdit.getMaxPrice(), projectEdit.getEndDate(), projectEdit.getId()); // Make sure editedProject has the ID set
        return projectEdit;
    }
}
