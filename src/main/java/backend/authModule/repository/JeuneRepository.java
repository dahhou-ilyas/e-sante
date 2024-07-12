package backend.authModule.repository;

import backend.authModule.entities.Jeune;
import backend.authModule.entities.JeuneScolarise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JeuneRepository extends JpaRepository<Jeune,Long> {
    @Query("SELECT j FROM Jeune j WHERE j.cin = :cin")
    Optional<Jeune> findByCin(@Param("cin") String cin);
    @Query("SELECT j FROM JeuneScolarise j WHERE j.CNE = :cne")
    Optional<JeuneScolarise> findByCNE(@Param("cne") String cne);

    @Query("SELECT j FROM JeuneScolarise j WHERE j.codeMASSAR = :codeMASSAR")
    Optional<JeuneScolarise> findByCodeMASSAR(@Param("codeMASSAR") String codeMASSAR);

    @Query("SELECT j FROM Jeune j WHERE j.appUser.mail = :mail")
    Optional<Jeune> findByMail(@Param("mail") String mail);

    @Query("SELECT j FROM Jeune j " +
            "WHERE j.appUser.mail = :recherche " +
            "OR j.cin = :recherche " +
            "OR EXISTS (SELECT js FROM JeuneScolarise js WHERE (js.CNE = :recherche OR js.codeMASSAR = :recherche) AND js.id = j.id)")
    Optional<Jeune> findByMailOrCinOrCNEOrCodeMASSAR(@Param("recherche") String recherche);
}
