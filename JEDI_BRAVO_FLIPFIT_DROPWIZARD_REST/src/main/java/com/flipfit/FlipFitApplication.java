package com.flipfit;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.flipfit.rest.GymCustomerController;
import com.flipfit.rest.GymOwnerController;
import com.flipfit.rest.AdminController;
import com.flipfit.rest.AuthController;

/**
 * Main application class for FlipFit Dropwizard REST API.
 * This class initializes and runs the Dropwizard application.
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-28
 */
public class FlipFitApplication extends Application<FlipFitConfiguration> {

    /**
     * Main method to run the application.
     * 
     * @param args Command line arguments
     * @throws Exception if application fails to start
     */
    public static void main(String[] args) throws Exception {
        new FlipFitApplication().run(args);
    }

    /**
     * Gets the application name.
     * 
     * @return The name of the application
     */
    @Override
    public String getName() {
        return "FlipFit REST API";
    }

    /**
     * Initializes the application.
     * 
     * @param bootstrap The bootstrap instance
     */
    @Override
    public void initialize(Bootstrap<FlipFitConfiguration> bootstrap) {
        // Add any bootstrapping configurations here
    }

    /**
     * Runs the application and registers REST controllers.
     * 
     * @param configuration The configuration instance
     * @param environment The environment instance
     */
    @Override
    public void run(FlipFitConfiguration configuration, Environment environment) {
        // Register REST controllers
        final AuthController authController = new AuthController();
        final GymCustomerController customerController = new GymCustomerController();
        final GymOwnerController ownerController = new GymOwnerController();
        final AdminController adminController = new AdminController();
        
        environment.jersey().register(authController);
        environment.jersey().register(customerController);
        environment.jersey().register(ownerController);
        environment.jersey().register(adminController);
        
        System.out.println("FlipFit REST API Started Successfully!");
        System.out.println("Access the API at: http://localhost:8080");
    }
}
