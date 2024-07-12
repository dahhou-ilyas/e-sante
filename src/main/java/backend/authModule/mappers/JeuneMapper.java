package backend.authModule.mappers;

import backend.authModule.dto.JeuneDTO;
import backend.authModule.dto.JeuneNonScolariseDto;
import backend.authModule.dto.JeuneScolariseDto;
import backend.authModule.entities.AppUser;
import backend.authModule.entities.Jeune;
import backend.authModule.entities.JeuneNonScolarise;
import backend.authModule.entities.JeuneScolarise;
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
    public JeuneScolariseDto fromJeuneScolarise(JeuneScolarise jeuneScolarise){
        JeuneScolariseDto jeuneScolariseDto = new JeuneScolariseDto();
        AppUser appUser = jeuneScolarise.getAppUser();
        jeuneScolariseDto.setId(jeuneScolarise.getId());
        jeuneScolariseDto.setPrenom(appUser.getPrenom());
        jeuneScolariseDto.setNom(appUser.getNom());
        jeuneScolariseDto.setMail(appUser.getMail());
        jeuneScolariseDto.setNumTele(appUser.getNumTele());
        jeuneScolariseDto.setSexe(jeuneScolarise.getSexe());
        jeuneScolariseDto.setDateNaissance(jeuneScolarise.getDateNaissance());
        jeuneScolariseDto.setAge(jeuneScolarise.getAge());
        jeuneScolariseDto.setIdentifiantPatient(jeuneScolarise.getIdentifiantPatient());
        jeuneScolariseDto.setCin(jeuneScolarise.getCin());
        jeuneScolariseDto.setConfirmed(jeuneScolarise.getIsConfirmed());
        jeuneScolariseDto.setFirstAuth(jeuneScolarise.getIsFirstAuth());
        jeuneScolariseDto.setROLE(jeuneScolarise.getROLE());
        jeuneScolariseDto.setNiveauEtudesActuel(jeuneScolarise.getNiveauEtudesActuel());
        jeuneScolariseDto.setCNE(jeuneScolarise.getCNE());
        jeuneScolariseDto.setCodeMASSAR(jeuneScolarise.getCodeMASSAR());

        return jeuneScolariseDto;
    }
    public JeuneNonScolariseDto fromJeuneNonScolarise(JeuneNonScolarise jeuneNonScolarise) {
        JeuneNonScolariseDto jeuneNonScolariseDto = new JeuneNonScolariseDto();
        AppUser appUser = jeuneNonScolarise.getAppUser();

        jeuneNonScolariseDto.setId(jeuneNonScolarise.getId());
        jeuneNonScolariseDto.setPrenom(appUser.getPrenom());
        jeuneNonScolariseDto.setNom(appUser.getNom());
        jeuneNonScolariseDto.setMail(appUser.getMail());
        jeuneNonScolariseDto.setNumTele(appUser.getNumTele());
        jeuneNonScolariseDto.setSexe(jeuneNonScolarise.getSexe());
        jeuneNonScolariseDto.setDateNaissance(jeuneNonScolarise.getDateNaissance());
        jeuneNonScolariseDto.setAge(jeuneNonScolarise.getAge());
        jeuneNonScolariseDto.setIdentifiantPatient(jeuneNonScolarise.getIdentifiantPatient());
        jeuneNonScolariseDto.setCin(jeuneNonScolarise.getCin());
        jeuneNonScolariseDto.setConfirmed(jeuneNonScolarise.getIsConfirmed());
        jeuneNonScolariseDto.setFirstAuth(jeuneNonScolarise.getIsFirstAuth());
        jeuneNonScolariseDto.setROLE(jeuneNonScolarise.getROLE());
        jeuneNonScolariseDto.setDernierNiveauEtudes(jeuneNonScolarise.getDernierNiveauEtudes());
        jeuneNonScolariseDto.setSituationActuelle(jeuneNonScolarise.getSituationActuelle());

        return jeuneNonScolariseDto;
    }
}
