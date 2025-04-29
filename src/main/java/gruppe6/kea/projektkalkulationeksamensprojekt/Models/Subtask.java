package gruppe6.kea.projektkalkulationeksamensprojekt.Models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subtask {
    private String id;
    private String taskId;
    private String name;
    private String description;
    private double time;
    private int status;

    public Subtask(){

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
