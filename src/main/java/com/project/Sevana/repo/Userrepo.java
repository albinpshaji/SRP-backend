package com.project.Sevana.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import com.project.Sevana.model.Users;

@Repository
public interface Userrepo extends JpaRepository<Users,Long>{

	Users findByUsername(String username);
	
	List<Users> findByRole(String role);
	
	@Modifying
	@Transactional
	@Query(value="update users set isverified =:isverify,role ='NGO' where userid=:id",nativeQuery=true)
	int verifyuser(@Param("id")Long id,@Param("isverify")Boolean isverify);
	
	@Modifying
	@Transactional
	@Query(value="update users set isverified =:isverify,role ='NV_NGO' where userid=:id",nativeQuery=true)
	int rejectuser(@Param("id")Long id,@Param("isverify")Boolean isverify);
	
}
