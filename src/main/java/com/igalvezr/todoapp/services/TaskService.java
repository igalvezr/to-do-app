/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igalvezr.todoapp.services;

import com.igalvezr.todoapp.entities.Task;
import com.igalvezr.todoapp.repositories.TaskRepository;
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
        
        if (result)
            return -1;
        else
            return 1;
    }
    
    public int modifyTask(Task task) {
        //TODO: implement this
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
