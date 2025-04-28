package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.SkillRowmapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SkillRepository {

    private final JdbcTemplate jdbcTemplate;

    public SkillRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //denne metode indhendter alle skills baseret på et username
    public List<Skill> getAllAssignedSkillsFromUsername(String username) {
        String sql = """
        SELECT s.*
        FROM profile_skill ps
        JOIN skill s ON ps.SKILL_ID = s.SKILL_ID
        WHERE ps.PROFILE_USERNAME = ?
        """;
        return jdbcTemplate.query(sql, new SkillRowmapper(), username);
    }


    }



