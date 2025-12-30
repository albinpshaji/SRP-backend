package com.project.Sevana.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.Sevana.model.Donations;

import jakarta.transaction.Transactional;
@Repository
public interface Donationrepo extends JpaRepository<Donations,Long>{
	List<Donations> findByDonorUserid(Long userid);
	
	List<Donations> findByRecipientUsername(String username);
	
	@Modifying
	@Transactional
	@Query(value="update donations set status=:status where donationid=:id",nativeQuery=true)
	int updatestatus(@Param("id")Long id,@Param("status")String status);
	
	@Query(value="select * from donations where donationid= :id and recipient_id= :nid",nativeQuery=true)
	Donations getCorrectNgo(@Param("id")Long id,@Param("nid")Long nid);
}
