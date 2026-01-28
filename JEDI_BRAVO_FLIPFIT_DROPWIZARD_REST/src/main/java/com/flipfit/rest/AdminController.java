package com.flipfit.rest;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymUser;
import com.flipfit.business.AdminService;
import com.flipfit.business.impl.AdminServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for admin operations.
 * Handles approval management for gym owners and gym centers.
 * Uses Java 8+ Stream API for filtering approved/not approved entities.
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-28
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminController {
    
    private final AdminService adminService;
    
    /**
     * Constructor initializing admin service.
     */
    public AdminController() {
        this.adminService = new AdminServiceImpl();
    }
    
    /**
     * Get all pending gym owner approval requests.
     * Uses Stream API to filter not approved gym owners.
     * 
     * @return Response with list of pending gym owners
     */
    @GET
    @Path("/owners/pending")
    public Response getPendingGymOwners() {
        try {
            List<GymOwner> allOwners = adminService.viewAllGymOwners();
            
            // Using Stream API with lambda expressions to filter not approved owners
            List<GymOwner> pendingOwners = allOwners.stream()
                .filter(owner -> !owner.isApproved())
                .collect(Collectors.toList());
            
            return Response.ok(pendingOwners).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch pending gym owners: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all approved gym owners.
     * Uses Stream API to filter approved gym owners.
     * 
     * @return Response with list of approved gym owners
     */
    @GET
    @Path("/owners/approved")
    public Response getApprovedGymOwners() {
        try {
            List<GymOwner> allOwners = adminService.viewAllGymOwners();
            
            // Using Stream API with lambda expressions to filter approved owners
            List<GymOwner> approvedOwners = allOwners.stream()
                .filter(GymOwner::isApproved)
                .collect(Collectors.toList());
            
            return Response.ok(approvedOwners).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch approved gym owners: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all pending gym center approval requests.
     * Uses Stream API to filter not approved gym centers.
     * 
     * @return Response with list of pending gym centers
     */
    @GET
    @Path("/centers/pending")
    public Response getPendingGymCenters() {
        try {
            List<GymCenter> allCenters = adminService.viewAllGymCenters();
            
            // Using Stream API with lambda expressions to filter not approved centers
            List<GymCenter> pendingCenters = allCenters.stream()
                .filter(center -> !center.isApproved())
                .collect(Collectors.toList());
            
            return Response.ok(pendingCenters).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch pending gym centers: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all approved gym centers.
     * Uses Stream API to filter approved gym centers.
     * 
     * @return Response with list of approved gym centers
     */
    @GET
    @Path("/centers/approved")
    public Response getApprovedGymCenters() {
        try {
            List<GymCenter> allCenters = adminService.viewAllGymCenters();
            
            // Using Stream API with lambda expressions to filter approved centers
            List<GymCenter> approvedCenters = allCenters.stream()
                .filter(GymCenter::isApproved)
                .collect(Collectors.toList());
            
            return Response.ok(approvedCenters).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch approved gym centers: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Approve a gym owner.
     * 
     * @param ownerId The owner ID to approve
     * @return Response with approval status
     */
    @PUT
    @Path("/owner/approve/{ownerId}")
    public Response approveGymOwner(@PathParam("ownerId") String ownerId) {
        try {
            boolean success = adminService.approveGymOwner(ownerId);
            
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Gym owner approved successfully");
                response.put("ownerId", ownerId);
                return Response.ok(response).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to approve gym owner");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Approval failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Approve a gym center.
     * 
     * @param gymId The gym center ID to approve
     * @return Response with approval status
     */
    @PUT
    @Path("/center/approve/{gymId}")
    public Response approveGymCenter(@PathParam("gymId") String gymId) {
        try {
            boolean success = adminService.approveGymCenter(gymId);
            
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Gym center approved successfully");
                response.put("gymId", gymId);
                return Response.ok(response).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to approve gym center");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Approval failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Reject a gym owner.
     * 
     * @param ownerId The owner ID to reject
     * @return Response with rejection status
     */
    @DELETE
    @Path("/owner/reject/{ownerId}")
    public Response rejectGymOwner(@PathParam("ownerId") String ownerId) {
        try {
            boolean success = adminService.rejectGymOwner(ownerId);
            
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Gym owner rejected and removed");
                response.put("ownerId", ownerId);
                return Response.ok(response).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to reject gym owner");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Rejection failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Reject a gym center.
     * 
     * @param gymId The gym center ID to reject
     * @return Response with rejection status
     */
    @DELETE
    @Path("/center/reject/{gymId}")
    public Response rejectGymCenter(@PathParam("gymId") String gymId) {
        try {
            boolean success = adminService.rejectGymCenter(gymId);
            
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Gym center rejected and removed");
                response.put("gymId", gymId);
                return Response.ok(response).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to reject gym center");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Rejection failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all users in the system.
     * 
     * @return Response with list of all users
     */
    @GET
    @Path("/users")
    public Response getAllUsers() {
        try {
            List<GymUser> users = adminService.viewAllUsers();
            return Response.ok(users).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch users: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all gym owners (both approved and pending).
     * 
     * @return Response with list of all gym owners
     */
    @GET
    @Path("/owners")
    public Response getAllGymOwners() {
        try {
            List<GymOwner> owners = adminService.viewAllGymOwners();
            return Response.ok(owners).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch gym owners: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all gym centers (both approved and pending).
     * 
     * @return Response with list of all gym centers
     */
    @GET
    @Path("/centers")
    public Response getAllGymCenters() {
        try {
            List<GymCenter> centers = adminService.viewAllGymCenters();
            return Response.ok(centers).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch gym centers: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}
