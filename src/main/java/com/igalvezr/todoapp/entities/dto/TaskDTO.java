package com.igalvezr.todoapp.entities.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author ivan
 */

public class TaskDTO {
    @NotBlank
    private String title;
    
    private String description;
    private Integer priority;
    private DateTime due_date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public DateTime getDue_date() {
        return due_date;
    }

    public void setDue_date(DateTime due_date) {
        this.due_date = due_date;
    }

    @Override
    public String toString() {
        return "TaskDTO{" + "title=" + title + ", description=" + description + ", priority=" + priority + ", due_date=" + due_date + '}';
    }
    
    
}
