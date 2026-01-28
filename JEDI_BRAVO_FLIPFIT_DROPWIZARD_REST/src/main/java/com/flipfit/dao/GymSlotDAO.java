package com.flipfit.dao;

import com.flipfit.bean.GymSlot;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface GymSlotDAO.
 * Defines operations for gym slot management
 * Note: Slots are time templates without dates. Dates are tracked in Booking table.
 * 
 * @author JEDI-BRAVO
 * @ClassName GymSlotDAO
 */
public interface GymSlotDAO {
    
    /**
     * Insert a new gym slot.
     *
     * @param slot the gym slot object
     * @return true if insertion successful, false otherwise
     */
    boolean insertSlot(GymSlot slot);
    
    /**
     * Get slot by ID.
     *
     * @param slotId the slot ID
     * @return the gym slot object
     */
    GymSlot getSlotById(String slotId);
    
    /**
     * Get all slots for a specific gym.
     *
     * @param gymId the gym ID
     * @return list of gym slots
     */
    List<GymSlot> getSlotsByCenter(String gymId);
    
    /**
     * Check if seats are available in a slot.
     *
     * @param slotId the slot ID
     * @return true if seats available, false otherwise
     */
    boolean checkSlotAvailability(String slotId);
    
    /**
     * Update available seats in a slot.
     *
     * @param slotId the slot ID
     * @param seatsChange the number of seats to add (positive) or remove (negative)
     * @return true if update successful, false otherwise
     */
    boolean updateAvailableSeats(String slotId, int seatsChange);
    
    /**
     * Get all available slots across all gyms.
     *
     * @return list of available gym slots
     */
    List<GymSlot> getAllAvailableSlots();
    
    /**
     * Get slots by city.
     *
     * @param city the city name
     * @return list of gym slots
     */
    List<GymSlot> getSlotsByCity(String city);
    
    /**
     * Delete a slot.
     *
     * @param slotId the slot ID
     * @return true if deletion successful, false otherwise
     */
    boolean deleteSlot(String slotId);
    
    /**
     * Update slot details.
     *
     * @param slot the gym slot object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateSlot(GymSlot slot);
    
    /**
     * Get slot capacity.
     *
     * @param slotId the slot ID
     * @return the capacity of the slot
     */
    int getSlotCapacity(String slotId);
    
    /**
     * Get available seats count for a slot.
     *
     * @param slotId the slot ID
     * @return the number of available seats
     */
    int getAvailableSeats(String slotId);
}
