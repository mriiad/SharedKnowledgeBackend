package org.sid.repository;

import java.util.Optional;

import org.sid.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

    Optional<RefreshToken> findByToken(String token);

    public void deleteByToken(String token);
}
