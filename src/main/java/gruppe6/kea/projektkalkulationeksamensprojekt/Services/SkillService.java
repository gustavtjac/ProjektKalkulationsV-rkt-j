package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;


    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill createNewSkill(Skill skill) {
        return skillRepository.createNewSkill(skill);
    }

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public Skill deleteFromId(String id) {
        return skillRepository.deleteFromId(id);
    }


    public Skill findByID(String id) {
        return skillRepository.findByID(id);
    }


    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

}
