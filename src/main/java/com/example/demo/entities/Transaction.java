package com.example.demo.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="transactions")
public class Transaction {
	@Id
	@Column(name="transaction_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="product_name")
	private String name;
	
	@Column(name="product_price")
	private int price;

	@Column(name="client_id")
	private int clientId;
	
	@Column(name="seller_id")
	private int sellerId;
	
	@Column(name="datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDateTime;

	@Column(name="username_employee")
	private String usernameEmployee;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public void setCreationDateTime(Long ms) {
		if(creationDateTime==null) {
			this.setCreationDateTime(new Date());
		}
		this.creationDateTime.setTime(ms);
	}

	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getUsernameEmployee() {
		return usernameEmployee;
	}

	public void setUsernameEmployee(String usernameEmployee) {
		this.usernameEmployee = usernameEmployee;
	}
}
