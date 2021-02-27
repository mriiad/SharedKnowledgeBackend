package org.sid.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.sid.exceptions.SpringRedditException;
import org.sid.model.RefreshToken;
import org.sid.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    public RefreshToken generateRefreshToken() {
	RefreshToken refreshToken = new RefreshToken();
	refreshToken.setToken(UUID.randomUUID().toString());
	refreshToken.setCreatedDate(Instant.now());
	
	return refreshTokenRepository.save(refreshToken);
    }
    
    public void validateRefreshToken(String token) {
	refreshTokenRepository.findByToken(token).orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
    }
    
    public void deleteRefreshToken(String token) {
	refreshTokenRepository.deleteByToken(token);
    }
}
