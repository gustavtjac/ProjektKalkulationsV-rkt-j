package gruppe6.kea.projektkalkulationeksamensprojekt.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Task {
    private String id;
    private String projectID;
    private Project project;
    private String name;
    private String description;
    private double maxTime;
    private double maxPrice;
    private List<Subtask> subtasks;

    public Task() {

    }


    @Override
    public String toString() {
        return "Task {id=" + id +
                "name=" + name +
                "description=" + description +
                "maxTime=" + maxTime +
                "maxPrice=" + maxPrice +
                "projectId=" + projectID +
                "subtasks=" + subtasks +
                "}";
    }
}
