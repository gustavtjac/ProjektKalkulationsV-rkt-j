package gruppe6.kea.projektkalkulationeksamensprojekt.Services;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    private SkillRepository skillRepository;

    public Profile AuthenticateLogin(String username,String password){
      return profileRepository.AuthenticateLogin(username,password);
    }

    public Profile createNewProfile(ProfileDTO profileDTO){
        if (profileDTO.getSkills() == null){
            profileDTO.setSkills(new ArrayList<>());
        }
        return profileRepository.createNewProfile(profileDTO);
    }

    public boolean checkIfUsernameExists(String username){
        return profileRepository.checkIfUsernameExists(username);

    }

    public List<Profile> findAllProfiles() {
        return profileRepository.findAll();
    }
public Profile findById(String id){
        return profileRepository.findByID(id);
}

public Profile saveDTO(ProfileDTO dto, String oldUsername){
        Profile dtoToProfile = new Profile();
        dtoToProfile.setUsername(dto.getUsername());
        dtoToProfile.setName(dto.getName());
        dtoToProfile.setSalary(dto.getSalary());
        dtoToProfile.setPassword(dto.getPassword());
        ArrayList<Skill> skills = new ArrayList<>();
        for (String skill : dto.getSkills()){

            skills.add(skillRepository.findByID(skill));
        }
        dtoToProfile.setSkills(skills);
        dtoToProfile.setAuthCode(dto.getAuthCode());
        return profileRepository.save(dtoToProfile,oldUsername);
}


    public Profile deleteFromId(String id){
        return profileRepository.deleteFromId(id);
    }


    public List<Profile> getAllProfilesAssignedToProject(String projectID){
        return profileRepository.getAllMembersOfProjectFromProjectID(projectID);
    }



}
