package org.sid.controller;

import javax.validation.Valid;

import org.sid.dto.AuthenticationResponse;
import org.sid.dto.LoginRequest;
import org.sid.dto.RefreshTokenRequest;
import org.sid.dto.RegisterRequest;
import org.sid.service.AuthService;
import org.sid.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
	authService.signup(registerRequest);
	return new ResponseEntity<String>("User registration successful", HttpStatus.OK);
    }
    
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
	authService.activateAccount(token);
	return new ResponseEntity<String>("Account activation successful", HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
	return authService.login(loginRequest);
    }
    
    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid /*@Valid is used to validate the constraint @NotBlank in RefreshTokenRequest */ @RequestBody RefreshTokenRequest refreshTokenRequest) {
	return authService.refreshToken(refreshTokenRequest);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
	refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
	return ResponseEntity.status(HttpStatus.OK)
		.body("Refresh Token Deleted Successfully!");
    }
}
