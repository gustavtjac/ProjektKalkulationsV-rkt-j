package gruppe6.kea.projektkalkulationeksamensprojekt.Models;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Subtask {
    private String id;
    private String taskId;
    private Task task;
    private String name;
    private String description;
    private double time;
    private int status;
    private List<Profile> assignedProfiles;

    public Subtask() {

    }


    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                '}';
    }
}
