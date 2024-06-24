package backend.authModule.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class ProfessionnelSante extends AppUser{
    private String cin;
    private String inpe;
}
