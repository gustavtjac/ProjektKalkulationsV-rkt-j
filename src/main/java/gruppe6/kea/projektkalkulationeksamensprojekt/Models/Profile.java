package gruppe6.kea.projektkalkulationeksamensprojekt.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class Profile {
    private String username;
    private String password;
    private double salary;
    private String name;
    private int authCode;
    private List<Skill> skills;

    public Profile(){

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
