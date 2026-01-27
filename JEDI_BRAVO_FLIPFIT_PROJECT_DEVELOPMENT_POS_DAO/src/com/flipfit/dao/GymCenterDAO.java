package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import java.util.List;

/**
 * The Interface GymCenterDAO.
 * Defines operations for gym center management
 * 
 * @author JEDI-BRAVO
 */
public interface GymCenterDAO {
    
    /**
     * Insert a new gym center.
     *
     * @param center the gym center object
     * @return true if insertion successful, false otherwise
     */
    boolean insertGymCenter(GymCenter center);
    
    /**
     * Get gym center by ID.
     *
     * @param gymId the gym ID
     * @return the gym center object
     */
    GymCenter getGymCenterById(String gymId);
    
    /**
     * Get all gym centers for an owner.
     *
     * @param ownerId the owner ID
     * @return list of gym centers
     */
    List<GymCenter> getGymCentersByOwner(String ownerId);
    
    /**
     * Get all gym centers in a city.
     *
     * @param city the city name
     * @return list of gym centers
     */
    List<GymCenter> getGymCentersByCity(String city);
    
    /**
     * Get all approved gym centers.
     *
     * @return list of all approved gym centers
     */
    List<GymCenter> getAllGymCenters();
    
    /**
     * Update gym center details.
     *
     * @param center the gym center object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateGymCenter(GymCenter center);
    
    /**
     * Delete a gym center.
     *
     * @param gymId the gym ID
     * @return true if deletion successful, false otherwise
     */
    boolean deleteGymCenter(String gymId);
}
