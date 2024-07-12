package backend.authModule.mappers;

import backend.authModule.dto.MedecinResponseDTO;
import backend.authModule.entities.Medecin;
import org.springframework.stereotype.Service;

@Service
public class MedecineMapper {

    public MedecinResponseDTO fromMedcine(Medecin medecin){
        MedecinResponseDTO medecinResponseDTO=new MedecinResponseDTO();
        medecinResponseDTO.setId(medecin.getId());
        medecinResponseDTO.setPrenom(medecin.getAppUser().getPrenom());
        medecinResponseDTO.setNom(medecin.getAppUser().getNom());
        medecinResponseDTO.setMail(medecin.getAppUser().getMail());
        medecinResponseDTO.setCin(medecin.getCin());
        medecinResponseDTO.setInpe(medecin.getInpe());
        medecinResponseDTO.setPpr(medecin.getPpr());
        medecinResponseDTO.setEstMedcinESJ(medecin.getEstMedcinESJ());
        medecinResponseDTO.setEstGeneraliste(medecin.getEstGeneraliste());
        medecinResponseDTO.setSpecialite(medecin.getSpecialite());
        return medecinResponseDTO;
    }

}
