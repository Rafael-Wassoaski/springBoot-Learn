package com.springboot.rafael.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.springboot.rafael.domain.entity.CustomUser;
import com.springboot.rafael.domain.repository.UserRepo;
import com.springboot.rafael.exception.InvalidPasswordException;
import com.springboot.rafael.exception.RuleException;

@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Usuário com esse nome não existe"));
        String[] roles = user.isAdmin() ? new String[] {"ADMIN", "USER"} : new String[] {"USER"};

        return User
            .builder()
            .username(user.getUsername())
            .password(user.getPassword()).roles(roles)
            .build();
    }

    public CustomUser createUser(CustomUser user){
        String encodedPassword = encoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        return userRepo.save(user);
    }

    public UserDetails authenticate(CustomUser user){
        UserDetails userDetails = loadUserByUsername(user.getUsername());

        boolean isSamePassword = this.encoder.matches(user.getPassword(), userDetails.getPassword());

        if(isSamePassword){
            return userDetails;
        }

        throw new InvalidPasswordException();

    }

}
