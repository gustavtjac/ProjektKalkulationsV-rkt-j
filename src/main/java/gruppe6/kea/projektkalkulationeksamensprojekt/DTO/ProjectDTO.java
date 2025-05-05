package gruppe6.kea.projektkalkulationeksamensprojekt.DTO;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Project;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProjectDTO {
    private String id;
    private String name;
    private String description;
    private double maxTime;
    private double maxPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String projectOwner;
    private List<String> projectMembers;

    public ProjectDTO() {
    }

    public ProjectDTO(String id, String name, String description, double maxTime, double maxPrice, Date endDate, String projectOwner, List<String> projectMembers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxTime = maxTime;
        this.maxPrice = maxPrice;
        this.endDate = endDate;
        this.projectOwner = projectOwner;
        this.projectMembers = projectMembers;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxTime=" + maxTime +
                ", maxPrice=" + maxPrice +
                ", endDate=" + endDate +
                ", projectOwner='" + projectOwner + '\'' +
                ", projectMembers=" + projectMembers +
                '}';
    }
}