package gruppe6.kea.projektkalkulationeksamensprojekt.Models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Skill {
    private String id;
    private String name;

    public Skill() {

    }


    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
