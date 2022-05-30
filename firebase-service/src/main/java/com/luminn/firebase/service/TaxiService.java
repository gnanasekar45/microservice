package com.luminn.firebase.service;

import com.luminn.firebase.controller.NotificationController;
import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.dto.TaxiSearchDTO;
import com.luminn.firebase.dto.TaxisDTO;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.model.NotificationModel;
import com.luminn.firebase.model.PushNotificationRequests;
import com.luminn.firebase.repository.TaxiRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.request.SearchDTO;
import com.luminn.firebase.util.Path;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.client.result.InsertOneResult;

import org.bson.BsonValue;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.*;


@Service
public class TaxiService {



    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private TaxiDetailService taxiDetailService;

    @Autowired
    DriverPhotoService driverPhotoService;

    @Autowired
    NotificationModel notificationModel;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaxiRepository taxiRepository;

    @Autowired
    UserMongoService userService;

    private MongoCollection<Document> collection;

    private static final Logger log = LoggerFactory.getLogger(TaxiService.class);

    public void findNearest(double longitude, double latitude, double distance) {


        //List<GeoResult<UserOne>> result = template.geoNear(
        //Point location = new Point(longitude, latitude);
        //NearQuery.near(location).maxDistance(new Distance(1, Metrics.KILOMETERS))
          //      .query(new Query(Criteria.where("location"))
            //            .limit(6));

    }

    /*public MongoCollection getConnection(){
        return mongoTemplate.getCollection("Taxi");
    }*/
    public Document findToken(String token) {


        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("token", token);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("Taxi").find(queryObj).limit(1);
        for(Document document : findIterable) {

            if(document.get("token") != null){
                System.out.println(" result1 -->" + document.get("token") );
                return document;
            }
        }
        return null;
    }

    public Taxi getToken(String token) {
        Document document = findToken(token);
        Taxi taxi = new Taxi();

        if(document != null){
            taxi.setName(document.get("name").toString());
            taxi.setToken(document.get("token").toString());
            return taxi;
        }
        return taxi;
    }


    public Taxi findId(String id,MongoCollection  collection) {

        Taxi taxi = null;
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", new ObjectId(id));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("Taxi").find(queryObj);
        if (findIterable == null) {
           System.out.println(" NO DATA ");

        }
        for(Document doc : findIterable){
            taxi =  Converter.documentToTaxi(doc);
        }
        return taxi;
    }


