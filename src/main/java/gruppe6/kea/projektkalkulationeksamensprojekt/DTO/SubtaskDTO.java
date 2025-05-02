package gruppe6.kea.projektkalkulationeksamensprojekt.DTO;

public class SubtaskDTO {
    private String id;
    private String taskId;
    private String name;
    private String description;
    private double time;
    private int status;

    public SubtaskDTO() {
    }

    public SubtaskDTO(String id, String taskId, String name, String description, double time, int status) {
        this.id = id;
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.time = time;
        this.status = status;
    }

}
