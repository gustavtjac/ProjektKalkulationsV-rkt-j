package gruppe6.kea.projektkalkulationeksamensprojekt.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Profile {
    private String username;
    private String password;
    private double salary;
    private String name;
    private int authCode;
    private List<Skill> skills;

    public Profile(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", authCode=" + authCode +
                ", skills=" + skills +
                '}';
    }
}
