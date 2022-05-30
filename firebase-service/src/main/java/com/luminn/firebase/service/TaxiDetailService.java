package com.luminn.firebase.service;

import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.TaxiDetailRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


@Service
public class TaxiDetailService {



    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private TaxiDetailRepository taxiDetailRepository;

    private MongoCollection<Document> collection;

    private static final Logger log = LoggerFactory.getLogger(TaxiDetailService.class);

    public void findNearest(double longitude, double latitude, double distance) {


        //List<GeoResult<UserOne>> result = template.geoNear(
        Point location = new Point(longitude, latitude);
        NearQuery.near(location).maxDistance(new Distance(1, Metrics.KILOMETERS))
                .query(new Query(Criteria.where("location"))
                        .limit(6));

    }
    //public MongoCollection getConnection(){
      //  return mongoTemplate.getCollection("taxiDetail");
    //}

    public TaxiDetail findTaxiId(String taxiId) {

        //MongoCollection  collection = getConnection();
        TaxiDetail taxiDetails = null;

        if(taxiId != null && !"".equalsIgnoreCase(taxiId)) {

            System.out.println(" ObjectId taxiId " + taxiId);

           if(ObjectId.isValid(taxiId)){

            BasicDBObject queryObj = new BasicDBObject();
            queryObj.put("taxiId", new ObjectId(taxiId));

                System.out.println(" ObjectId taxiId " + queryObj.toString());
                FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj);

                for (Document document : findIterable) {
                    taxiDetails = Converter.documentToTaxiDetail(document);
                    return taxiDetails;
                }
                if(findIterable == null || taxiDetails == null){
                   return new TaxiDetail();
                }

           }
           else
               return new TaxiDetail();
        }
        return new TaxiDetail();
    }

    public TaxiDetail findDriverId(String driverId) {

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("driverId", new ObjectId(driverId));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj);



        for(Document document : findIterable) {
            System.out.println(" document --> " + document.toString() );
            TaxiDetail taxiDetails = Converter.documentToTaxiDetail(document);
            return taxiDetails;
        }
        return null;
    }

    public List<TaxiDetail> findSupplierId(String taxiId) {


        List<TaxiDetail> list = new ArrayList<>();

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("supplierId", new ObjectId(taxiId));

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj).sort(new BasicDBObject("lastUpdate",-1)).limit(40);

        for(Document document : findIterable) {
            System.out.println(" document --> " + document.toString() );
            TaxiDetail taxiDetails = Converter.documentToTaxiDetail(document);
            list.add(taxiDetails);
        }
        return list;
    }

    public List<TaxiDetail> findSupplierIdNumberPlate(String taxiId,String numberPlate) {


        List<TaxiDetail> list = new ArrayList<>();

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("supplierId", new ObjectId(taxiId));
        queryObj.put("numberPlate", numberPlate);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj).sort(new BasicDBObject("lastUpdate",-1)).limit(40);

        for(Document document : findIterable) {
            System.out.println(" document --> " + document.toString() );
            TaxiDetail taxiDetails = Converter.documentToTaxiDetail(document);
            list.add(taxiDetails);
        }
        return list;
    }
    public List<TaxiDetail> findSupplierIdDriverId(String taxiId,String driverId) {


        List<TaxiDetail> list = new ArrayList<>();

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("supplierId", new ObjectId(taxiId));
        queryObj.put("driverId", new ObjectId(driverId));

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj).sort(new BasicDBObject("lastUpdate",-1)).limit(40);

        for(Document document : findIterable) {
            System.out.println(" document --> " + document.toString() );
            TaxiDetail taxiDetails = Converter.documentToTaxiDetail(document);
            list.add(taxiDetails);
        }
        return list;
    }

    public List<TaxiDetail> findSupplierIdandPhoneNumber(String taxiId,String driverId) {


        List<TaxiDetail> list = new ArrayList<>();

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("supplierId", new ObjectId(taxiId));
        queryObj.put("driverId", driverId);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj).sort(new BasicDBObject("lastUpdate",-1)).limit(40);

        for(Document document : findIterable) {
            System.out.println(" document --> " + document.toString() );
            TaxiDetail taxiDetails = Converter.documentToTaxiDetail(document);
            list.add(taxiDetails);
        }
        return list;
    }

    public TaxiDetail findBySupplierId(String taxiId) {


        List<TaxiDetail> list = new ArrayList<>();
        TaxiDetail raxiDetail = null;

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("supplierId", new ObjectId(taxiId));

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj);

        for(Document document : findIterable) {
            System.out.println(" document --> " + document.toString() );
           return  Converter.documentToTaxiDetail(document);

        }
        return raxiDetail;
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

    public Document findToken(String token) {

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("token", token);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj).limit(1);
        for(Document document : findIterable) {

            if(document.get("token") != null){
                System.out.println(" result1 -->" + document.get("token") );
                return document;
            }
        }
        return null;
    }

    public TaxiDetail finByDriverId(String driverId){

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("driverId", new ObjectId(driverId));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj).limit(1);
        for(Document document : findIterable) {
            TaxiDetail taxiDetails = Converter.documentToTaxiDetail(document);
            return taxiDetails;
        }
        return null;
    }

    public TaxiDetail finBySupplierId(String supplierId){


        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("supplierId", new ObjectId(supplierId));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("taxiDetail").find(queryObj).limit(1);
        for(Document document : findIterable) {
            TaxiDetail taxiDetails = Converter.documentToTaxiDetail(document);
            return taxiDetails;
        }
        return null;
    }




    /*public Document findId(String id,MongoCollection  collection) {


        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", new ObjectId(id));

        FindIterable<Document> findIterable = collection.find(queryObj);

        if (findIterable == null) {
           System.out.println(" NO DATA ");
        }

        MongoCursor<Document> cursor = findIterable.iterator();
        if (cursor.hasNext()) {
            Document document = cursor.next();
            //return document.getString("identifier");

                    //Converter.entityToDTO()
            System.out.println(" DATA " + cursor.toString() +  document.get("name") );
            return document;
        } else {
            System.out.println(" NO DATA 2 ");
            return null;
        }
    }*/

    public TaxiDetail findById(String id,MongoCollection  collection) {


        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", new ObjectId(id));

        FindIterable<Document> findIterable = collection.find(queryObj);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc :findIterable ){
            return   Converter.documentToTaxiDetail(doc);
        }

        return null;
    }

    //documentToTaxiDetail


    public Document findName(String name) {

        //MongoCollection  collection = mongoTemplate.getCollection("Taxi");

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("name", name);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("_id","name")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("Taxi").find(queryObj).limit(1);

        for(Document document : findIterable) {
            return document;
        }
        return null;
    }


    public boolean updateSupplierIds(String ids,String supplierId){

        //MongoCollection  collection = getConnection();

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("supplierIds", (supplierId));


        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ids));
        if (searchQuery != null) {
            mongoTemplate.getCollection("taxiDetail").updateOne(searchQuery, setQuery);
        }
        System.out.println(" -- updateTaxiId amd taxiId --");
        return true;
    }

    public boolean updateAccount(String ids,String accountNumber){

        //MongoCollection  collection = getConnection();
         TaxiDetail taxiDetail =  findTaxiId(ids);

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("account", accountNumber);



        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(taxiDetail.getId()));
        if (searchQuery != null) {
            mongoTemplate.getCollection("taxiDetail").updateOne(searchQuery, setQuery);
        }
        System.out.println(" 3-- updateTaxiId amd taxiId --");
        return true;
    }

    public boolean updateTaxiId(String ids,String taxiId,String supplierId,String userKey,int seats,String vehicleBrand,String taxiNumber,int vehicleYear){

        //MongoCollection  collection = getConnection();

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("driverId", new ObjectId(userKey));
        updateFields.append("taxiId", new ObjectId(taxiId));
        updateFields.append("supplierId", new ObjectId(supplierId));
        updateFields.append("supplierIds", (supplierId));

        updateFields.append("seats", seats);
        updateFields.append("brand", vehicleBrand);
        updateFields.append("numberPlate", taxiNumber);
        updateFields.append("year",vehicleYear);



        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ids));
        if (searchQuery != null) {
            mongoTemplate.getCollection("taxiDetail").updateOne(searchQuery, setQuery);
        }
        System.out.println(" 1-- updateTaxiId amd taxiId --");
        return true;
    }

    public boolean updateTaxiIdKm(String ids,int hrs,int km,int seats,String brand,String year,String taxiNumber){

        /*  taxiDetails.setHour(hour);
                        taxiDetails.setKm(km);
                        taxiDetails.setSeats(seats);
        */
       // MongoCollection  collection = getConnection();
        /*BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("driverId", new ObjectId(userKey));
        updateFields.append("taxiId", new ObjectId(taxiId));
        updateFields.append("supplierId", new ObjectId(supplierId));
        updateFields.append("supplierIds", (supplierId));

        updateFields.append("seats", seats);
        updateFields.append("brand", vehicleBrand);
        updateFields.append("numberPlate", taxiNumber);
        updateFields.append("year",vehicleYear);*/

        BasicDBObject updateFields = new BasicDBObject();
        if(hrs > 0)
            updateFields.append("hour", hrs);
        if(km > 0)
            updateFields.append("km", km);
        if(seats > 0)
            updateFields.append("seats", seats);
        if(brand != null)
            updateFields.append("brand", brand);
        if(year != null)
            updateFields.append("year", year);
        if(taxiNumber != null)
            updateFields.append("numberPlate", taxiNumber);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ids));
        if (searchQuery != null) {
            mongoTemplate.getCollection("taxiDetail").updateOne(searchQuery, setQuery);
        }
        System.out.println(" 11-- updateTaxiId amd taxiId --");
        return true;
    }

    public boolean updateTaxiActive(boolean active){


        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("active", active);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        //BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ids));
        //if (searchQuery != null) {
        //  mongoTemplate.getCollection("taxiDetail").updateOne(searchQuery, setQuery);
        //}
        return true;
    }

    public String updateActive(String id, String status){

        MongoCollection  collection = mongoTemplate.getCollection("Taxi");
        Document doc =   findName(id);

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("code", "8000");


        FindIterable<Document> findIterable = collection.find(queryObj);
        for(Document document : findIterable) {

            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("active", "true");

            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);

            if(document.get("_id") != null) {
                 BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(document.get("_id").toString()));
                    collection.updateOne(searchQuery, setQuery);
            }
        }
        return "success";
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

    public String addTaxiDetails(TaxiDetail taxiDetail){
        return taxiDetailRepository.insert(taxiDetail).getId();
    }

    public boolean deleteTaxiDetail(String id){
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("taxiId", new ObjectId(id));
        mongoTemplate.getCollection("taxiDetail").deleteOne(queryObj);
        return true;
    }
}
