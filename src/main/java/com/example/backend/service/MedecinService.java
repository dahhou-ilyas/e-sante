package com.example.backend.service;

import com.example.backend.entities.Medecin;
import com.example.backend.exception.MedecinException;

public interface MedecinService {
    Medecin saveMecine(Medecin medecin) throws MedecinException;
}
