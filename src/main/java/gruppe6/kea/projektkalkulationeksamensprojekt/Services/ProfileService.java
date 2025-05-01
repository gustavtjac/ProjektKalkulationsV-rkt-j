package gruppe6.kea.projektkalkulationeksamensprojekt.Services;


import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;

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


    public Profile deleteFromId(String id){
        return profileRepository.deleteFromId(id);
    }

}
