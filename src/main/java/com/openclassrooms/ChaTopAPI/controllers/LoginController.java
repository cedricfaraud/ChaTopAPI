package com.openclassrooms.ChaTopAPI.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ChaTopAPI.controllers.dto.LoginDto;
import com.openclassrooms.ChaTopAPI.controllers.dto.UserDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.LoginResponse;
import com.openclassrooms.ChaTopAPI.model.User;
import com.openclassrooms.ChaTopAPI.services.JWTService;
import com.openclassrooms.ChaTopAPI.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    /*
     * public LoginController(JWTService jwtService, UserService userService,
     * AuthenticationManager authenticationManager) {
     * this.jwtService = jwtService;
     * this.userService = userService;
     * this.authenticationManager = authenticationManager;
     * 
     * }
     */
    @PostMapping(value = "api/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> userLogin(@RequestBody @Valid LoginDto loginDto) {
        logger.error("Login user : " + loginDto);
        try {
            User user = userService.userLogin(loginDto.getLogin(), loginDto.getPassword());
            if (user == null) {
                throw new BadCredentialsException("Bad mail : " + loginDto.getLogin());
            }
            logger.error("Login user found : " + user);
            if (!bcryptEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Bad password ");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new LoginResponse(jwtService.generateToken(user.getEmail())));
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
         * token = jwtService.generateToken(user.getEmail());
         * }
         * return ResponseEntity.status(HttpStatus.ACCEPTED).body(new
         * LoginResponse(token));
         */
    }

    /**
     * 
     * @param userDto
     * @return
     */
    @PostMapping(value = "api/auth/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> createUser(@RequestBody UserDto userDto) {
        logger.error("Create user : " + userDto);
        // todo : Vérifier mail unique
        User user = userService.getUserByEmail(userDto.getEmail());
        if (user == null) {
            userDto.setPassword(bcryptEncoder.encode(userDto.getPassword()));
            User createdUser = userService.saveUser(userDto.userDtoToUser());
            logger.error("createdUser : " + createdUser);
            String token = null;
            if (createdUser != null) {
                token = jwtService.generateToken(createdUser.getEmail());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
        } else {
            logger.error("Create user error, user already exist : " + userDto);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /* Get current user (logged) */
    @GetMapping(value = "api/auth/me", produces = { "application/json" })
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    /* Get user by id */
    @GetMapping(value = "api/user/{id}", produces = { "application/json" })
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }
}