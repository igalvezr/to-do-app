package com.igalvezr.todoapp.services;

import com.igalvezr.todoapp.entities.State;
import com.igalvezr.todoapp.entities.Task;
import com.igalvezr.todoapp.entities.dto.DateTime;
import com.igalvezr.todoapp.entities.dto.TaskDTO;
import com.igalvezr.todoapp.repositories.TaskRepository;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author ivan
 */
@Service
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public int deleteTask(Integer id) {
        taskRepository.deleteById(id);
        boolean result = taskRepository.existsById(id);

        if (result) {
            return -1;
        } else {
            return 0;
        }
    }

    public int modifyTask(Task task) {
        //TODO: implement this
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Task dtoToTask(TaskDTO dto) throws DateTimeException {
        // The defaults go here. They're hardcoded
        // We get a DTO entity that has at least a title. So we need to assign all the 
        // defaults here, which are the rest of the properties
        // except for completion_date and id.

        // TODO: read this from the properties
        // The first thing we need to check is if the date format is ok or it is
        // invalid, if provided.
        LocalDateTime due_date = LocalDateTime.now();
        if (dto.getDue_date() != null) {
            try {
                DateTime provided = dto.getDue_date();
                due_date = LocalDateTime.of(
                        provided.getYear(),
                        provided.getMonth(),
                        provided.getDay(),
                        provided.getHour(),
                        provided.getMinute(),
                        provided.getSecond());
            } catch (DateTimeException e) {
                System.out.println("WARNING: Exception while parsing due_date");
                throw e;
            }

        }

        // The default description is null
        String description = (dto.getDescription() != null) ? dto.getDescription() : "";
        // The default priority is 1
        Integer priority = (dto.getPriority() != null) ? dto.getPriority() : 1;

        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(description);
        task.setState(State.PENDING); // The default state is pending
        task.setPriority(priority);
        task.setCreation_date(LocalDateTime.now()); // the default for creation date is now
        task.setDue_date(due_date);

        return task;
    }
    
    public int saveTask(Task task) {
        Task saved = taskRepository.save(task);
        
        if (saved != null)
            return 0;
        else
            return -1;
    }
    
    public int changeState(Integer taskId, State state) {
        Optional<Task> originalTask = taskRepository.findById(taskId);
        
        if (originalTask.isEmpty())
            return 404; // not found code
        
        var taskEntity = originalTask.get();
        
        // check that we don't update to the same task
        if (taskEntity.getState() == state)
            return -1; // same state code
        
        // If the state WAS COMPLETED and now is different, we must delete the completion date
        if (taskEntity.getState() == State.COMPLETED)
            taskEntity.setCompletion_date(null);
        
        // If the state IS COMPLETED, then we also need to add the completed date
        if (state == State.COMPLETED) {
            taskEntity.setCompletion_date(LocalDateTime.now());
        }
        
        // finally, update the state
        taskEntity.setState(state);
        
        // Put the entity back into the db
        taskRepository.save(taskEntity);
        return 0;
    }
}
