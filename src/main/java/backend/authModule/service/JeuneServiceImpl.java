package backend.authModule.service;

import backend.authModule.entities.Jeune;
import backend.authModule.repository.JeuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
//il faut ajouté de global hundler Exception
public class JeuneServiceImpl implements JeuneService {

    private JeuneRepository jeuneRepository;
    @Override
    public Jeune saveJeune(Jeune jeune) {
        return null;
    }
}
