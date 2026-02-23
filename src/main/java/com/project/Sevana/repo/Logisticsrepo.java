package com.project.Sevana.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import com.project.Sevana.model.Logistics;

@Repository
public interface Logisticsrepo extends JpaRepository<Logistics, Long> {

	@Query(value = "SELECT l.* FROM logistics l JOIN donations d ON l.donationid = d.donationid JOIN users u ON d.recipient_id = u.userid WHERE u.username = :username", nativeQuery = true)
	List<Logistics> findByDonationRecipientUsername(@Param("username") String username);

	@Modifying
	@Transactional
	@Query(value = "UPDATE logistics SET deliverystatus = :status, updated_at = NOW() WHERE donationid = :donId", nativeQuery = true)
	int updateDeliveryStatusByDonationId(@Param("donId") Long donId, @Param("status") String status);
}