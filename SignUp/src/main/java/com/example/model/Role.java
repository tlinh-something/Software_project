package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Role")
public class Role {
    @Id
    @Column(name = "roleID")
    private int roleID;

    @Column(name = "roleName")
    private String roleName;

	public Role(String roleName) {
		super();
		this.roleName = roleName;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

    // getters and setters
    
}