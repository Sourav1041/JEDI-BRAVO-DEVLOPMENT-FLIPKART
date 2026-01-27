
package com.flipfit.dao;

/**
 * 
 */


import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import java.time.LocalTime;
import java.util.List;

public interface GymCentreDAO {
    // Persists new gym center information 
    void save(GymCenter centre);
    
    // Returns gyms in a specific city for the Customer Service 
    List<GymCenter> findByCity(String city);
    
    // Resolves the specific GymSlot based on user-requested times
    GymSlot getSlotByTime(String centreId, LocalTime startTime, LocalTime endTime);
}
