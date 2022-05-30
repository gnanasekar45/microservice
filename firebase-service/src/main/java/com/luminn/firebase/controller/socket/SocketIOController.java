package com.luminn.firebase.controller.socket;

import com.luminn.firebase.socket.ISocketIOService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//https://programmer.ink/think/springboot-23-integrates-socket.io-server-and-client-for-communication.html
@RestController
@RequestMapping("/api/socket.io")
public class SocketIOController {
    //https://cdmana.com/2021/03/20210327234947051D.html
    @Autowired
    private ISocketIOService socketIOService;

    @PostMapping(value = "/api/pushMessageToUser", produces = {"application/json"})
    //@ApiOperation(value = "Push information to specified client", httpMethod = "POST", response = ApiResult.class)
    public String pushMessageToUser(@RequestParam String userId, @RequestParam String msgContent) {
        socketIOService.pushMessageToUser(userId, msgContent);
        return "Success";
    }
}
