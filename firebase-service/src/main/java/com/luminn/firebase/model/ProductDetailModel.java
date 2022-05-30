package com.luminn.firebase.model;

import com.luminn.firebase.dto.LoginDTO;
import com.luminn.firebase.dto.MobileDTO;
import com.luminn.firebase.dto.UserSocialDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailModel {
    @Autowired
    UserMongoService userMongoService;

    @Autowired
    UserRepository userRepository;

    public User getSocaialId(UserSocialDTO userSocial){
        return userRepository.findBySocialId(userSocial.getSocialId());
    }

    public User getLogincheck(UserSocialDTO userSocial){


        if(userSocial != null && userSocial.getSocialId() != null){
            User userSocail = this.userMongoService.findBySocail(userSocial.getSocialId());
            return userSocail;
        }

        if(userSocial != null && userSocial.getEmail() != null) {
            User user1 = this.userMongoService.findByEmail(userSocial.getEmail());
            return user1;
        }

        if(userSocial != null && userSocial.getPhoneNumber() != null){
            User userPhone = this.userMongoService.findByPhoneNumber(userSocial.getPhoneNumber());
                return userPhone;
        }

        return null;
    }
    public LoginDTO getLoginObject(User userSocial){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setId(userSocial.getId());
        loginDTO.setEmail(userSocial.getEmail());
        loginDTO.setRole(userSocial.getRole());
        loginDTO.setPhoneNumber(userSocial.getPhoneNumber());
        loginDTO.setName(userSocial.getFirstName()+userSocial.getLastName());
        return loginDTO;
    }

    public LoginDTO addUser(UserSocialDTO userSocial){
        String id = null;
        LoginDTO loginDTO = new LoginDTO();

         {
            MobileDTO mobileDTO = new MobileDTO();
            mobileDTO.setFirstName(userSocial.getFirstName());
            mobileDTO.setLastName(userSocial.getLastName());
            //mobileDTO.setPassword("123456789");
             if(userSocial.getPhoneNumber() != null && !"".equalsIgnoreCase(userSocial.getPhoneNumber()))
                mobileDTO.setPhoneNumber(userSocial.getPhoneNumber());
            if(userSocial.getEmail() != null &&  !"".equalsIgnoreCase(userSocial.getEmail()))
                mobileDTO.setEmail(userSocial.getEmail());

            mobileDTO.setRole("ROLE_USER");
            ///////////////////////////////////////////////////////////////////VERY GOOD CODE BELOW/////////////////////////////////
            // if(userSocial.getSocialId() != null &&  !"".equalsIgnoreCase(userSocial.getSocialId()))
             //    mobileDTO.setId(userSocial.getSocialId());

             id = this.userMongoService.addUserOneRegistration(mobileDTO,true,userSocial.getSocialId());
            //statusResponse.setMessage("new");
            loginDTO.setId(id);
            loginDTO.setEmail(mobileDTO.getEmail());
            loginDTO.setPhoneNumber(mobileDTO.getPhoneNumber());
            loginDTO.setRole("ROLE_USER");
            loginDTO.setName(userSocial.getFirstName() + userSocial.getLastName());
        }
        return loginDTO;
    }

}
