package com.openclassrooms.ChaTopAPI.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ChaTopAPI.controllers.dto.UserDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.LoginResponse;
import com.openclassrooms.ChaTopAPI.model.User;
import com.openclassrooms.ChaTopAPI.services.JWTService;
import com.openclassrooms.ChaTopAPI.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/auth")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private JWTService jwtService;
    private UserService userService;
    private final AuthenticationManager authenticationManager;

    public LoginController(JWTService jwtService, UserService userService,
            AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;

    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> getToken(@RequestBody @Valid UserDto request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getName(), request.getPassword()));

            User user = (User) authenticate.getPrincipal();

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new LoginResponse(jwtService.generateToken(user.getName())));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // authenticate(((UserDto) authentication).getEmail(), ((UserDto)
        // authentication).getPassword());
        /*
         * User user = userService.getUserByEmail(((UserDto)
         * authentication).getEmail());
         * String token = null;
         * if (user != null) {
         * // Todo : add password control
         * token = jwtService.generateToken(user.getName());
         * }
         * return ResponseEntity.status(HttpStatus.ACCEPTED).body(new
         * LoginResponse(token));
         */
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> createUser(@RequestBody UserDto user) {
        logger.error("Create user : " + user);
        User createdUser = userService.saveUser(user.userDtoToUser());
        logger.error("createdUser : " + createdUser);
        String token = null;
        if (createdUser != null) {
            token = jwtService.generateToken(createdUser.getName());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
    }

    /* Get current user (logged) */
    @GetMapping(value = "/me", produces = { "application/json" })
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    /* Get user by id */
    @GetMapping(value = "/user/{id}", produces = { "application/json" })
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }
}