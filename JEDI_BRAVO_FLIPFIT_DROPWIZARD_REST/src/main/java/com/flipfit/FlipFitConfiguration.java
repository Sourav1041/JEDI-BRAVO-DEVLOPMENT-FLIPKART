package com.flipfit;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration class for FlipFit Dropwizard application.
 * This class holds application-level configuration parameters.
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-28
 */
public class FlipFitConfiguration extends Configuration {
    
    @JsonProperty
    private String applicationName = "FlipFit REST API";
    
    @JsonProperty
    private String version = "1.0";
    
    /**
     * Gets the application name.
     * 
     * @return The application name
     */
    public String getApplicationName() {
        return applicationName;
    }
    
    /**
     * Sets the application name.
     * 
     * @param applicationName The application name to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    
    /**
     * Gets the application version.
     * 
     * @return The application version
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Sets the application version.
     * 
     * @param version The version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
