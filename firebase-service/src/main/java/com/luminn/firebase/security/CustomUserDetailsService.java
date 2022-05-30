package com.luminn.firebase.security;

import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserModel userModel;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        //User user =  this.userRepository.findByEmail(username);
             //.orElseThrow(() -> new UsernameNotFoundException("Unable to find user - " + username));
        return withUsername(user.getEmail()).password(user.getPassword()).authorities(user.getRole()).build();
    }
}
