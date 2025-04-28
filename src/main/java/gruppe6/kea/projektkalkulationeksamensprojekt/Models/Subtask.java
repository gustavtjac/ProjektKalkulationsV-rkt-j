package gruppe6.kea.projektkalkulationeksamensprojekt.Models;

public class Subtask {
    private int id;
    private int taskId;
    private String name;
    private String description;
    private double time;

    public Subtask(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String toString(){
        return "Subtask {id=" + id + ", name=" + name + ", description=" + description + ", taskId=" + taskId + "}";
    }
}
