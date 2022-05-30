package com.luminn.firebase.model;


import com.authy.AuthyApiClient;
import com.luminn.firebase.dto.UserDTO;

/**
 * Created by ch on 3/3/2016.
 */
public interface IMobileModel {

    UserDTO findByPhone(String phoneNumber);

    //CK
    AuthyApiClient getAuthyApiClient();
}
