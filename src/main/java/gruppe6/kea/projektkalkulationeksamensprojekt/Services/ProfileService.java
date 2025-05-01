package gruppe6.kea.projektkalkulationeksamensprojekt.Services;


import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    public Profile AuthenticateLogin(String username,String password){
      return profileRepository.AuthenticateLogin(username,password);
    }

    public List<Profile> findAllProfiles() {
        return profileRepository.findAll();
    }


}
