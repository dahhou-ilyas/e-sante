package com.authModule.service;

import com.authModule.entities.Medecin;
import com.authModule.exception.MedecinException;

public interface MedecinService {
    Medecin saveMecine(Medecin medecin) throws MedecinException;
}
