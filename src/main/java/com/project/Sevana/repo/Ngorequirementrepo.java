package com.project.Sevana.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.Sevana.model.Requirements;
import com.project.Sevana.model.Users;


@Repository
public interface Ngorequirementrepo extends JpaRepository<Requirements,Long>{
	
	@Query(value = "SELECT * FROM requirements WHERE user_id = ?1", nativeQuery = true)
	List<Requirements> findByUser_Userid(Long userid);
}
