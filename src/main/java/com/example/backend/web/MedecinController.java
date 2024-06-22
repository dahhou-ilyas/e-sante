package com.example.backend.web;


import com.example.backend.entities.Medecin;
import com.example.backend.service.MedecinService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medecins")
@AllArgsConstructor
public class MedecinController {
    private MedecinService medecinService;

    @PostMapping
    public Medecin createMedcine(@RequestBody Medecin medecin){
        return medecinService.saveMecine(medecin);
    }
}
