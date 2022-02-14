package com.example.documentsystem.controller;

import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.exceptions.UnauthorizedException;
import com.example.documentsystem.exceptions.WrongPasswordException;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.AuthenticationRequest;
import com.example.documentsystem.models.AuthenticationResponse;
import com.example.documentsystem.models.ChangePassword;
import com.example.documentsystem.services.UserService;
import com.example.documentsystem.util.JwtUtil;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@ExtensionMethod(EntityExtensions.class)
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtTokenUtil;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtTokenUtil) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword changePassword) {
        UserEntity userEntity = userService.getCurrentUser();

        if (!userEntity.getUsername().equals(changePassword.getUsername())) {
            throw new UnauthorizedException(String.format("You are not authorized to change the password of user '%s'", changePassword.getUsername()));
        }

        if (!passwordEncoder.matches(changePassword.getOldPassword(), userEntity.getPassword())) {
            throw new WrongPasswordException("Incorrect old password!");
        }

        userEntity.setPassword(changePassword.getNewPassword());
        userService.update(userEntity.toUser());

        return ResponseEntity.ok("Password changed");
    }
}
