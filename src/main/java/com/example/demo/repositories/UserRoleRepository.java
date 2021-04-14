package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	
}