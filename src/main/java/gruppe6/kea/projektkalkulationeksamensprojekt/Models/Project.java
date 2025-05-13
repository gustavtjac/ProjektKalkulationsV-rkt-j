package gruppe6.kea.projektkalkulationeksamensprojekt.Models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Component
public class Project {
    private String id;
    private String name;
    private String description;
    private double maxTime;
    private double maxPrice;
    private Date endDate;
    private Profile projectOwner;
    private List<Profile> projectMembers;
    private List<Task> tasks;

    public Project() {
    }


    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxTime=" + maxTime +
                ", maxPrice=" + maxPrice +
                ", endDate=" + endDate +
                ", projectOwner=" + projectOwner +
                ", projectMembers=" + projectMembers +
                ", tasks=" + tasks +
                '}';
    }
}
