package com.igalvezr.todoapp.services;

import com.igalvezr.todoapp.entities.Task;
import com.igalvezr.todoapp.filtering.FilteredTasks;
import static com.igalvezr.todoapp.filtering.FilteredTasks.FilterType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author ivan
 * 
 * This service has the purpose of managing the tedious logic of the filtering
 * mechanism.
 */
@Service
public class FilteringService {
    // This method maps the parameters provided to the controller to the
    // corresponding filter type. This with the intention of condensing all 
    // filtering related data into one data structure, making it's managing
    // easier.
    // It uses a HashMap to map a FilterType to an array of strings. We handle
    // this with an array of strings because the filters for creation date and
    // desired completion need two values. So, for avoiding using multiple
    // objects to the same data, we store one argument for the filters that
    // require one, and two for the ones that require two.
    
    public Map<FilterType, String[]> getFiltertypeMappings(
            String title_contains,
            String desc_contains,
            String priority,
            String desired_completion_start,
            String desired_completion_end,
            String creation_date_start,
            String creation_date_end
    ) {
        // Instantiation of an empty HasMap
        HashMap<FilterType, String[]> filterMap = new HashMap<FilterType, String[]>();
        
        // The params' values are condensed into one array, so handling them is
        // easier. In this case, we are binding the position in the array of a given
        // param to it's corresponding filter type, but it can't be avoided as far as
        // I know currently.
        String[] params = {
            title_contains,
            desc_contains,
            priority,
            desired_completion_start,
            desired_completion_end,
            creation_date_start,
            creation_date_end
        };
        
        // We manually map every filter type with it's parameters.
        filterMap.put(FilterType.TITLE_CONTAINS, new String[]{params[0]});
        filterMap.put(FilterType.DESC_CONTAINS, new String[]{params[1]});
        filterMap.put(FilterType.PRIORITY, new String[]{params[2]});
        
        filterMap.put(FilterType.COMPLETION_DATE, new String[]{params[3], params[4]});
        filterMap.put(FilterType.CREATION_DATE, new String[]{params[5], params[6]});
        
        // Finally, we return the completed map
        return filterMap;
    }

    // This method handles the actual logic of calling FilteredTasks.applyFilter
    // for every filter.
    public List<Task> applyFilters(Map<FilterType, String[]> filterParamsMap, List<Task> rawTasks) throws IllegalStateException {
        // In order to use the filter types, we need to transform the keys of
        // the map into a "list". But, since the HashMap returns its keys as a
        // set, we transform that set into an iterator, and use that instead of an
        // actual list
        Iterator<FilterType> filterList = filterParamsMap.keySet().iterator();
        
        // The FilteredTasks entity used to apply all the filters
        FilteredTasks filteringEntity = FilteredTasks.of_list(rawTasks);
        
        // This try block catches all the possible exceptions during the
        // filter application process, so it doesn't break the server with a 500
        try {
            // This loop calls applyFilter for every filter in the iterator
            for (int i=0; i < filterParamsMap.size(); i++) {
                // We exctract the current FilterType value so we can pass that 
                // to the method. Since we don't have an actual list, we need to
                // use the .next() method on the iterator
                var currentFilter = filterList.next();
                
                // Verify that the arguments exist. If not, this filter was not
                // included in the request, so we don't apply it.
                if (filterParamsMap.get(currentFilter)[0] == null)
                    continue;
                
                //System.out.println("Filtering for " + currentFilter + " with args " + filterParamsMap.get(currentFilter)[0]);
                
                // We pass the current FilterType and the parameters for that 
                // stored in the map
                filteringEntity.applyFilter(currentFilter, filterParamsMap.get(currentFilter));
            }
        } catch (IllegalStateException e) {
            // We log the error, but still throw it as a way of informing the calling
            // controller that somenthing went wrong
            System.out.println("ERROR: exception while applying filters: " + e.getMessage());
            throw e;
        }
        
        // Finally, we return the result of the filtering
        return filteringEntity.to_list();
    }
}
