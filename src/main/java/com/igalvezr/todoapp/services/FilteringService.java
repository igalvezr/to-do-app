/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igalvezr.todoapp.services;

import com.igalvezr.todoapp.entities.Task;
import com.igalvezr.todoapp.filtering.FilteredTasks;
import static com.igalvezr.todoapp.filtering.FilteredTasks.FilterType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author ivan
 */
@Service
public class FilteringService {
    public Map<FilterType, String[]> getFiltertypeMappings(
            String title_contains,
            String desc_contains,
            String priority,
            String desired_completion_start,
            String desired_completion_end,
            String creation_date_start,
            String creation_date_end
    ) {
        HashMap<FilterType, String[]> filterMap = new HashMap<FilterType, String[]>();
        String[] params = {
            title_contains,
            desc_contains,
            priority,
            desired_completion_start,
            desired_completion_end,
            creation_date_start,
            creation_date_end
        };
        
        filterMap.put(FilterType.TITLE_CONTAINS, new String[]{params[0]});
        filterMap.put(FilterType.DESC_CONTAINS, new String[]{params[1]});
        filterMap.put(FilterType.PRIORITY, new String[]{params[2]});
        
        filterMap.put(FilterType.COMPLETION_DATE, new String[]{params[3], params[4]});
        filterMap.put(FilterType.CREATION_DATE, new String[]{params[5], params[6]});
        
        return filterMap;
    }

    public List<Task> applyFilters(Map<FilterType, String[]> filterParamsMap, List<Task> rawTasks) throws IllegalStateException {
        Iterator<FilterType> filterList = filterParamsMap.keySet().iterator();
        
        FilteredTasks filteringEntity = FilteredTasks.of_list(rawTasks);
        
        try {
            for (int i=0; i < filterParamsMap.size(); i++) {
                var currentFilter = filterList.next();
                
                // Verify that the arguments exist
                if (filterParamsMap.get(currentFilter)[0] == null)
                    continue;
                
                //System.out.println("Filtering for " + currentFilter + " with args " + filterParamsMap.get(currentFilter)[0]);
                
                filteringEntity.applyFilter(currentFilter, filterParamsMap.get(currentFilter));
            }
        } catch (IllegalStateException e) {
            System.out.println("ERROR: exception while applying filters: " + e.getMessage());
            throw e;
        }
        
        
        return filteringEntity.to_list();
    }
}
