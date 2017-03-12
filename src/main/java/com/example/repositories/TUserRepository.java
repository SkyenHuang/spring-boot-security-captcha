package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.TUser;

@Repository
public interface TUserRepository extends JpaRepository<TUser, Integer> {
	public TUser findByUsername(String username);
}
