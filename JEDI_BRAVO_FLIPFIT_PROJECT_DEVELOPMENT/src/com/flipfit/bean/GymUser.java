package com.flipfit.bean;

import com.flipfit.enums.Role;

public class GymUser {
    private String userId;     // maps to source [cite: 81]
    private String name;       // maps to source [cite: 82]
    private String email;      // maps to source [cite: 83]
    private String password;   // maps to source [cite: 84]
    private Role role;         // maps to source [cite: 86]

    // Getters and Setters
    public String getUserId() { 
    	return userId; 
    }
    public void setUserId(String userId) { 
    	this.userId = userId; 
    }

    public String getName() { 
    	return name; 
    }
    public void setName(String name) { 
    	this.name = name; 
    }

    public String getEmail() { 
    	return email; 
    }
    public void setEmail(String email) { 
    	this.email = email; 
    }

    public String getPassword() { 
    	return password; 
    }
    public void setPassword(String password) { 
    	this.password = password; 
    }

    public Role getRole() { 
    	return role; 
    }
    public void setRole(Role role) { 
    	this.role = role; 
    }
}