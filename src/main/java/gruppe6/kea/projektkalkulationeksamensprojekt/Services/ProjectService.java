package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjectsFromProfile(Profile profile){
        return projectRepository.getAllProjectsFromProfile(profile);
    }

    public Project findById(String id){
       return projectRepository.findByID(id);
    }

    public boolean checkIfProfileIsAssignedProject(Profile profile, Project project){
        String profileID = profile.getUsername();
        if (profile.getAuthCode()==1){
            if (project.getProjectOwner().getUsername().equalsIgnoreCase(profileID)){
                return true;
            }
        }else if (profile.getAuthCode()==0){
            return true;
        }else{
            for (Profile projectmember : project.getProjectMembers()){
                if (projectmember.getUsername().equalsIgnoreCase(profileID)){
                    return true;
                }

            }
        }
        return false;
    }

    public boolean checkIfProfileOwnsProject(String projectID, String username){
        Project foundproject = projectRepository.findByID(projectID);
        if(foundproject.getProjectOwner().getUsername().equals(username)){
            return true;
        }
        return false;
    }

    public void deleteProject(String projectID){
        projectRepository.deleteProject(projectID);
    }

    public void deleteTask(String taskID){
        projectRepository.deleteTask(taskID);
    }


    public ProjectDTO createNewProject(ProjectDTO projectDTO) {
        return projectRepository.createNewProject(projectDTO);
    }
}
