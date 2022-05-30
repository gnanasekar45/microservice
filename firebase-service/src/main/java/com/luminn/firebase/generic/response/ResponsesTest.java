package com.luminn.firebase.generic.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ResponsesTest {

    //@Autowired
   // RestTemplate restTemplate;

    String schema;
    String platformApipost;
    String flowablePath;
    String host;


   /* public <RequestType,ResponseType> ResponseType postForEntity(String urlPoint,RequestType requestType,Class<ResponseType> type){
        ResponseEntity<ResponseType> response =  restTemplate.postForEntity(getFulUrl(urlPoint),requestType,type);
        return response.getBody();
    }

    public <ResponseType> ResponseType getForEntity(String url,Class<ResponseType> type){
        return (ResponseType) restTemplate.getForEntity(url,type);
    }

    public String getFulUrl(String url){
       return UriComponentsBuilder.newInstance().scheme(schema).host(host).build().toUriString();
    }*/
}
