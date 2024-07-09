package backend.authModule.entities;

import backend.authModule.enums.Sexe;
import backend.authModule.exception.AgeNonValideException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 6, discriminatorType = DiscriminatorType.STRING)
@Data
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
    @Column(unique = true)
    private int identifiantPatient;
    private boolean scolarise;
    @Column(unique = true)
    private String cin;
    private Boolean isConfirmed = false;
    private Boolean isFirstAuth = true;

    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentFamilial antecedentFamilial;

    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentPersonnel antecedentPersonnel;

}
