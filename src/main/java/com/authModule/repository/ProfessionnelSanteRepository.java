package com.authModule.repository;

import com.authModule.entities.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionnelSanteRepository extends JpaRepository<ProfessionnelSante,Long> {
}
