package backend.authModule.service;

import backend.authModule.dto.MedecinResponseDTO;
import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;
import backend.authModule.exception.MedecinNotFoundException;

import java.util.Optional;

public interface MedecinService extends ConfirmeMailService<Medecin> {
    MedecinResponseDTO saveMecine(Medecin medecin) throws MedecinException;

    MedecinResponseDTO getMedecinById(Long id) throws MedecinNotFoundException;

    void updateMedecin(Long id, Medecin medecin) throws MedecinNotFoundException, MedecinException;

    void deleteMedecin(Long id) throws MedecinNotFoundException, MedecinException;
}
