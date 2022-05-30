package com.luminn.firebase.model;

import com.luminn.firebase.dto.LoginDTO;
import com.luminn.firebase.dto.MobileDTO;
import com.luminn.firebase.dto.UserSocialDTO;
import com.luminn.firebase.entity.Order;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.OrderRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderModel {
    @Autowired
    UserMongoService userMongoService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;


    public String addOrder(Order order){
        String id = null;

         {
             orderRepository.save(order);


        }
        return "1";
    }

}
