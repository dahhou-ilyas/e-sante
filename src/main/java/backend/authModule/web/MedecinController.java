package backend.authModule.web;


import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;
import backend.authModule.service.MedecinService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/medecins")
@AllArgsConstructor
public class MedecinController {
    private MedecinService medecinService;

    @PostMapping
    public Medecin createMedcine(@RequestBody Medecin medecin) throws MedecinException {
        return medecinService.saveMecine(medecin);
    }

    //ajouté un endpoite pour la confirmation et le redérigé vers le frontend:
    @GetMapping("/medecins/confirmation")
    public RedirectView confirmEmail(@RequestParam("token") String token) {
        //Medecin medecin = medecinService.confirmEmail(token);
        // Redirection vers une vidéo YouTube après confirmation réussie
        return new RedirectView("https://www.youtube.com/watch?v=VIDEO_ID");
    }

    @ExceptionHandler(MedecinException.class)
    public ResponseEntity<Object> handleMedecinException(MedecinException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

