package backend.authModule.web;


import backend.authModule.dto.MedecinResponseDTO;
import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;
import backend.authModule.exception.MedecinNotFoundException;
import backend.authModule.service.MedecinService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/medecins")
@AllArgsConstructor
public class MedecinController {
    private MedecinService medecinService;

    @PostMapping
    public MedecinResponseDTO createMedcine(@RequestBody Medecin medecin) throws MedecinException {
        return medecinService.saveMecine(medecin);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MedecinResponseDTO> patchMedecin(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws MedecinNotFoundException {
        MedecinResponseDTO updatedMedecin = medecinService.updateMedecinPartial(id, updates);
        return ResponseEntity.ok(updatedMedecin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedecin(@PathVariable Long id) {
        try {
            medecinService.deleteMedecin(id);
            return ResponseEntity.ok("Médecin supprimé avec succès");
        } catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MedecinException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<MedecinResponseDTO> getMedecinById(@PathVariable Long id) {
        try {
            MedecinResponseDTO medecin = medecinService.getMedecinById(id);
            return ResponseEntity.ok(medecin);
        } catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @GetMapping("/confirmation")
    public RedirectView confirmEmail(@RequestParam("token") String token) {

        Medecin medecin = medecinService.confirmEmail(token);

        return new RedirectView("https://www.youtube.com/watch?v=VIDEO_ID");
    }

    @ExceptionHandler(MedecinException.class)
    public ResponseEntity<Object> handleMedecinException(MedecinException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

