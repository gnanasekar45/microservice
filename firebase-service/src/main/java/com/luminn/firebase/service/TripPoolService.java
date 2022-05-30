package com.luminn.firebase.service;

import com.luminn.firebase.dto.TripPool;
import com.luminn.firebase.entity.RIDESTATUS;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.PushNotificationRequests;
import com.luminn.firebase.model.RideModel;
import com.luminn.firebase.repository.TripPoolRepository;
import com.luminn.firebase.request.TripPoolRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
@Log4j2
public class TripPoolService {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    RideModel rideModel;

    @Autowired
    TripPoolRepository tripPoolRepository;

    @Autowired
    TaxiDetailService taxiDetailService;

    HashMap<String, String> hashMap = null;

    private static final Logger log = LoggerFactory.getLogger(TripPoolService.class);

    public void addUserOne(TripPool trip) {
        MongoCollection collection = mongoTemplate.getCollection("tripPool");

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("status", trip.getStatus());
        queryObj.put("token", trip.getToken());

        collection.insertOne(queryObj);
    }

    public Document getToken(String token) {

        MongoCollection collection = mongoTemplate.getCollection("tripPool");

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("token",token);

        FindIterable<Document> findIterable = collection.find(queryObj).limit(1);
        for(Document document : findIterable) {
            return document;
        }
        return new Document();
    }
    public MongoCollection getCollection(){
        MongoCollection collection = mongoTemplate.getCollection("tripPool");
        return collection;
    }

    public Document getTripId(String token, MongoCollection collection) {


        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("tripId",token);
        //queryObj.put("status",status);

        FindIterable<Document> findIterable = collection.find(queryObj).limit(1);
        for(Document document : findIterable) {
            return document;
        }
        return new Document();
    }

    public boolean check(){
        return true;
    }
    public Document getStatus(String status) {

        MongoCollection collection = mongoTemplate.getCollection("tripPool");

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("status",status);

        FindIterable<Document> findIterable = collection.find(queryObj).limit(1);
        for(Document document : findIterable) {
            return document;
        }
        return new Document();
    }

    public Document findTripId(String tripId,String status) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(tripId,status);
        MongoCollection  collection = mongoTemplate.getCollection("tripPool");

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("tripId", tripId);
        queryObj.put("status", status);

