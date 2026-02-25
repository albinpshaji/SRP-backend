package com.project.Sevana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.project.Sevana.DTO.LogisticsDTO;
import com.project.Sevana.DTO.LogisticsResponseDTO;
import com.project.Sevana.model.Logistics;
import com.project.Sevana.model.Donations;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Logisticsrepo;
import com.project.Sevana.repo.Userrepo;
import java.util.stream.Collectors;

@Service
public class Logisticsservice {

    private final Logisticsrepo logisticsrepo;
    private final Userrepo userrepo;

    @Autowired
    public Logisticsservice(Logisticsrepo logisticsrepo, Userrepo userrepo) {
        this.logisticsrepo = logisticsrepo;
        this.userrepo = userrepo;
    }

    Users getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userrepo.findByUsername(username);
    }

    
    @Transactional
    public List<LogisticsResponseDTO> getLogisticsForNgo() {
        String username = getAuthenticatedUser().getUsername();
        List<Logistics> logisticsList = logisticsrepo.findByDonationRecipientUsername(username);
        
        return logisticsList.stream().map(logistics -> {
            LogisticsResponseDTO dto = new LogisticsResponseDTO();
            dto.setLogisticsid(logistics.getLogisticsid());
            dto.setMethod(logistics.getMethod());
            dto.setAddressLine(logistics.getAddressLine());
            dto.setPickupdate(logistics.getPickupdate());
            dto.setDeliverystatus(logistics.getDeliverystatus());
            dto.setCreatedAt(logistics.getCreatedAt());
            
            if (logistics.getUpdatedBy() != null) {
                dto.setUpdatedByUsername(logistics.getUpdatedBy().getUsername());
            }
            
            Donations donation = logistics.getDonation();
            if (donation != null) {
                dto.setDonationId(donation.getDonationid());
                dto.setDonationTitle(donation.getTitle());
                dto.setCategory(donation.getCategory());
                if (donation.getDonor() != null) {
                    dto.setDonorUsername(donation.getDonor().getUsername());
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }

    
    @Transactional
    public String updateDeliveryStatus(Long id, LogisticsDTO dto) {
        Logistics logistics = logisticsrepo.findById(id).orElse(null);
        if (logistics == null) return "notfound";

        Users updatedBy = getAuthenticatedUser();

        if (dto.getDeliverystatus() != null) {
            logistics.setDeliverystatus(dto.getDeliverystatus().toUpperCase());
        }
        logistics.setUpdatedBy(updatedBy);
        logisticsrepo.save(logistics);
        return "success";
    }
}
