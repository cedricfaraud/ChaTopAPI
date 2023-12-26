package com.openclassrooms.ChaTopAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.ChaTopAPI.controllers.RentalController;
import com.openclassrooms.ChaTopAPI.model.User;
import com.openclassrooms.ChaTopAPI.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    public User getUser(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bad id : " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Bad mail : " + email));
    }

    public User userLogin(String email, String password) {
        User userByEmail = getUserByEmail(email);
        logger.info("Login user found : " + userByEmail);
        if (!bcryptEncoder.matches(password, userByEmail.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }

        return userByEmail;
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getGrantedAuthorities());
    }

    private List<GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}
