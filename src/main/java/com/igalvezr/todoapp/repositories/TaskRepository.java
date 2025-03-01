package com.igalvezr.todoapp.repositories;
import com.igalvezr.todoapp.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ivan
 */

public interface TaskRepository extends JpaRepository <Task, Integer> {
    
}
