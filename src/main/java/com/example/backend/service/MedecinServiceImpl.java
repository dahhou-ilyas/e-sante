package com.example.backend.service;

import com.example.backend.entities.Medecin;
import com.example.backend.repository.MedecinRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MedecinServiceImpl implements MedecinService {

    private MedecinRepository medecinRepository;


    @Override
    public Medecin saveMecine(Medecin medecin) {
        return medecinRepository.save(medecin);
    }
}
