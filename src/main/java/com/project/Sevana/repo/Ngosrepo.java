package com.project.Sevana.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Sevana.model.Ngos;

@Repository
public interface Ngosrepo extends JpaRepository<Ngos,Long>{

}
