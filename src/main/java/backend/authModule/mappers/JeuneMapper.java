package backend.authModule.mappers;

import backend.authModule.dto.JeuneDTO;
import backend.authModule.entities.Jeune;
import org.springframework.stereotype.Service;

@Service
public class JeuneMapper {
    public static JeuneDTO fromJeune(Jeune jeune) {
        JeuneDTO dto = new JeuneDTO();
        dto.setId(jeune.getId());
        dto.setNom(jeune.getAppUser().getNom());
        dto.setPrenom(jeune.getAppUser().getPrenom());
        dto.setMail(jeune.getAppUser().getMail());
        dto.setNumTele(jeune.getAppUser().getNumTele());
        dto.setSexe(jeune.getSexe());
        dto.setDateNaissance(jeune.getDateNaissance());
        dto.setAge(jeune.getAge());
        dto.setIdentifiantPatient(jeune.getIdentifiantPatient());
        dto.setScolarise(jeune.isScolarise());
        dto.setCin(jeune.getCin());
        dto.setIsConfirmed(jeune.getIsConfirmed());
        dto.setIsFirstAuth(jeune.getIsFirstAuth());
        dto.setRole(jeune.getROLE());
        return dto;
    }
}
