package com.project.Sevana.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Sevana.model.Users;

@Repository
public interface Userrepo extends JpaRepository<Users,Long>{
	
}
