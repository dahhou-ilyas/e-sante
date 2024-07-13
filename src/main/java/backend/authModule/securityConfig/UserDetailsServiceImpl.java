package backend.authModule.securityConfig;

import backend.authModule.entities.Jeune;
import backend.authModule.entities.Medecin;
import backend.authModule.repository.JeuneRepository;
import backend.authModule.repository.MedecinRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    JeuneRepository jeuneRepository;
    MedecinRepository medecinRepository;
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Jeune> jeuneOpt = jeuneRepository.findByMailOrCinOrCNEOrCodeMASSAR(username);
        Optional<Medecin> medecinOpt = medecinRepository.findByCinOrMail(username);

        if (medecinOpt.isPresent()) {
            Medecin medecin = medecinOpt.get();
            return User
                    .withUsername(username)
                    .password(medecin.getAppUser().getPassword())
                    .roles(medecin.getROLE()).build();
        } else if (jeuneOpt.isPresent()) {
            Jeune jeune = jeuneOpt.get();
            return User
                    .withUsername(username)
                    .password(jeune.getAppUser().getPassword())
                    .roles(jeune.getROLE()).build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
