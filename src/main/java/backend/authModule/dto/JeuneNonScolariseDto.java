package backend.authModule.dto;

import backend.authModule.enums.NiveauEtudes;
import backend.authModule.enums.Sexe;
import backend.authModule.enums.Situation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JeuneNonScolariseDto {
    private Long id;
    private String nom;
    private String prenom;
    private Sexe sexe;
    private Date dateNaissance;
    private int age;
    private int identifiantPatient;
    private String cin;
    private String mail;
    private String numTele;
    private boolean isConfirmed;
    private boolean isFirstAuth;
    private String ROLE;
    private NiveauEtudes dernierNiveauEtudes;
    private Situation situationActuelle;
}
