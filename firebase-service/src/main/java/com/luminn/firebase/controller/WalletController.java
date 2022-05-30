package com.luminn.firebase.controller;

import com.luminn.firebase.entity.Coupon;
import com.luminn.firebase.entity.Wallet;
import com.luminn.firebase.repository.CouponRepository;
import com.luminn.firebase.repository.WalletRepository;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletRepository walletRepository;

    @PostMapping("/app/v1/add")
    private ResponseEntity<StatusResponse> addDelivery(@RequestBody Wallet dto) {

        Wallet wallet = walletRepository.findByUserId(dto.getUserId());
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(true);
        if(wallet == null) {
            walletRepository.save(dto);
            statusResponse.setStatus(true);
            statusResponse.setMessage("user is added");
        }
        else{
            statusResponse.setStatus(false);
            statusResponse.setMessage("user is already existing");
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }


    @GetMapping("/app/v1/find/{userId}")
    private ResponseEntity<StatusResponse> supplierId(@PathVariable String userId) {
        StatusResponse statusResponse = new StatusResponse();
        Wallet wallet = walletRepository.findByUserId(userId);
        System.out.println(" wallet --->" + wallet);
        if(wallet != null){
            statusResponse.setStatus(true);
            statusResponse.setData(wallet);
        }
        else {
            statusResponse.setStatus(false);
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @GetMapping("/app/v1/find/token/{token}")
    private ResponseEntity<StatusResponse> getToken(@PathVariable String token) {
        StatusResponse statusResponse = new StatusResponse();
        List<Wallet> wallets = walletRepository.findByToken(token);


        if(wallets != null && wallets.size() > 0 && wallets.get(0) != null){
            Wallet wl = wallets.get(0);
            statusResponse.setStatus(true);
            statusResponse.setData(wl);
            statusResponse.setMessage("Valid");
        }
        else {
            statusResponse.setStatus(false);
            statusResponse.setData(null);
            statusResponse.setMessage("NotValid");
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @PutMapping("/app/v1/debit")
    private ResponseEntity<StatusResponse> debit(@RequestBody Wallet dto) {
        StatusResponse statusResponse = new StatusResponse();
        Wallet wallet = walletRepository.findByUserId(dto.getUserId());
        System.out.println(" wallet --->" + wallet);
        if(wallet != null){
            statusResponse.setStatus(true);
            wallet.setDebit(dto.getDebit());
            wallet.setBalance(wallet.getBalance() - wallet.getDebit());
            walletRepository.save(wallet);
            statusResponse.setData(wallet);
        }
        else {
            statusResponse.setStatus(false);
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
    @PutMapping("/app/v1/credit")
    private ResponseEntity<StatusResponse> credit(@RequestBody Wallet dto) {
        StatusResponse statusResponse = new StatusResponse();
        Wallet wallet = walletRepository.findByUserId(dto.getUserId());
        System.out.println(" wallet --->" + wallet);
        if(wallet != null){
            statusResponse.setStatus(true);
            wallet.setCredit(dto.getCredit());
            wallet.setBalance(wallet.getBalance() + wallet.getCredit());
            walletRepository.save(wallet);
            statusResponse.setData(wallet);
        }
        else {
            statusResponse.setStatus(false);
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
}
