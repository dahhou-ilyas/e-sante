package backend.authModule.securityConfig;

import backend.authModule.entities.Jeune;
import backend.authModule.entities.Medecin;
import backend.authModule.repository.JeuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JeuneDetailsService implements UserDetailsService {
    JeuneRepository jeuneRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Jeune> jeuneOpt = jeuneRepository.findByMailOrCinOrCNEOrCodeMASSAR(username);

        if (jeuneOpt.isPresent()) {
            Jeune jeune = jeuneOpt.get();
            return User
                    .withUsername(username)
                    .password(jeune.getAppUser().getPassword())
                    .roles(jeune.getROLE()).build();
        } else {
            throw new UsernameNotFoundException("Jeune not found with username: " + username);
        }
    }
}
