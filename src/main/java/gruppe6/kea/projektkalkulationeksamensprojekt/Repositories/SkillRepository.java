package gruppe6.kea.projektkalkulationeksamensprojekt.Repositories;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers.SkillRowmapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SkillRepository {

    JdbcTemplate jdbcTemplate;


    public SkillRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //denne metode indhendter alle skills baseret på et username
    public List<Skill> getAllSkillsFromUsername(String username){
        String sql = "select * from profile_skill where PROFILE_USERNAME = ?";
       return jdbcTemplate.query(sql,new SkillRowmapper(),username);
    }


}
