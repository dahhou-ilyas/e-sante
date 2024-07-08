package backend.authModule.web;

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

    @PostMapping("/scolarise")
    public ResponseEntity<JeuneScolarise> saveJeuneScolarise(@RequestBody JeuneScolarise jeuneScolarise) {
        JeuneScolarise savedJeune = jeuneService.saveJeuneScolarise(jeuneScolarise);
        return ResponseEntity.ok(savedJeune);
    }

    // il faut defirencier entre un jeune scolarisé et non scolarisé pour crée un objet
    // soit scolarisé ou non scolarisé pour stocké les donné compléte dans le db
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
