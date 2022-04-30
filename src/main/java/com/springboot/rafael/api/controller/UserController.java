package com.springboot.rafael.api.controller;

import javax.validation.Valid;
import com.springboot.rafael.domain.entity.CustomUser;
import com.springboot.rafael.dto.CredentialsDTO;
import com.springboot.rafael.dto.TokenDTO;
import com.springboot.rafael.exception.InvalidPasswordException;
import com.springboot.rafael.security.JWTService;
import com.springboot.rafael.service.implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImplementation userService;
    private final JWTService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomUser createUser(@RequestBody @Valid CustomUser user){
        return userService.createUser(user);      
    }

    @PostMapping("/auth")
    public TokenDTO authenticate(@RequestBody CredentialsDTO credentials){
        try {
            UserDetails authenticatedUser =  userService.authenticate(
                CustomUser.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .build()
            );

            String token = jwtService.generateToken( 
                CustomUser.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .build()
            );

            return new TokenDTO(authenticatedUser.getUsername(), token);
        } catch (UsernameNotFoundException | InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    
}
