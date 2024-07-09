package backend.authModule.service;

import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;

public interface MedecinService extends ConfirmeMailService<Medecin> {
    Medecin saveMecine(Medecin medecin) throws MedecinException;
}
