package backend.authModule.web;


import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;
import backend.authModule.service.MedecinService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medecins")
@AllArgsConstructor
public class MedecinController {
    private MedecinService medecinService;

    @PostMapping
    public Medecin createMedcine(@RequestBody Medecin medecin) throws MedecinException {
        return medecinService.saveMecine(medecin);
    }

    @ExceptionHandler(MedecinException.class)
    public ResponseEntity<Object> handleMedecinException(MedecinException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

