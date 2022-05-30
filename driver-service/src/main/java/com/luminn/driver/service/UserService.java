package com.luminn.driver.service;

import com.luminn.driver.model.DriverOne;
import com.luminn.driver.model.Location;
import com.luminn.driver.model.UserOne;
import com.luminn.driver.repository.DriverLocationRepository;

import com.luminn.driver.repository.UserRepository;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class UserService  {
    @Autowired
    private MongoOperations mongoOperations;


    //implements UserInterface

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoCollection<Document> collection;

    public List<UserOne> getUsersByLocation(double latitude,double longitude, double distance){

        //double distanceInRad = 5.0 / 6371;
        //FindIterable<Document> result = collection.find(Filters.geoWithinCenterSphere("location", -0.1435083, 51.4990956, distanceInRad))

        List<UserOne> result = null;
        MongoCollection  collection = mongoTemplate.getCollection("driverone");

        System.out.println(" BEFORE FIALE "  );

        double distanceInRad = 0;
        FindIterable<Document> docs = collection.find(Filters.geoWithinCenterSphere("location",
                latitude,
                longitude,
                distanceInRad));

        Iterator it = docs.iterator();
        while (it.hasNext()) {
            System.out.println(" ---->> " + it.next());
        }

        for(Document doc1 : docs) {
            //access documents e.g. doc.get()
            //System.out.println(" result1 -->" );
            //Document{{_id=5fa3e103df95ec459bc3ee76, name=Oerloko, status=START, location=Document{{coordinates=[52.86884, 10.89236], type=Point}}}}
            System.out.println(" result1 -->" + docs.first().get("_id") );
        }



        //System.out.println(" result1 -->" + result1.first().g );

        System.out.println(" result1 -->" );
        return result;

    }

    public List<UserOne> getUsersByLocation3(double longitude, double latitude, double distance){

        List<UserOne> result = null;
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        Point p = new Point(longitude, latitude);
        NearQuery nearQuery  = NearQuery.near(p, Metrics.KILOMETERS).maxDistance(distance);
        //nearQuery.getCollation().
        System.out.println(" nearQuery collection" + nearQuery.getCollation());

        list.add(Aggregation.geoNear(nearQuery, "distance"));
        list.add(Aggregation.project("id", "location"));

        System.out.println(" list " + list.size());
        TypedAggregation<UserOne> agg = new TypedAggregation(UserOne.class, list);
        result = mongoOperations.aggregate(agg, UserOne.class).getMappedResults();
        return result;
    }



    public void findNearest(double longitude, double latitude, double distance) {


        //List<GeoResult<UserOne>> result = template.geoNear(
                Point location = new Point(longitude, latitude);
                NearQuery.near(location).maxDistance(new Distance(1, Metrics.KILOMETERS))
                        .query(new Query(Criteria.where("location"))
                                .limit(6));

    }
    public List<AggregationOperation> getByLocation(double longitude, double latitude, double distance){
        List<UserOne> result = null;
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();

        Point p = new Point(longitude, latitude);
        NearQuery nearQuery  = NearQuery.near(p, Metrics.KILOMETERS).maxDistance(distance);
        list.add(Aggregation.geoNear(nearQuery, "distance"));
        list.add(Aggregation.project("id", "location"));

        return list;

    }

    public void firstRecords(double longitude, double latitude, double distance){
        com.mongodb.client.model.geojson.Point currentLoc = new com.mongodb.client.model.geojson.Point(new Position(-0.126821, 51.495885));
        FindIterable<Document> result = collection.find(Filters.near("location", currentLoc, 1000.0, 10.0));
    }



    public void addUserOne1(double longitude, double latitude, double distance){
        Location location = new Location();
        location.setArea1("Dhule");
        location.setCountry("India");
        location.setPostalCode("11111");

        //double[] locationCoord;
        double[] a = {74.7774223, 20.833993};
        location.setLocationCoord(a);

        UserOne one = new UserOne();
        //one.setLocation(location);

        userRepository.save(one);
    }

    public void addUserOne(DriverOne driver){



        //collection.deleteMany(new Document());
        //collection.createIndex(Indexes.geo2dsphere("location"));
        //collection.insertOne(Document.parse("{'name':'Big Ben','location': {'coordinates':[-0.1268194,51.5007292],'type':'Point'}}"));
        //collection = mongoTemplate.getCollection("place");
        //mongoTemplate.insert(Document.parse("{'name':'Big Ben','location': {'coordinates':[-0.1268194,51.5007292],'type':'Point'}}"));
        //mongoTemplate.insert(u,"places")

       // DB db = mongoTemplate.getCollectionNames("place");
       //DBCollection collection = db.getCollection("dummyColl");
        /*
        JSONObject jo = new JSONObject();
        jo.put("company", login.getCompany());
        jo.put("user", login.getUser());
        jo.put("secure_password", login.getSecure_password());
        jo.put("secure_device_id", login.getSecure_device_id());

        Map m = new LinkedHashMap(3);
        m.put("os", app.getOs());
        m.put("ver", app.getVer());
        m.put("lang", app.getLang());

        jo.put("app_info", m);
        System.out.println(jo.toString)
         */
        MongoCollection  collection = mongoTemplate.getCollection("driverone");
        String json = "{'name':'" +driver.getName().toString() + "'," +
                        "'status':'" + driver.getStatus().toString() + "'," +
        //String json = "{'name':'dd', " +
                "'location': {'coordinates':" +
                "[" +
                String.valueOf(driver.getLatitude()) + "," +
                String.valueOf(driver.getLongitude()) +
                "]," +
                "'type':'Point'}}" ;

        collection.insertOne(Document.parse(json));

    }

    public void updateDriver(DriverOne driver){

        //FindIterable<document> cursor = db.getCollection("SOME_COLLECTION").find()sort(new Document("_id", -1)).limit(1);

        MongoCollection  collection = mongoTemplate.getCollection("driverone");
        String jsonLocation = "{'coordinates':" +
                "[" +
                String.valueOf(driver.getLatitude()) + "," +
                String.valueOf(driver.getLongitude()) +
                "]," +
                "'type':'Point'}}" ;
        collection.updateOne(Filters.eq("name", driver.getName()), Updates.set("location", jsonLocation));

    }
}
