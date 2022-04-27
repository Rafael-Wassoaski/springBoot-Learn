package com.springboot.rafael.api.controller;

import javax.validation.Valid;
import com.springboot.rafael.domain.entity.CustomUser;
import com.springboot.rafael.service.implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImplementation userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomUser createUser(@RequestBody @Valid CustomUser user){
        return userService.createUser(user);      
    }
    
}
