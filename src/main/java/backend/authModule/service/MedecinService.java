package backend.authModule.service;

import backend.authModule.dto.MedecinResponseDTO;
import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;
import backend.authModule.exception.MedecinNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MedecinService extends ConfirmeMailService<Medecin> {
    MedecinResponseDTO saveMecine(Medecin medecin) throws MedecinException;

    MedecinResponseDTO getMedecinById(Long id) throws MedecinNotFoundException;

    MedecinResponseDTO updateMedecinPartial(Long id, Map<String, Object> updates) throws MedecinNotFoundException;

    List<MedecinResponseDTO> getAllMedecins();

    void deleteMedecin(Long id) throws MedecinNotFoundException, MedecinException;
}
