package gruppe6.kea.projektkalkulationeksamensprojekt.Models;

import java.util.List;

public class Task {
    private int id;
    private int projectID;
    private String name;
    private String description;
    private double maxTime;
    private double maxPrice;
    private List<Subtask> subtasks;

    public Task(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public String toString(){
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
