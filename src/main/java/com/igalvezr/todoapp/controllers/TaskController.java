/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igalvezr.todoapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ivan
 */
@RestController
@RequestMapping("/users")
public class TaskController {
    private TaskController taskController;

    public TaskController(TaskController taskController) {
        this.taskController = taskController;
    }
    
    
}
