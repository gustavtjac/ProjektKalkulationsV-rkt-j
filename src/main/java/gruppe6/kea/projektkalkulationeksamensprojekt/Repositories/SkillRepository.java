package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.SkillRowmapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SkillRepository implements CrudMethods<Skill, String> {

    private final JdbcTemplate jdbcTemplate;

    public SkillRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //denne metode indhendter alle skills baseret på et username
    public List<Skill> getAllAssignedSkillsFromUsername(String username) {
        String sql = """
                SELECT s.*
                FROM Profile_Skill ps
                JOIN Skill s ON ps.SKILL_ID = s.SKILL_ID
                WHERE ps.PROFILE_USERNAME = ?
                """;
        return jdbcTemplate.query(sql, new SkillRowmapper(), username);
    }


    @Override
    public List<Skill> findAll() {
        String sql = "select * from skill";
        return jdbcTemplate.query(sql, new SkillRowmapper());
    }

    @Override
    public Skill findByID(String s) {
        String sql = "select * from Skill where skill_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new SkillRowmapper(), s);
        } catch (DataAccessException e) {
            throw new RuntimeException("Could not find skill from id");
        }

    }

    @Override
    public Skill save(Skill object) {
        String sql = "update skill set skill_name =? where skill_id = ?";
        try {
            jdbcTemplate.update(sql, object.getName(), object.getId());
            return object;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }


    public Skill createNewSkill(Skill skill) {
        skill.setId(UUID.randomUUID().toString());
        String sql = "Insert into skill (skill_id,skill_name) values (?,?)";
        try {
            jdbcTemplate.update(sql, skill.getId(), skill.getName());
        } catch (DataAccessException e) {
            throw new RuntimeException("Skill already exists");
        }
        return skill;
    }

    public Skill deleteFromId(String id) {
        Skill skill = findByID(id);


        String sql = "Delete from Skill where skill_id = ?";
        try {
            jdbcTemplate.update(sql, id);
            return skill;
        } catch (DataAccessException e) {
            throw new RuntimeException("Skill could not be deleted");
        }


    }

}



