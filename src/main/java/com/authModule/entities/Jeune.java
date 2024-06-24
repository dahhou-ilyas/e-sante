package com.authModule.entities;

import com.authModule.enums.Sexe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_jeune")
public class Jeune extends AppUser{
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    private Date dateNaissance;
    private int age;
    private int identifiantPatient;
    private boolean scolarise;
    private String cin;

    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentFamilial antecedentFamilial;

    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentPersonnel antecedentPersonnel;

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
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

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if (birthDate != null && currentDate != null) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            throw new IllegalArgumentException("La date de naissance et la date actuelle ne doivent pas être nulles");
        }
    }

}
