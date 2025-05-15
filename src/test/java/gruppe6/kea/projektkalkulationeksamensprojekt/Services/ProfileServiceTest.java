package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.ProfileDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Profile;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Skill;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.ProfileRepository;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SkillRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    private ProfileRepository profileRepository;
    private SkillRepository skillRepository;
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileRepository = mock(ProfileRepository.class);
        skillRepository = mock(SkillRepository.class);
        profileService = new ProfileService(profileRepository, skillRepository);
    }

    @Test
    void testAuthenticateLoginReturnsProfile() {
        // ARRANGE - vi laver en dummy profil og siger at den skal returneres hvis username og password matcher
        Profile expectedProfile = new Profile();
        when(profileRepository.AuthenticateLogin("admin", "1234")).thenReturn(expectedProfile);

        // ACT - vi kalder metoden
        Profile result = profileService.AuthenticateLogin("admin", "1234");

        // ASSERT - vi tjekker at vi får en profil tilbage
        assertNotNull(result);
        verify(profileRepository, times(1)).AuthenticateLogin("admin", "1234");
    }

    @Test
    void testCreateNewProfileInitializesEmptySkills() {
        // ARRANGE - hvis skills er null, skal de sættes som en tom liste
        ProfileDTO dto = new ProfileDTO();
        dto.setSkills(null); // simulerer at brugeren ikke har valgt nogen skills endnu
        Profile expectedProfile = new Profile();
        when(profileRepository.createNewProfile(any(ProfileDTO.class))).thenReturn(expectedProfile);

        // ACT
        Profile result = profileService.createNewProfile(dto);

        // ASSERT - vi tjekker at skills nu ikke er null og at profilen blev lavet
        assertNotNull(dto.getSkills());
        assertNotNull(result);
        verify(profileRepository).createNewProfile(dto);
    }

    @Test
    void testCheckIfUsernameExistsReturnsTrue() {
        // ARRANGE
        when(profileRepository.checkIfUsernameExists("admin")).thenReturn(true);

        // ACT
        boolean result = profileService.checkIfUsernameExists("admin");

        // ASSERT - vi forventer at brugernavnet eksisterer
        assertTrue(result);
    }

    @Test
    void testFindAllProfilesReturnsList() {
        // ARRANGE - laver en liste med én profil
        List<Profile> mockList = new ArrayList<>();
        mockList.add(new Profile());
        when(profileRepository.findAll()).thenReturn(mockList);

        // ACT
        List<Profile> result = profileService.findAllProfiles();

        // ASSERT - vi tjekker at listen har præcis én profil
        assertEquals(1, result.size());
    }

    @Test
    void testFindByIdReturnsProfile() {
        // ARRANGE
        Profile expected = new Profile();
        expected.setUsername("admin");
        when(profileRepository.findByID("admin")).thenReturn(expected);

        // ACT
        Profile result = profileService.findById("admin");

        // ASSERT - vi tjekker at brugernavnet matcher
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
    }

    @Test
    void testSaveDTOSavesCorrectly() {
        // ARRANGE - vi laver en DTO med 2 skills og opretter tilhørende dummy Skill-objekter
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername("newuser");
        dto.setName("New User");
        dto.setPassword("pass");
        dto.setSalary(1000);
        dto.setAuthCode(1);
        dto.setSkills(List.of("1", "2"));

        Skill skill1 = new Skill();
        skill1.setId("1");
        Skill skill2 = new Skill();
        skill2.setId("2");

        when(skillRepository.findByID("1")).thenReturn(skill1);
        when(skillRepository.findByID("2")).thenReturn(skill2);

        Profile saved = new Profile();
        when(profileRepository.save(any(Profile.class), eq("olduser"))).thenReturn(saved);

        // ACT
        Profile result = profileService.saveDTO(dto, "olduser");

        // ASSERT - vi tjekker at metoden blev kaldt og at skills blev hentet korrekt
        assertNotNull(result);
        verify(profileRepository).save(any(Profile.class), eq("olduser"));
        verify(skillRepository).findByID("1");
        verify(skillRepository).findByID("2");
    }

    @Test
    void testDeleteFromIdCallsRepository() {
        // ARRANGE - vi sletter en profil med username "admin"
        Profile deletedProfile = new Profile();
        deletedProfile.setUsername("admin");
        when(profileRepository.deleteFromId("admin")).thenReturn(deletedProfile);

        // ACT
        Profile result = profileService.deleteFromId("admin");

        // ASSERT - vi forventer at få den slettede profil tilbage
        assertEquals("admin", result.getUsername());
        verify(profileRepository).deleteFromId("admin");
    }

    @Test
    void testGetAllProfilesAssignedToProject() {
        // ARRANGE
        List<Profile> mockList = List.of(new Profile());
        when(profileRepository.getAllMembersOfProjectFromProjectID("proj123")).thenReturn(mockList);

        // ACT
        List<Profile> result = profileService.getAllProfilesAssignedToProject("proj123");

        // ASSERT
        assertEquals(1, result.size());
        verify(profileRepository).getAllMembersOfProjectFromProjectID("proj123");
    }

    @Test
    void testGetAllProfilesAssignedToSubtask() {
        // ARRANGE
        List<Profile> mockList = List.of(new Profile());
        when(profileRepository.getAllprofilesAssginedToSubtask("subtask123")).thenReturn(mockList);

        // ACT
        List<Profile> result = profileService.getAllprofilesAssginedToSubtask("subtask123");

        // ASSERT
        assertEquals(1, result.size());
        verify(profileRepository).getAllprofilesAssginedToSubtask("subtask123");
    }
}
