/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.igalvezr.todoapp.repositories;
import com.igalvezr.todoapp.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ivan
 */

public interface TaskRepository extends JpaRepository <Task, Integer> {
    
}
