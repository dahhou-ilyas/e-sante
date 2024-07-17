package backend.authModule.securityConfig;

import backend.authModule.entities.Jeune;
import backend.authModule.entities.Medecin;
import backend.authModule.exception.BadRequestException;
import backend.authModule.exception.ResourceNotFoundException;
import backend.authModule.exception.UsernameNotFoundException;
import backend.authModule.repository.JeuneRepository;
import backend.authModule.repository.MedecinRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/login")

public class SecurityController {
    private final JwtEncoder jwtEncoder;
    private final JeuneRepository jeuneRepository;
    private final MedecinRepository medecinRepository;
    private final AuthenticationManager authenticationManagerJeune;
    private final AuthenticationManager authenticationManagerMedecin;

    public SecurityController(JwtEncoder jwtEncoder, JeuneRepository jeuneRepository, MedecinRepository medecinRepository,
                              @Qualifier("authenticationManagerJeune") AuthenticationManager authenticationManagerJeune,
                              @Qualifier("authenticationManagerMedecin") AuthenticationManager authenticationManagerMedecin) {
        this.authenticationManagerJeune = authenticationManagerJeune;
        this.authenticationManagerMedecin = authenticationManagerMedecin;
        this.jwtEncoder = jwtEncoder;
        this.jeuneRepository = jeuneRepository;
        this.medecinRepository = medecinRepository;
    }



    @PostMapping("/jeunes")
    public Map<String, String> login(@RequestBody Map<String, String> loginData) throws UsernameNotFoundException {
        String username = loginData.get("username");
        String password = loginData.get("password");

        try{
            Authentication authentication = authenticationManagerJeune.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            Instant instant = Instant.now();

            Jeune jeune = jeuneRepository.findByMailOrCinOrCNEOrCodeMASSAR(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("role", scope);

            if (jeune != null) {
                claims.put("id", jeune.getId());
                claims.put("nom", jeune.getAppUser().getNom());
                claims.put("prenom", jeune.getAppUser().getPrenom());
                claims.put("mail", jeune.getAppUser().getMail());
                claims.put("confirmed", jeune.getIsConfirmed());
                claims.put("isFirstAuth", jeune.getIsFirstAuth());
            }

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))  // Changer la durée d'expiration à 30 minutes
                    .subject(username)
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        }catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }


    }

    @PostMapping("/medecins")
    public Map<String, String> loginMedcin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            Authentication authentication = authenticationManagerMedecin.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            Instant instant = Instant.now();

            Medecin medecin = medecinRepository.findByCinOrMail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Medecin not found with username: " + username));

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("role", scope);

            claims.put("id", medecin.getId());
            claims.put("nom", medecin.getAppUser().getNom());
            claims.put("prenom", medecin.getAppUser().getPrenom());
            claims.put("mail", medecin.getAppUser().getMail());
            claims.put("confirmed", medecin.isConfirmed());
            claims.put("isFirstAuth", medecin.getIsFirstAuth());

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .subject(username)
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid username or password");
        }
    }

}
