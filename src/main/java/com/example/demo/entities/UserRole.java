package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users_roles")
public class UserRole {

	@Id
	@Column(name = "user_id")
	private int user_id;
	
	@Column(name = "role_id")
	private int role_id;

	public UserRole() { }
	
	public UserRole(int user_id, int role_id)
	{
		this.user_id = user_id;
		this.role_id = role_id;
	}
}
