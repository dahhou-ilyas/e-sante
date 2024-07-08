package backend.authModule.entities;

import backend.authModule.enums.Sexe;
import backend.authModule.exception.AgeNonValideException;
import backend.authModule.exception.JeuneException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 6, discriminatorType = DiscriminatorType.STRING)
@AllArgsConstructor @NoArgsConstructor
public class Jeune{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    private Date dateNaissance;
    private int age;
    private int identifiantPatient;
    private boolean scolarise;
    private String cin;
    private Boolean isConfirmed = false;

    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentFamilial antecedentFamilial;

    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentPersonnel antecedentPersonnel;

    @OneToOne(mappedBy = "jeune", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ConfirmationToken confirmationToken;

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) throws AgeNonValideException {
        this.dateNaissance = dateNaissance;
        LocalDate birthD=this.dateNaissance.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.age=calculateAge(birthD,LocalDate.now());
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getIdentifiantPatient() {
        return identifiantPatient;
    }

    public void setIdentifiantPatient(int identifiantPatient) {
        this.identifiantPatient = identifiantPatient;
    }

    public boolean isScolarise() {
        return scolarise;
    }

    public void setScolarise(boolean scolarise) {
        this.scolarise = scolarise;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) throws AgeNonValideException {
        if (birthDate != null && currentDate != null) {
            int age= Period.between(birthDate, currentDate).getYears();
            if(age>=10 && age<=30){
                return age;
            }else {
                throw new AgeNonValideException("l'age doit être entre 10 et 30 ans");
            }
        } else {
            throw new IllegalArgumentException("La date de naissance et la date actuelle ne doivent pas être nulles");
        }
    }

    public void setIsConfirmed(Boolean isConfirmed){
        this.isConfirmed=isConfirmed;
    }
    public Boolean getIsConfirmed(){
        return isConfirmed;
    }
}
