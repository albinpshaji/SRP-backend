package com.project.Sevana.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Sevana.model.Donations;
@Repository
public interface Donationrepo extends JpaRepository<Donations,Long>{
	List<Donations> findByDonorUserid(Long userid);
	List<Donations> findByRecipientUsername(String username);
}
