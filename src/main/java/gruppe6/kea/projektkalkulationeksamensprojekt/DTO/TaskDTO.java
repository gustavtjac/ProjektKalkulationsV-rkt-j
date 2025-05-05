package gruppe6.kea.projektkalkulationeksamensprojekt.DTO;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private String id;
    private String projectID;
    private String name;
    private String description;
    private double maxTime;
    private double maxPrice;
    private List<String> subtasks;

    public TaskDTO() {
    }

    public TaskDTO(String id, String projectID, String name, String description, double maxTime, double maxPrice, List<String> subtasks) {
        this.id = id;
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.maxTime = maxTime;
        this.maxPrice = maxPrice;
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id='" + id + '\'' +
                ", projectID='" + projectID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxTime=" + maxTime +
                ", maxPrice=" + maxPrice +
                ", subtasks=" + subtasks +
                '}';
    }
}
