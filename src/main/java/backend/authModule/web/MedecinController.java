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

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class MedecinController {
    private MedecinService medecinService;

    @PostMapping("/register/medecins")
    public MedecinResponseDTO createMedcine(@RequestBody Medecin medecin) throws MedecinException {
        return medecinService.saveMecine(medecin);
    }

    @PatchMapping("/medecins/{id}")
    public ResponseEntity<MedecinResponseDTO> patchMedecin(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws MedecinNotFoundException {
        MedecinResponseDTO updatedMedecin = medecinService.updateMedecinPartial(id, updates);
        return ResponseEntity.ok(updatedMedecin);
    }

    @GetMapping("/medecins")
    public ResponseEntity<List<MedecinResponseDTO>> getAllMedecins() {
        try {
            List<MedecinResponseDTO> medecins = medecinService.getAllMedecins();
            return ResponseEntity.ok(medecins);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/medecins/{id}")
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
    @GetMapping("/medecins/{id}")
    public ResponseEntity<MedecinResponseDTO> getMedecinById(@PathVariable Long id) {
        try {
            MedecinResponseDTO medecin = medecinService.getMedecinById(id);
            return ResponseEntity.ok(medecin);
        } catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @GetMapping("/register/medecins/confirmation")
    public RedirectView confirmEmail(@RequestParam("token") String token) {

        Medecin medecin = medecinService.confirmEmail(token);

        return new RedirectView("http://localhost:3000/auth/medecins");
    }

    @PostMapping("/register/resend-token")
    public ResponseEntity<String> resendToken(@RequestParam("email") String email) {
        try {
            medecinService.resendToken(email);
            return ResponseEntity.ok("Token resent successfully");
        } catch (MedecinException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ExceptionHandler(MedecinException.class)
    public ResponseEntity<Object> handleMedecinException(MedecinException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

