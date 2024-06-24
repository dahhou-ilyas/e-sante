package com.example.backend.repository;

import com.example.backend.entities.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionnelSanteRepository extends JpaRepository<ProfessionnelSante,Long> {
}
