package com.example.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Medecin extends User{
    private String cin ;
    private String inpe;
    private String ppr;
    private Boolean estMedcinESJ;
    private Boolean estGeneraliste;
    private String specialite;

}