    public boolean updateApprove(String id,boolean flag){



        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("active", flag);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("Taxi").updateOne(searchQuery, setQuery);
        }
        System.out.println(" 66-- updateTaxiId amd taxiId --");
        return true;
    }




    public Document findName(String name) {


        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("name", name);
        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("_id","name")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("Taxi").find(queryObj).limit(1);

        for(Document document : findIterable) {
            return document;
        }
        return null;
    }

    public void updateName(String name,String token){


     /* MongoCollection  collection = mongoTemplate.getCollection("Taxi");
      Document doc =   findId(name,collection);

      doc.entrySet().stream()
                .filter(ep -> ep.getKey().length() > 0)
                .forEach(ep -> {

                    System.out.println(" ep value " + ep.getKey() + " Value " + ep.getValue());

                    BasicDBObject updateFields = new BasicDBObject();
                    updateFields.append("token", token);

                    BasicDBObject setQuery = new BasicDBObject();
                    setQuery.append("$set", updateFields);

                    if(ep.getKey().startsWith("_id")) {
                        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ep.getValue().toString()));
                        if (searchQuery != null) {
                            collection.updateOne(searchQuery, setQuery);
                        }
                    }

                });*/
    }

    public boolean deleteTaxi(String id){
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", new ObjectId(id));
        mongoTemplate.getCollection("Taxi").deleteOne(queryObj);
        return true;
    }


    public void updateStatus(String id, String status){

        MongoCollection  collection = mongoTemplate.getCollection("Taxi");
        Document doc =   findName(id);

        doc.entrySet().stream()
                .filter(ep -> ep.getKey().length() > 0)
                .forEach(ep -> {

                    System.out.println(" ep value " + ep.getKey() + " Value " + ep.getValue());

                    BasicDBObject updateFields = new BasicDBObject();
                    updateFields.append("status", status);

                    BasicDBObject setQuery = new BasicDBObject();
                    setQuery.append("$set", updateFields);

                    if(ep.getKey().startsWith("_id")) {
                        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ep.getValue().toString()));
                        if (searchQuery != null) {
                            collection.updateOne(searchQuery, setQuery);
                        }
                    }

                });
    }


    public List<TaxiSearchDTO> getUsersByLocation(float latitude, float longitude, double distance){

        //double distanceInRad = 5.0 / 6371;
        //FindIterable<Document> result = collection.find(Filters.geoWithinCenterSphere("location", -0.1435083, 51.4990956, distanceInRad))

        List<TaxiSearchDTO> result = new ArrayList<>();
        MongoCollection  collection = mongoTemplate.getCollection("Taxi");

        System.out.println(" BEFORE FIALE "  );

        double distanceInRad = 0;
        FindIterable<Document> docs = collection.find(Filters.geoWithinCenterSphere("location",
                latitude,
                longitude,
                distanceInRad));

        System.out.println(" result1 -->" + docs.toString() );
        for(Document doc : docs) {
            //access documents e.g. doc.get()
            //System.out.println(" result1 -->" );
            //Document{{_id=5fa3e103df95ec459bc3ee76, name=Oerloko, status=START, location=Document{{coordinates=[52.86884, 10.89236], type=Point}}}}
            System.out.println(" result1 -->" + doc.get("_id") );
            TaxiSearchDTO search = Converter.documentToTaxiSearch(doc);
            result.add(search);
        }
        //System.out.println(" result1 -->" + result1.first().g );
        System.out.println(" result1 -->" );
        return result;
    }




    public boolean updateTaxiType(String taxiId,String taxiType){


        //Taxi taxi =   findId(taxiId,null);

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("type", taxiType);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(taxiId));
        if (searchQuery != null) {
            mongoTemplate.getCollection("Taxi").updateOne(searchQuery, setQuery);
        }
        System.out.println(" 99 -- updateTaxiId amd taxiId --");
        return true;
    }

    public void updateLocations(String id,double latitude,double longtitude,String status){

        //MongoCollection  collection = getConnection();
        //Optional<Taxi> optionalTaxi = taxiRepository.findById(id);



        BasicDBList coords= new BasicDBList();
        coords.add(Double.valueOf(longtitude));
        coords.add(Double.valueOf(latitude));


        BasicDBObject loc2 = new BasicDBObject("type", "Point").append("coordinates", (coords));

        mongoTemplate.getCollection("Taxi").updateOne(eq("_id", new ObjectId(id)),combine(set("location", loc2),set("status", status),set("lastUpdate",new Date())));
        log.info(" updateLocations id -->" + id);

    }

    public void updateLocation(double latutude, double longtude){

        double latLong[] = {latutude, longtude};
        JSONArray jsonArray = new JSONArray(latLong);
        JSONObject jobj = new JSONObject().put("type", "point");
        jobj.put("coordinates", jsonArray);

        // below  jsonObject_loc contains the jsonobject as you want..
        JSONObject jsonObject_loc = new JSONObject();
        jsonObject_loc.put("location", jobj);
        System.out.println(jsonObject_loc);

        // but you have to store jobj in db as your query already has 'loc' object
        BasicDBObject loc = new BasicDBObject();
        loc.put("location", jobj.toString());
    }


    public String addTaxiOne(Taxi driver){
        //MongoCollection  collection = mongoTemplate.getCollection("Taxi");
         String token = null;
        String name = null;

         if(driver.getToken() != null){
             token = driver.getToken();
         }
         else
             token = "EMPTY";


        if(driver.getName() != null){
            token = driver.getName();
        }
        else
            token = "NAME";

       //collection.createIndex(Indexes.geo2dsphere("location"));

        String json = "{'name':'" + driver.getName() + "'," +
                "'status':'" + driver.getStatus() + "'," +
                "'code':'" + driver.getCode() + "'," +
                "'type':'" + driver.getCarType() + "'," +
                "'category':'" + driver.getCategory() + "'," +
                "'active':'" + "false" + "'," +
                "'lastUpdate':'" + driver.getLastUpdate() + "'," +
                "'location': {'coordinates':" +
                "[" +
                Double.valueOf(driver.getLongitude()) + "," +
                Double.valueOf(driver.getLatitude()) +
                "]" +
                "'type':'Point'}}" ;

        InsertOneResult rs = mongoTemplate.getCollection("Taxi").insertOne(Document.parse(json));

       // collection.insertOne(Document.parse("{'name':'Hyde Park','location': {'coordinates': [[[-0.159381,51.513126],[-0.189615,51.509928],[-0.187373,51.502442], [-0.153019,51.503464],[-0.159381,51.513126]]],'type':'Polygon'}}"));
        return rs.getInsertedId().asObjectId().getValue().toString();

        //FAILED
        //BsonValue bs =  rs.getInsertedId();
        //return bs.asDocument().get("_id").asObjectId().getValue().toString();
    }


    public void addTaxiDetails(Taxi taxi){

        String taxiId = addTaxiOne(taxi);
        TaxiDetail td = new TaxiDetail();
        td.setTaxiId(taxiId);
        taxiDetailService.addTaxiDetails(td);

    }

    public String addTaxiDetails(TaxiDetail taxidetail){
        return taxiDetailService.addTaxiDetails(taxidetail);
    }


    public void updateDriver(Taxi driver){

        //FindIterable<document> cursor = db.getCollection("SOME_COLLECTION").find()sort(new Document("_id", -1)).limit(1);

        MongoCollection  collection = mongoTemplate.getCollection("driverone");
        String jsonLocation = "{'coordinates':" +
                "[" +
                String.valueOf(driver.getLatitude()) + "," +
                String.valueOf(driver.getLongitude()) +
                "]," +
                "'type':'Point'}}" ;
        collection.updateOne(eq("name", driver.getName()), set("location", jsonLocation));

    }
}
