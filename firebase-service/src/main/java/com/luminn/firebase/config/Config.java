package com.luminn.firebase.config;

import com.authy.AuthyApiClient;

import com.luminn.firebase.model.IMobileModel;
import com.luminn.firebase.model.MobileModel;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import javax.annotation.PostConstruct;

/**
 * Created by ch on 2/21/2016.
 */

@Configuration
public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

   private AuthyApiClient authyClient;

    @Value("${emailJetApiKey}")
    private String emailJetApiKey;
    @Value("${emailJetSecretKey}")
    private String emailJetSecretKey;

    @PostConstruct
    public void start() {
        //Twilio();
         //authyClient = new AuthyApiClient(Twilio.ACCOUNT_ID,"http://sandbox-api.authy.com",true);
        //https://www.twilio.com/console/authy/applications/86921/settings
        //AC1772f54eb77f74629633b03ff96b9d11
        authyClient = new AuthyApiClient("9vynOT8pHyP1i2F85V9vr25KB2XU9cSH");
    }

    @Bean
    public IMobileModel getPassengerModel() {
        authyClient = new AuthyApiClient("9vynOT8pHyP1i2F85V9vr25KB2XU9cSH");
        return new MobileModel(authyClient);

    }

    @Bean
    public AuthyApiClient authyApiClient() {
        return new AuthyApiClient("9vynOT8pHyP1i2F85V9vr25KB2XU9cSH");
    }



   /* private void Twilio(){
        Twilio.ACCOUNT_ID ="d249cf68a1ee4551f09fdabac136e4da";
    }*/

    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
        fmConfigFactoryBean.setTemplateLoaderPath("/templates/");
        return fmConfigFactoryBean;
    }

    @Bean("mailJet")
    public MailjetClient getMailjetClient(){
        MailjetClient client;
        client = new MailjetClient(emailJetApiKey,
                emailJetSecretKey, new ClientOptions("v3.1"));
        return client;

    }


}
