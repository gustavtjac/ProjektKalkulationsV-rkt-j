package gruppe6.kea.projektkalkulationeksamensprojekt.Models;

import java.util.Date;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String description;
    private double maxTime;
    private double maxPrice;
    private Date endDate;
    private Profile projectOwner;
    private List<Profile> projectMembers;
    private List<Task> tasks;

    public Project(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Profile getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(Profile projectOwner) {
        this.projectOwner = projectOwner;
    }

    public List<Profile> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<Profile> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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
