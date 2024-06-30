package backend.authModule.web;

import backend.authModule.entities.AntecedentFamilial;
import backend.authModule.entities.AntecedentPersonnel;
import backend.authModule.entities.AppUser;
import backend.authModule.entities.Jeune;
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
public class JeunController {
    private JeuneService jeuneService;

    @PostMapping
    public ResponseEntity<?> saveJeune(@RequestBody Jeune jeune) throws PhoneNonValideException, EmailNonValideException {
        Jeune savedJeune=jeuneService.saveJeune(jeune);
        return ResponseEntity.ok(savedJeune);
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
