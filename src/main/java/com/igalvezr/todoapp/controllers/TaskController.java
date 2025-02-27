/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igalvezr.todoapp.controllers;

import com.igalvezr.todoapp.entities.State;
import com.igalvezr.todoapp.entities.Task;
import com.igalvezr.todoapp.entities.dto.TaskDTO;
import com.igalvezr.todoapp.filtering.FilteredTasks;
import com.igalvezr.todoapp.services.FilteringService;
import com.igalvezr.todoapp.services.TaskService;
import jakarta.validation.Valid;
import java.time.DateTimeException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ivan
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;
    private FilteringService filteringService;

    public TaskController(TaskService taskService, FilteringService filteringService) {
        this.taskService = taskService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public ResponseEntity retrieveAllTasks(
            @RequestParam(required=false) String title_contains,
            @RequestParam(required=false) String desc_contains,
            @RequestParam(required=false) String priority,
            @RequestParam(required=false) String desired_completion_start,
            @RequestParam(required=false) String desired_completion_end,
            @RequestParam(required=false) String creation_date_start,
            @RequestParam(required=false) String creation_date_end
            ) {
        
        String[] params = {
            title_contains,
            desc_contains,
            priority,
            desired_completion_start,
            desired_completion_end,
            creation_date_start,
            creation_date_end
        };
        
        boolean hasParams = false;
        
        for (var param : params) {
            if (param != null)
                hasParams = true;
        }
        
        if (!hasParams) {
            List<Task> allTasks = taskService.getAll();
            return ResponseEntity.ok(allTasks);
        }
        
        Map<FilteredTasks.FilterType, String[]> mappedParams = filteringService.getFiltertypeMappings(
                title_contains, 
                desc_contains, 
                priority, 
                desired_completion_start, 
                desired_completion_end, 
                creation_date_start, 
                creation_date_end);
        
        List<Task> rawTasks = taskService.getAll();
        List<Task> filteredTasks;
        
        try {
            filteredTasks = filteringService.applyFilters(mappedParams, rawTasks);
        } catch (IllegalStateException e) {
            return ResponseEntity.internalServerError().body("The filters couldn't be applied");
        }
        
        
        return ResponseEntity.ok(filteredTasks);
    }
    
    @PostMapping
    public ResponseEntity createTask(@Valid @RequestBody TaskDTO taskData) {
        Task taskEntity;
        
        try {
            taskEntity = taskService.dtoToTask(taskData);
        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().body("The date provided is invalid");
        }
        
        int result = taskService.saveTask(taskEntity);
        
        if (0 == result)
            return ResponseEntity.ok("The task has been stored correctly.");
        else
            return ResponseEntity.status(500).body("The task hasn't been created");
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity changeState(@PathVariable Integer id, @RequestParam State state) {
        int result = taskService.changeState(id, state);
        
        if (0 == result) {
            return ResponseEntity.ok("The state has been changed");
        } else if (404 == result)
            return ResponseEntity.internalServerError().body("The requested task doesn't exist");
        else if (-1 == result)
            return ResponseEntity.badRequest().body("The new state can't be equal to the previous state");
        else
            return ResponseEntity.badRequest().body("The task's state couldn't be changed");
    }
}
