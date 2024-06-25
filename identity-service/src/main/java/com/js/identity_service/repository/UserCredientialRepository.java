package com.js.identity_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.js.identity_service.entity.UserCredential;

public interface UserCredientialRepository extends JpaRepository<UserCredential, Integer>{

	Optional<UserCredential> findByName(String username);

}
