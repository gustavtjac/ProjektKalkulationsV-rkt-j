package gruppe6.kea.projektkalkulationeksamensprojekt.Services;

import gruppe6.kea.projektkalkulationeksamensprojekt.DTO.SubtaskDTO;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SubtaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;

    public SubtaskService(SubtaskRepository subtaskRepository) {
        this.subtaskRepository = subtaskRepository;
    }

    public List<Subtask> findAllSubtaskByTaskID(String id) {
        return subtaskRepository.findAllSubtaskByTaskID(id);
    }

    public Subtask createNewSubtask(SubtaskDTO subtaskDTO){
        return subtaskRepository.createNewSubtask(subtaskDTO);
    }

    public Subtask findById(String id){
        return subtaskRepository.findByID(id);
    }

    public Subtask deleteSubtask(String id){
        return subtaskRepository.deleteSubtask(id);
    }


}
