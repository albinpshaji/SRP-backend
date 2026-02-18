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

	//there is a new constructor in users which does not accept images so that the login should work
	@Query("SELECT new com.project.Sevana.model.Users(u.userid, u.username, u.password, u.role, u.phone, u.location, u.isverified) FROM Users u WHERE u.username = :username")
	Users findByUsername(String username);
	
	@Query("SELECT new com.project.Sevana.model.Users(u.userid, u.username, u.password, u.role, u.phone, u.location, u.isverified) FROM Users u WHERE u.role = :role")
	List<Users> findByRole(String role);
	
	@Query("SELECT new com.project.Sevana.model.Users(u.userid, u.username, u.password, u.role, u.phone, u.location, u.isverified) " +
		       "FROM Users u WHERE u.role IN (:role1, :role2)")
	List<Users> findByRoles(@Param("role1")String role1,@Param("role2")String role2);
	
	@Modifying
	@Transactional
	@Query(value="update users set isverified =:isverify,role ='NGO' where userid=:id",nativeQuery=true)
	int verifyuser(@Param("id")Long id,@Param("isverify")String isverify);
	
	@Modifying
	@Transactional
	@Query(value="update users set isverified =:isverify,role ='NV_NGO' where userid=:id",nativeQuery=true)
	int rejectuser(@Param("id")Long id,@Param("isverify")String isverify);

	@Query(value = """
			select * from users where(
			lower(username) like lower(concat('%',:keyword,'%')) or
			lower(location) like lower(concat('%',:keyword,'%'))) and role='NGO'
			""", nativeQuery = true)
	List<Users> searchbyanything(@Param("keyword") String keyword);
	
	
}