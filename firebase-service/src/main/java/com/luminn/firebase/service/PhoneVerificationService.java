package com.luminn.firebase.service;

import com.authy.AuthyApiClient;
import com.authy.api.Params;
import com.authy.api.Verification;

import com.luminn.firebase.twilio.TokenVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.twilio.accountsecurity.exceptions.TokenVerificationException;
@Service
public class PhoneVerificationService {

   // private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    private AuthyApiClient authyApiClient;

    @Autowired
    public PhoneVerificationService(AuthyApiClient authyApiClient) {
        this.authyApiClient = authyApiClient;
    }

    public void start(String countryCode, String phoneNumber, String via) {
        Params params = new Params();
        params.setAttribute("code_length", "4");
        Verification verification = authyApiClient
                .getPhoneVerification()
                .start(phoneNumber, countryCode, via, params);

        if(!verification.isOk()) {
            logAndThrow("Error requesting phone verification. " +
                    verification.getMessage());
        }
    }

    public void verify(String countryCode, String phoneNumber, String token) {
        Verification verification = authyApiClient
                .getPhoneVerification()
                .check(phoneNumber, countryCode, token);

        if(!verification.isOk()) {
            logAndThrow("Error verifying token. " + verification.getMessage());
        }
    }

    private void logAndThrow(String message) {
        //LOGGER.warn(message);
        throw new TokenVerificationException(message);
    }
}
