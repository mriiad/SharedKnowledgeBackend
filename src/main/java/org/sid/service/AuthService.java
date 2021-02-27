package org.sid.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.sid.dto.AuthenticationResponse;
import org.sid.dto.LoginRequest;
import org.sid.dto.RefreshTokenRequest;
import org.sid.dto.RegisterRequest;
import org.sid.exceptions.SpringRedditException;
import org.sid.model.NotificationEmail;
import org.sid.model.User;
import org.sid.model.VerificationToken;
import org.sid.repository.UserRepository;
import org.sid.repository.VerificationTokenRepository;
import org.sid.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Transactional
    public void signup(RegisterRequest registerRequest) {
	User user = new User();
	user.setUsername(registerRequest.getUsername());
	user.setEmail(registerRequest.getEmail());
	user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
	user.setCreated(Instant.now());
	user.setEnabled(false);
	
	userRepository.save(user);
	
	String token = generateVerificationToken(user);
	mailService.sendEmail(new NotificationEmail("Reddit email activation", user.getEmail(), "Thank you for signing up to Spring Reddit, " +
		"please click on the below url to activate your account: " + "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
    
    private String generateVerificationToken(User user) {
	String token = UUID.randomUUID().toString();
	VerificationToken verificationToken = new VerificationToken();
	verificationToken.setToken(token);
	verificationToken.setUser(user);
	
	verificationTokenRepository.save(verificationToken);
	
	return token;
    }

    public void activateAccount(String verificationToken) {
	Optional<VerificationToken> token = verificationTokenRepository.findByToken(verificationToken);
	token.orElseThrow(() -> new SpringRedditException("Token not found"));
	enableUser(token);
    }

    @Transactional
    public void enableUser(Optional<VerificationToken> token) {
	Optional<User> user = userRepository.findById(token.get().getUser().getUserId());
	user.orElseThrow(() -> new SpringRedditException("User not found"));
	user.get().setEnabled(true);
	userRepository.save(user.get());
    }
    
    public AuthenticationResponse login(LoginRequest loginRequest) {
	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	SecurityContextHolder.getContext().setAuthentication(authentication);
	String token = jwtProvider.generateToken(authentication);
	return AuthenticationResponse.builder()
		.authenticationToken(token)
		.refreshToken(refreshTokenService.generateRefreshToken().getToken())
		.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
		.username(loginRequest.getUsername())
		.build();
    }

    public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
	refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());;
	String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
	return AuthenticationResponse.builder()
		.authenticationToken(token)
		.refreshToken(refreshTokenRequest.getRefreshToken())
		.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
		.username(refreshTokenRequest.getUsername())
		.build();
    }
}