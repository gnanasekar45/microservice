package com.luminn.firebase.security;

import org.springframework.stereotype.Service;

@Service
public interface HashCodeUtils {

    String generatePasswordFirstTime(String passwordText);

    boolean validPasswordCheck(String existingEncodedPassword, String passwordText);

}
