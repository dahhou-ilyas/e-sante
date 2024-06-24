package backend.authModule.service;

import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;

public interface MedecinService {
    Medecin saveMecine(Medecin medecin) throws MedecinException;
}
