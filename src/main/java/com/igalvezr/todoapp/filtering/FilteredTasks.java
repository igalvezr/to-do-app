/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igalvezr.todoapp.filtering;

import com.igalvezr.todoapp.entities.Task;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan
 */

/* 
* Entity that contains the logic for recursive filter aplication.
* This filtering is made in a cascading fashion. That means, each filter acts
* on the result of the last filter, each filtering step applying one type
* of filtering.
*/
public class FilteredTasks {
    // This list contains the reference to the tasks being processed at this step
    private List<Task> tasks;
    // A simple counter to know how many filters were applied
    private Integer filtersApplied;
    
    // A private constructor prevents the instantiation of an empty filtering
    // entity
    private FilteredTasks(List<Task> tasks) {
        this.tasks = tasks;
        filtersApplied = 0;
    }
    
    // This method encapsulates the logic for updating the entity
    private FilteredTasks updatedEntity(List<Task> filteredTasks) {
        // Increment the counter of filters applied
        filtersApplied++;
        // Take the result of the last iteration
        tasks = filteredTasks;
        
        if (tasks.isEmpty())
            System.out.println("WARNING: the filtering list is empty at step " + this.filtersApplied);
        
        return this;
    }
    
    // At the end of the filtering, we return the final list with the resulting
    // elements
    public List<Task> to_list() {
        System.out.println("WARNING: Returning list with " + filtersApplied + " filters applied");
        return tasks;
    }
    
    public static FilteredTasks of_list(List<Task> list) {
        return new FilteredTasks(list);
    }
    
    // The main logic of the filtering is here. We call this function
    // upon the last's iteration result, operating over the results of the last
    // step.
    public FilteredTasks applyFilter(FilterType type, String... args) throws IllegalStateException {
        // Initialize the object so it's not null
        List<Task> afterFilterTasks = new ArrayList<Task>();
        
        // Every filter needs at least 1 argument. We can't operate if that one
        // argument is not present
        if (args.length < 1)
            throw new IllegalStateException("Filter needs at least one argument");
        
        // Now, two of the filter types need two arguments. So we check that
        if ((type == FilterType.CREATION_DATE || type == FilterType.COMPLETION_DATE) && args.length < 2)
            throw new IllegalStateException("CREATION_DATE and COMPLETION_DATE need at least two arguments");
        
        // Depending of the FilterType that the method receives, we apply some
        // filtering logic
        switch (type) {
            case TITLE_CONTAINS:
                for (var task : tasks) {
                    if (task.getTitle().contains(args[0]))
                        afterFilterTasks.add(task);
                }
                break;
            case DESC_CONTAINS:
                for (var task : tasks) {
                    if (task.getDescription().contains(args[0]))
                        afterFilterTasks.add(task);
                }
                break;
            case PRIORITY:
                // A variable holding the integer value of the priority
                Integer intPriority;
                
                // The parsing can throw an exception. So we catch that so we can
                // manage it in our way
                try {
                    intPriority = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    throw new IllegalStateException("The Priority filter's argument must be an Integer. Value: " + args[0]);
                }
                
                for (var task : tasks) {
                    if (task.getPriority() == intPriority)
                        afterFilterTasks.add(task);
                }
                break;
            case COMPLETION_DATE:
                // TODO: Support this
                throw new UnsupportedOperationException("Filter type not supported: completion date");
            case CREATION_DATE:
                // TODO: Support this too
                throw new UnsupportedOperationException("Filter type not supported: completion date");
        }
        
        // When the specified filter has been applied, we return the result so it
        // can be further processed
        return updatedEntity(afterFilterTasks);
    }
            
    public static enum FilterType {
        TITLE_CONTAINS,
        DESC_CONTAINS,
        PRIORITY,
        COMPLETION_DATE,
        CREATION_DATE
    }
}
