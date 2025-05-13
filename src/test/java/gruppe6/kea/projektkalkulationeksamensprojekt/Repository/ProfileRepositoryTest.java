package gruppe6.kea.projektkalkulationeksamensprojekt.Repository;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
@SpringBootTest
@Transactional
@Rollback(true)
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    //hvis assert ikke står under teksten er det fordi dataen eksisterer i H2init scriptet


    //tester om den retunerer den rigtige bruger når man indskriver rigtigt info
    @Test
    public void authenticateLogin_returnsCorrectProfile() {
        // Act
        Profile result = profileRepository.AuthenticateLogin("admin", "password123");
        // Assert
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
    }


    //tester om den retunerer den rigtige bruger når man indskriver forkert info
    @Test
    public void authenticateLogin_returnsNull() {
        // Act
                                                            //skriver forkert username med vilje
        Profile result = profileRepository.AuthenticateLogin("admim", "password123");
        // Assert
        assertNull(result);
    }

    //henter en bruger baseret på brugernavn
    @Test
    public void getProfileFromUsername_returnsCorrectProfile() {
        // Act
        Profile profile = profileRepository.getProfileFromUsername("admin");
        // Assert
        assertNotNull(profile);
        assertEquals("admin", profile.getUsername());
    }

    @Test
    public void createNewProfile_insertsProfileAndSkills() {
        // Arrange
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("jdoe");
        profileDTO.setName("John Doe");
        profileDTO.setPassword("secure");
        profileDTO.setAuthCode(1);
        profileDTO.setSalary(40000.0);
        profileDTO.setSkills(new ArrayList<>(List.of("1"))); //tildeler ham skill 1

        // Act
        Profile created = profileRepository.createNewProfile(profileDTO);

        // Assert
        assertNotNull(created);

        assertEquals("1",created.getSkills().get(0).getId());
        assertEquals("jdoe", created.getUsername());
        assertEquals("John Doe", created.getName());
    }

    @Test
    public void checkIfUsernameExists_returnsTrueForExistingUser() {
        // Act
        boolean exists = profileRepository.checkIfUsernameExists("admin");
        // Assert
        assertTrue(exists);
    }

    @Test
    public void deleteFromId_removesProfile() {
        // Arrange
        String username = "admin";
        //tjekker først at brugeren eksisterer
        assertTrue(profileRepository.checkIfUsernameExists(username));

        // Act
        //sletter ham fra databasen
        Profile deleted = profileRepository.deleteFromId(username);

        // Assert
        //sammenligner den slettede med udgangspunket
        assertNotNull(deleted);
        assertEquals(username, deleted.getUsername());
        //kigger om han findes nu
        assertFalse(profileRepository.checkIfUsernameExists(username));
    }

    @Test
    public void findAll_returnsProfiles() {
        // Act
        List<Profile> profiles = profileRepository.findAll();
        // Assert

        //den er ikke tom da der eksisterer testdata i h2 scriptet
        assertFalse(profiles.isEmpty());
    }

    @Test
    public void getAllMembersOfProjectFromProjectID_returnsProfiles() {
        // Arrange
        String existingProjectId = "project-123";

        // Act
        List<Profile> members = profileRepository.getAllMembersOfProjectFromProjectID(existingProjectId);

        // Assert

        //listen burde ikke være null fordi vi altid tildeler en tom list
        assertNotNull(members);
    }

    @Test
    public void getAllProfilesAssignedToSubtask_returnsProfiles() {
        // Arrange
        String subtaskId = "project-123";

        // Act
        List<Profile> profiles = profileRepository.getAllprofilesAssginedToSubtask(subtaskId);

        // Assert
        assertNotNull(profiles);
    }
}
