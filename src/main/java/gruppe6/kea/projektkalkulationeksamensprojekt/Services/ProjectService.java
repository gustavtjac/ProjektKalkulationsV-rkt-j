package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProjectDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjectsFromProfile(Profile profile){

        return projectRepository.getAllProjectsFromProfile(profile)
                .stream()
                .sorted(Comparator.comparing(Project::getEndDate))  // This will sort by date ascending
                .collect(Collectors.toList());
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
    public String getDeadlineClass(Date endDate) {
        Date today = new Date();
        long diffInMillies = endDate.getTime() - today.getTime();
        long daysDiff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (endDate.before(today)) {
            return "red-text";
        } else if (daysDiff < 7) {
            return "yellow-text";
        } else {
            return "";
        }
    }


    public void deleteProject(String projectID){
        projectRepository.deleteProject(projectID);
    }

    public void deleteTask(String taskID){
        projectRepository.deleteTask(taskID);
    }


    public ProjectDTO createNewProject(ProjectDTO projectDTO) {

        if (projectDTO.getProjectMembers()==null){
            projectDTO.setProjectMembers(new ArrayList<>());
        }
        return projectRepository.createNewProject(projectDTO);
    }

    public Project editProject(Project projectEdit){
        return projectRepository.editProject(projectEdit);
    }

    public Project save(ProjectDTO dto){
        return projectRepository.save(dto);
    }
}
