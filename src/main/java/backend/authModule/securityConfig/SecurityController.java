package backend.authModule.securityConfig;

import backend.authModule.entities.Jeune;
import backend.authModule.entities.Medecin;
import backend.authModule.repository.JeuneRepository;
import backend.authModule.repository.MedecinRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
@AllArgsConstructor
public class SecurityController {

    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;
    private JeuneRepository jeuneRepository;
    private MedecinRepository medecinRepository;

    @PostMapping
    public Map<String, String> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        Instant instant = Instant.now();

        Jeune jeune = jeuneRepository.findByMailOrCinOrCNEOrCodeMASSAR(username).orElse(null);
        Medecin medecin = medecinRepository.findByCinOrMail(username).orElse(null);


        String scope = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(" "));

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("scope", scope);

        if (medecin != null) {
            claims.put("id", medecin.getId());
            claims.put("nom", medecin.getAppUser().getNom());
            claims.put("prenom", medecin.getAppUser().getPrenom());
            claims.put("mail", medecin.getAppUser().getMail());
            claims.put("role", medecin.getROLE());
            claims.put("confirmed",medecin.isConfirmed());
            claims.put("isFirstAuth",medecin.getIsFirstAuth());
        } else if (jeune != null) {
            claims.put("id", jeune.getId());
            claims.put("nom", jeune.getAppUser().getNom());
            claims.put("prenom", jeune.getAppUser().getPrenom());
            claims.put("mail", jeune.getAppUser().getMail());
            claims.put("role", jeune.getROLE());
            claims.put("confirmed",jeune.getIsConfirmed());
            claims.put("isFirstAuth",jeune.getIsFirstAuth());
        }

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                .subject(username)
                .claim("claims",claims)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet
        );

        String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        return Map.of("access-token", jwt);
    }

}
