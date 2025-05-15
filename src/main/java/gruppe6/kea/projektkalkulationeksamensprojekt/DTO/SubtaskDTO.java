package gruppe6.kea.projektkalkulationeksamensprojekt.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubtaskDTO {
    private String id;
    private String taskId;
    private String name;
    private String description;
    private double time;
    private int status;
    private List<String> assignedProfiles = new ArrayList<>();

    public SubtaskDTO() {

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
