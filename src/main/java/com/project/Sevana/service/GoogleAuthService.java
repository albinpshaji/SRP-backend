package com.project.Sevana.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.project.Sevana.DTO.LoginresponseDTO;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Userrepo;

@Service
public class GoogleAuthService {

    @Value("${google.client-id}")
    private String googleClientId;

    private final Userrepo userrepo;
    private final Jwtservice jwtservice;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public GoogleAuthService(Userrepo userrepo, Jwtservice jwtservice) {
        this.userrepo = userrepo;
        this.jwtservice = jwtservice;
    }

    public LoginresponseDTO authenticateGoogleUser(String idTokenString) {
        try {
            // Verify the Google ID token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Check if user already exists (by email or by username=email)
            Users existingUser = userrepo.findByEmail(email);
            if (existingUser == null) {
                existingUser = userrepo.findByUsername(email);
                // Backfill the email field if it was missing
                if (existingUser != null && existingUser.getEmail() == null) {
                    existingUser.setEmail(email);
                    existingUser.setAuthProvider("GOOGLE");
                    userrepo.save(existingUser);
                }
            }

            if (existingUser != null) {
                // Returning user — generate token and return
                String token = jwtservice.generatetoken(existingUser.getUsername());
                return new LoginresponseDTO(token, existingUser.getRole(), existingUser.getUserid());
            }

            // New Google user — create incomplete account
            Users newUser = new Users();
            newUser.setUsername(email); // Use email as username (unique)
            newUser.setEmail(email);
            newUser.setPassword(encoder.encode(UUID.randomUUID().toString())); // Random unguessable password
            newUser.setRole("INCOMPLETE");
            newUser.setAuthProvider("GOOGLE");
            newUser.setIsverified("PENDING");

            Users savedUser = userrepo.save(newUser);

            String token = jwtservice.generatetoken(savedUser.getUsername());
            return new LoginresponseDTO(token, savedUser.getRole(), savedUser.getUserid());

        } catch (Exception e) {
            throw new RuntimeException("Google authentication failed: " + e.getMessage(), e);
        }
    }
}
