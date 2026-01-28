package com.flipfit.rest;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymSlot;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.impl.GymOwnerServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for gym owner operations.
 * Handles gym center management, slot creation, and owner profile viewing.
 * Uses for-each loops for iterations as per Java 8+ standards.
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-28
 */
@Path("/owner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerController {
    
    private final GymOwnerService gymOwnerService;
    
    /**
     * Constructor initializing gym owner service.
     */
    public GymOwnerController() {
        this.gymOwnerService = new GymOwnerServiceImpl();
    }
    
    /**
     * Add a new gym center.
     * 
     * @param centerData Map containing gym center details
     * @return Response with creation status
     */
    @POST
    @Path("/center/add")
    public Response addGymCenter(Map<String, String> centerData) {
        try {
            String gymId = centerData.get("gymId");
            String ownerId = centerData.get("ownerId");
            String gymName = centerData.get("gymName");
            String gymAddress = centerData.get("gymAddress");
            String city = centerData.get("city");
            String state = centerData.get("state");
            String pincode = centerData.get("pincode");
            String phoneNumber = centerData.get("phoneNumber");
            String email = centerData.get("email");
            int totalSlots = Integer.parseInt(centerData.get("totalSlots"));
            
            gymOwnerService.addGymCenter(gymId, ownerId, gymName, gymAddress, city, state, 
                                        pincode, phoneNumber, email, totalSlots);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Gym center added successfully. Pending admin approval.");
            response.put("gymId", gymId);
            
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add gym center: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * View all gym centers owned by a specific owner.
     * 
     * @param ownerId The owner ID
     * @return Response with list of gym centers
     */
    @GET
    @Path("/centers/{ownerId}")
    public Response viewMyGymCenters(@PathParam("ownerId") String ownerId) {
        try {
            List<GymCenter> centers = gymOwnerService.viewMyGymCenters(ownerId);
            return Response.ok(centers).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch gym centers: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Add gym slots for a specific gym center.
     * 
     * @param slotData Map containing slot details
     * @return Response with creation status
     */
    @POST
    @Path("/slot/add")
    public Response addGymSlot(Map<String, String> slotData) {
        try {
            String slotId = slotData.get("slotId");
            String gymId = slotData.get("gymId");
            LocalTime startTime = LocalTime.parse(slotData.get("startTime"));
            LocalTime endTime = LocalTime.parse(slotData.get("endTime"));
            int totalSeats = Integer.parseInt(slotData.get("totalSeats"));
            double price = Double.parseDouble(slotData.get("price"));
            
            gymOwnerService.addGymSlot(slotId, gymId, startTime, endTime, totalSeats, price);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Gym slot added successfully");
            response.put("slotId", slotId);
            response.put("gymId", gymId);
            
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add gym slot: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * View all slots for a specific gym center.
     * 
     * @param gymId The gym center ID
     * @return Response with list of slots
     */
    @GET
    @Path("/slots/{gymId}")
    public Response viewGymSlots(@PathParam("gymId") String gymId) {
        try {
            List<GymSlot> slots = gymOwnerService.viewGymSlots(gymId);
            return Response.ok(slots).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch gym slots: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get gym owner profile and approval status.
     * 
     * @param ownerId The owner ID
     * @return Response with owner details
     */
    @GET
    @Path("/profile/{ownerId}")
    public Response getOwnerProfile(@PathParam("ownerId") String ownerId) {
        try {
            GymOwner owner = gymOwnerService.getGymOwnerById(ownerId);
            
            if (owner != null) {
                return Response.ok(owner).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Owner not found");
                return Response.status(Response.Status.NOT_FOUND).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch owner profile: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Update gym center details.
     * 
     * @param gymId The gym center ID
     * @param centerData Map containing updated center details
     * @return Response with update status
     */
    @PUT
    @Path("/center/{gymId}")
    public Response updateGymCenter(@PathParam("gymId") String gymId, 
                                    Map<String, String> centerData) {
        try {
            String gymName = centerData.get("gymName");
            String gymAddress = centerData.get("gymAddress");
            String phoneNumber = centerData.get("phoneNumber");
            String email = centerData.get("email");
            
            gymOwnerService.updateGymCenter(gymId, gymName, gymAddress, phoneNumber, email);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Gym center updated successfully");
            response.put("gymId", gymId);
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update gym center: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}
