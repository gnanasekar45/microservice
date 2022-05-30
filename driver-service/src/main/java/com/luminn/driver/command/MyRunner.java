package com.luminn.driver.command;

import com.luminn.driver.model.Location;
import com.luminn.driver.model.UserOne;
import com.luminn.driver.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class MyRunner implements CommandLineRunner {



    @Autowired
    private UserRepository cityRepository;

    @Override
    public void run(String... args) throws Exception {

        Location location = new Location();
        location.setArea1("Dhule");
        location.setCountry("India");
        location.setPostalCode("11111");

        //double[] locationCoord;
        double[] a = {74.7774223, 20.833993};
        location.setLocationCoord(a);

        UserOne one = new UserOne();
        //one.setLocation(location);


        /*
        {
              location: {
                             route: "Dhule Moghan Road1",
                             locality: "Ranmala",
                             area1: "Dhule",
                             area2: "Maharashtra",
                             country: "India",
                             postalCode: "424311",
                             locationCoord: [74.7774223, 20.833993]
              }
}
         */

       // cityRepository.save(one);

    }
}