        FindIterable<Document> findIterable = collection.find(queryObj).limit(1);
        for(Document document : findIterable) {
            return document;
        }
        return new Document();
    }

    public Document getDocument(FindIterable<Document> findIterable){
        for(Document document : findIterable) {
            return document;
        }
        return null;
    }



    public ModelStatus updateTripId(TripPoolRequest tripPoolRequest) {


        MongoCollection  collection = getCollection();
        Document document = getTripId(tripPoolRequest.getTripId(),collection);

        if(document != null)
       {

            log.info(" BEFORE SAVE ---->" );

            if(document.entrySet().stream() != null) {
                rideModel.add(tripPoolRequest);
            }

            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("status", tripPoolRequest.getStatus());

            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);

            BasicDBObject searchQuery = new BasicDBObject("_id", document.get("_id"));
            if (searchQuery != null) {
                collection.updateOne(searchQuery, setQuery);
            }
            System.out.println(" -- updateTaxiId amd taxiId --");

            return ModelStatus.SUCCESS;
        }
        return ModelStatus.EXCEPTION;
    }

    public ModelStatus updateUserConfirmation(TripPoolRequest tripPoolRequest) {


        MongoCollection  collection = getCollection();
        Document document = getTripId(tripPoolRequest.getTripId(),collection);

        if(document != null)
        {

            log.info(" BEFORE SAVE ---->" );


            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("status", tripPoolRequest.getStatus());

            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);

            BasicDBObject searchQuery = new BasicDBObject("_id", document.get("_id"));
            if (searchQuery != null) {
                collection.updateOne(searchQuery, setQuery);
            }
            System.out.println(" -- updateTaxiId amd taxiId --");

            return ModelStatus.SUCCESS;
        }
        return ModelStatus.EXCEPTION;
    }

    public Document findTripId(String tripId) {

        MongoCollection  collection = mongoTemplate.getCollection("tripPool");

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("tripId", tripId);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("_id","name")).limit(1);
        FindIterable<Document> findIterable = collection.find(queryObj).limit(1);

        for(Document document : findIterable) {
            return document;
        }
        return null;
    }



    public Document findDate(String tripId) {

        MongoCollection  collection = mongoTemplate.getCollection("tripPool");

        BasicDBObject queryObj = new BasicDBObject();
        //queryObj.put(new BasicDBObject("$gte", fromDate));
        //queryObj.put(new BasicDBObject("$lte", toDate));
        //LocalDate fromDate = LocalDate.now().minusDays(-2);
        //LocalDate toDate = LocalDate.of(2020, 1, 31);

        LocalDate fromDate = LocalDate.now();
        Date fromDate2 = java.util.Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant());
        Date toDate = new Date();

        queryObj.put("timestamp", BasicDBObjectBuilder.start("$gte", fromDate2).add("$lte", toDate).get());

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("_id","name")).limit(1);
        //query.put("dateAdded", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());

        FindIterable<Document> findIterable = mongoTemplate.getCollection("tripPool").find(queryObj).limit(1);

        System.out.println("findIterable ---> " + findIterable + " " + findIterable.first());

        for(Document document : findIterable) {
            return document;
        }




        Document doc = findIterable.first();
        System.out.println(" DATE --12 " + findIterable.first() );
        doc.entrySet().stream()
                .filter(ep -> ep.getKey().length() > 0)
                .forEach(ep -> {

                    System.out.println(" ep value " + ep.getKey() + " Value " + ep.getValue());



                });
        return null;
    }


    public void updateTrip(TripPool trip){

        MongoCollection  collection = mongoTemplate.getCollection("tripPool");

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("status",trip.getStatus());

        Document doc =   findTripId(trip.getTripId());
        doc.entrySet().stream()
                .filter(ep -> ep.getKey().length() > 0)
                .forEach(ep -> {
                    System.out.println(" ep value " + ep.getKey() + " Value " + ep.getValue());

                    BasicDBObject updateFields = new BasicDBObject();
                    updateFields.append("status", trip.getStatus());

                    BasicDBObject setQuery = new BasicDBObject();
                    setQuery.append("$set", updateFields);

                    if(ep.getKey().startsWith("_id")) {
                        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ep.getValue().toString()));
                        if (searchQuery != null) {
                            collection.updateOne(searchQuery, setQuery);
                        }
                    }

                });

        System.out.println(" update is completed ");
    }

    public String getSupplierId(String driverId){
        TaxiDetail taxiDetail = taxiDetailService.finByDriverId(driverId);
        if(taxiDetail != null)
            return taxiDetail.getSupplierId();
        else
            return "";
    }

    //This is not using it, remove this
    public String addTrip(PushNotificationRequests trip){


        String status = "NEW";
        String supplierId = "";
        if(trip.getDriverId() != null)
         supplierId = getSupplierId(trip.getDriverId());

        String json = "{'driverId':'" +trip.getDriverId() + "'," +
                "'userId':'" + trip.getUserId() + "'," +
                "'tripId':'" + trip.getTripId() + "'," +
                "'status':'" + status + "'," +
                "'token':'" + trip.getToken() +  "'," +
                "'source':'" + trip.getSource() +  "'," +
                "'desc':'" + trip.getDes() +  "'," +
                "'km':'" + trip.getKm() +  "'," +
                "'supplierId':'" + supplierId +  "'," +
                "'timestamp':'" + new Date() +  "'" +
                "}" ;

        System.out.println(" json --> " + json);
        InsertOneResult rs = mongoTemplate.getCollection("tripPool").insertOne(Document.parse(json));
        return rs.getInsertedId().asObjectId().getValue().toString();
    }

    public String addTripPool(PushNotificationRequests trip){

        //add supplierId
        String supplierId = "";
        if(trip.getDriverId() != null)
            supplierId = getSupplierId(trip.getDriverId());

        TripPool tripPool = Converter.toDTO(trip);
        tripPool.setSupplierId(supplierId);
        tripPool.setStatus("NEW");

        return tripPoolRepository.save(tripPool).getId();
    }

    public String addTripOld(TripPool trip){

        MongoCollection  collection = mongoTemplate.getCollection("tripPool");
        String uniqueID = UUID.randomUUID().toString();

        String json = "{'driverId':'" +trip.getId() + "'," +
                "'tripId':'" + uniqueID + "'," +
                "'status':'" + trip.getStatus() + "'," +
                "'token':'" + trip.getToken() +  "'," +
                "'timestamp':'" + new Date() +  "'" +
                "}" ;

        System.out.println(" json --> " + json);
        collection.insertOne(Document.parse(json));
        return uniqueID;
    }
}
