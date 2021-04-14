package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hierarchy")
public class Link {
	@Id
	@Column(name = "employee_id")
	private int employee_id;
	
	@Column(name="parent_id")
	private int parent_id;

	public Link() { }
	
	public Link(int employee_id, int parent_id)
	{
		this.employee_id = employee_id;
		this.parent_id = parent_id;
	}
}
