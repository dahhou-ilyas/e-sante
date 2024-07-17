package backend.authModule.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habitude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean sport;
    private Boolean tabac;
    private Integer cigarettesParJour;
    private Boolean alcool;
    private String consommationAlcool;
    private Boolean tempsEcran;
    private String dureeTempsEcran;
}
