package gruppe6.kea.projektkalkulationeksamensprojekt.Rowmappers;

import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Subtask;
import gruppe6.kea.projektkalkulationeksamensprojekt.Models.Task;
import gruppe6.kea.projektkalkulationeksamensprojekt.Repositories.SubtaskRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Component
public class TaskRowMapper implements RowMapper<Task> {
    private final SubtaskRepository subtaskRepository;

    public TaskRowMapper(SubtaskRepository subtaskRepository) {
        this.subtaskRepository = subtaskRepository;
    }


    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getString("TASK_ID"));
        task.setDescription(rs.getString("TASK_DESC"));
        task.setMaxPrice(rs.getDouble("TASK_MAX_PRICE"));
        task.setMaxTime(rs.getDouble("TASK_MAX_TIME"));
        task.setName(rs.getString("TASK_NAME"));
        task.setProjectID(rs.getString("TASK_PROJECT_ID"));

        List<Subtask> subtaskList = subtaskRepository.getSubtasksFromTaskID(task.getId());
        if (subtaskList==null){
            task.setSubtasks(new ArrayList<>());
        }else {
            task.setSubtasks(subtaskList);
        }

        return task;
    }
}
