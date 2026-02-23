package com.project.Sevana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.project.Sevana.DTO.LogisticsDTO;
import com.project.Sevana.model.Logistics;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Logisticsrepo;
import com.project.Sevana.repo.Userrepo;

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
    public List<Logistics> getLogisticsForNgo() {
        String username = getAuthenticatedUser().getUsername();
        return logisticsrepo.findByDonationRecipientUsername(username);
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
