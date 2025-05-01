package gruppe6.kea.projektkalkulationeksamensprojekt.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ProfileDTO {
    private String username;
    private String password;
    private String name;
    private int authCode;
    private List<String> skills;

    public ProfileDTO() {
    }

    public ProfileDTO(String username, List<String> skills, int authCode, String name, String password) {
        this.username = username;
        this.skills = skills;
        this.authCode = authCode;
        this.name = name;
        this.password = password;
    }


}
