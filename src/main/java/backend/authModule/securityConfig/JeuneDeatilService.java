package backend.authModule.securityConfig;

import backend.authModule.entities.Jeune;
import backend.authModule.repository.JeuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JeuneDeatilService implements UserDetailsService {

    JeuneRepository jeuneRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Jeune jeune=jeuneRepository.findByMailOrCinOrCNEOrCodeMASSAR(username).get();
        if(jeune==null) throw new UsernameNotFoundException("Jeune not existe with info "+username);
        UserDetails userDetails= User
                .withUsername(username)
                .password(jeune.getAppUser().getPassword())
                .roles(jeune.getROLE()).build();

        return userDetails;
    }
}
