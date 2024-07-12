package backend.authModule.web;

import backend.authModule.dto.JeuneDTO;
import backend.authModule.entities.*;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.JeuneException;
import backend.authModule.exception.PhoneNonValideException;
import backend.authModule.repository.UserRepository;
import backend.authModule.service.JeuneService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/jeunes")
@AllArgsConstructor
@CrossOrigin("*")
public class JeunController {
    private JeuneService jeuneService;

    @GetMapping
    public ResponseEntity<JeuneDTO> getAllJeune(){
        return null;
    }

    @PostMapping("/scolarise")
    public ResponseEntity<JeuneDTO> saveJeuneScolarise(@RequestBody JeuneScolarise jeuneScolarise) {
        try {
            JeuneDTO savedJeune = jeuneService.saveJeune(jeuneScolarise);
            return ResponseEntity.ok(savedJeune);
        } catch (EmailNonValideException | PhoneNonValideException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/nonscolarise")
    public ResponseEntity<JeuneDTO> saveJeuneNonScolarise(@RequestBody JeuneNonScolarise jeuneNonScolarise) {
        try {
            JeuneDTO savedJeune = jeuneService.saveJeune(jeuneNonScolarise);
            return ResponseEntity.ok(savedJeune);
        } catch (EmailNonValideException | PhoneNonValideException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{jeuneId}/antecedents/familiaux")
    public ResponseEntity<?> addAntecedentFamilial(@PathVariable Long jeuneId, @RequestBody AntecedentFamilial antecedentFamilial) {
        try {
            AntecedentFamilial savedAntecedentFamilial = jeuneService.addAntecedentFamilial(jeuneId, antecedentFamilial);
            return ResponseEntity.ok(savedAntecedentFamilial);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{jeuneId}/antecedents/personnels")
    public ResponseEntity<?> addAntecedentPersonnel(@PathVariable Long jeuneId, @RequestBody AntecedentPersonnel antecedentPersonnel) {
        try {
            AntecedentPersonnel savedAntecedentPersonnel = jeuneService.addAntecedentPersonnel(jeuneId, antecedentPersonnel);
            return ResponseEntity.ok(savedAntecedentPersonnel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{jeuneId}/antecedents")
    public ResponseEntity<?> getAntecedents(@PathVariable Long jeuneId) throws JeuneException {
        try {
            Map<String, Object> result = jeuneService.getAntecedents(jeuneId);
            return ResponseEntity.ok(result);
        } catch (JeuneException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirmation")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
        Jeune jeune = jeuneService.confirmEmail(token);
        return ResponseEntity.ok("Email confirmed successfully");
    }

}
