package com.luminn.driver.service;

import com.luminn.driver.model.Driver;
import com.luminn.driver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DriverLocation {

    @Autowired
    DriverRepository driverRepository;

    public String saveDriverLocation(Driver driver) throws IOException {
        //save2Position
        driverRepository.save(driver);

        /*
           collection = db.getCollection("places");
            collection.deleteMany(new Document());
            collection.createIndex(Indexes.geo2dsphere("location"));
            collection.insertOne(Document.parse("{'name':'Big Ben','location': {'coordinates':[-0.1268194,51.5007292],'type':'Point'}}"));
            collection.insertOne(Document.parse("{'name':'Hyde Park','location': {'coordinates': [[[-0.159381,51.513126],[-0.189615,51.509928],[-0.187373,51.502442], [-0.153019,51.503464],[-0.159381,51.513126]]],'type':'Polygon'}}"));
         */


        return "Test";
    }
}
