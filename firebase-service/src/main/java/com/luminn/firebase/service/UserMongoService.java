package com.luminn.firebase.service;

import com.luminn.firebase.dto.MobileDTO;
import com.luminn.firebase.dto.UserDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.security.HashCodeUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;

import org.bson.Document;
import org.bson.types.ObjectId;
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
import com.luminn.firebase.helper.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


@Service
public class UserMongoService {



    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    private MongoCollection<Document> collection;

    @Autowired
    private HashCodeUtils hashCodeUtils;

    @Autowired
    private TaxiService taxiService;

    @Autowired
    private TaxiDetailService taxiDetailService;

    @Autowired
    UserRepository userRepository;


    public void findNearest(double longitude, double latitude, double distance) {


        //List<GeoResult<UserOne>> result = template.geoNear(
        Point location = new Point(longitude, latitude);
        NearQuery.near(location).maxDistance(new Distance(1, Metrics.KILOMETERS))
                .query(new Query(Criteria.where("location"))
                        .limit(6));

    }

    public Document findToken(String token) {

       // MongoCollection  collection = getCollection();

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("token", token);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("user").find(queryObj).limit(1);
        for(Document document : findIterable) {

            if(document.get("token") != null){
                System.out.println(" result1 -->" + document.get("token") );
                return document;
            }
        }
        return null;
    }

    public User findKey(String key, String value) {


        List<User> userList = new ArrayList<>();
        User user = null;
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put(key, value);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> collections =   mongoTemplate.getCollection("user").find(queryObj).limit(1);
        for(Document doc : collections){

            user = Converter.documentToUser(doc);
            userList.add(user);
            return user;
        }
        return user;
    }

    public User findKeyDriver(String key, String value) {


        List<User> userList = new ArrayList<>();
        User user = null;
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put(key, value);
        queryObj.put("ROLE", "ROLE_DRIVER");

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> collections =   mongoTemplate.getCollection("user").find(queryObj).limit(1);
        for(Document doc : collections){

            user = Converter.documentToUser(doc);
            userList.add(user);
            return user;
        }
        return user;
    }

    public List<User> findKeyMany(String key, String value) {


        List<User> userList = new ArrayList<>();
        User user = null;
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put(key, value);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> collections =   mongoTemplate.getCollection("user").find(queryObj).limit(1);
        for(Document doc : collections){

            user = Converter.documentToUser(doc);
            userList.add(user);
            return userList;
        }
        return userList;
    }

    public FindIterable<Document> findPhoneNumber(String key,String value) {


        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("phoneNumber", value);

        return  mongoTemplate.getCollection("user").find(queryObj).limit(1);

    }

    public List<User> findDuplicateKey(String key,String value) {



        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put(key, value);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("token")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("user").find(queryObj);

        User user = null;
        List<User> list = new ArrayList<>();
        for(Document document : findIterable) {

            if(document.get("email") != null){
                user = Converter.getDocumentToObject(document);
                list.add(user);
            }
        }
        return list;
    }


    public User getToken(String token) {
        Document document = findToken(token);
        User taxi = new User();

        if(document != null){
            taxi.setName(document.get("name").toString());
            taxi.setToken(document.get("token").toString());
            return taxi;
        }
        return taxi;
    }


    public List<User> getDuplicateToken(String token) {
        Document document = findToken(token);
        User user = new User();
        List<User> UserList = new ArrayList<>();
        if(document != null){
            if(document.get("name") != null)
                user.setName(document.get("name").toString());
            if(document.get("token") != null)
                user.setToken(document.get("token").toString());
        }
        UserList.add(user);
        return UserList;
    }

    public User findByEmail(String email) {

        User user  = findKey("email",email);

        return user;
    }

    public User findByPhoneNumber(String phoneNumber) {

        //User user  = findKeyDriver("phoneNumber",phoneNumber);
        User user  = findKey("phoneNumber",phoneNumber);
        return user;
    }

    public User findByPhoneNumber(String phoneNumber,String role) {

        User user  = findKeyDriver("phoneNumber",phoneNumber);

        return user;
    }
    public User findByPhoneNumberDriver(String phoneNumber) {
        User user  = findKey("phoneNumber",phoneNumber);
        return user;
    }
    public User findBySocail(String social) {

        User user  = findKey("socialId",social);

        return user;
    }

    public List<User> getEmailDuplicate(String email) {

        List<User> findIterable = findDuplicateKey("email",email);
        return findIterable;
    }

    public List<User> getPhoneNumber(String phoneNumber) {

        FindIterable<Document> find = findPhoneNumber("phoneNumber",phoneNumber);
        Document document = null;


        for(Document doc : find) {
            if(doc.get("phoneNumber") != null){
                document = doc;
            }
        }

        //later it is array
        List<User> user = new ArrayList<>();
        User usr = null;

        if(document != null){
             usr = new User();
             if(document.get("phoneNumber") != null) {
                 usr = com.luminn.firebase.helper.Converter.getDocumentToObject(document);
             }
             else
                 usr.setName("NODATA");
            user.add(usr);
        }
        return user;
    }


    public User checkGetByPhoneNumber(String phoneNumber, String role, String domain) {
        List<User> users = getPhoneNumber(phoneNumber);


        if (users != null && users.size() > 0) {
            for (User user : users) {
                /*if(user.get(i) != null && (user.get(i).getWebSite() == null || "".equalsIgnoreCase(user.get(i).getWebSite())) && user.get(i).getRole().equalsIgnoreCase(role)){
                    return user.get(i);
                }
                else if (user.get(i).getRole().equalsIgnoreCase(role) && user.get(i).getWebSite() != null && user.get(i).getWebSite().equalsIgnoreCase(domain)) {
                    return user.get(i);
                } //only 1 phone number - unique domain doesnt matter
                */
                 return user;
            }
        } else if (users == null) {
            User emtpyuser = new User();
            return emtpyuser;
        }
        User emtpyuser = new User();
        return emtpyuser;

    }

    public User getById(String id){
        return findId(id);
    }

    public User getByIdWithoutObjectId(String id){
        return findIdWithoutObject(id);
    }

    public User findId(String id) {

        //MongoCollection  collection = getCollection();
        User user = null;

        System.out.println(" id-->_ " + id);

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", new ObjectId(id));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("user").find(queryObj);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc : findIterable){
            user = Converter.documentToUser(doc);
        }
        return user;
    }

    public String updateDeleteFlag(String id) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;
        System.out.println("updateDeleteFlag id-->_ " + id);



        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("isDeleted", "Y");

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);


        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public String updatePassword(String id,String password) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;
        System.out.println("password id-->_ " + id);



        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("password", password);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);


        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public String updatePhoneVerified(String id,String phoneVerified) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;
        System.out.println("password id-->_ " + id);



        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("phoneVerified", phoneVerified);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);


        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public String delete(String id) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;






        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").deleteMany(searchQuery);
        }
        return "Succes";
    }

    public String updateTokenfromId(String id,String token, String notificationToekn) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;
        System.out.println(" id-->_ " + id);



        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("token", token);
        updateFields.append("notificationToken", notificationToekn);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);


        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public String updateResetKeyId(String id,String key) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;
        System.out.println(" id-->_ " + id);



        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("passwordResetKey", key);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public String updateValue(String id,String status) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;
        System.out.println(" id-->_ " + id);



        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("status", status);

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public String updateSetting(UserDTO dto) {

        //MongoCollection  collection = getCollection();
        User user = null;
        Document userDoc = null;
        System.out.println(" id-->_ " + dto.getId());

        BasicDBObject updateFields = new BasicDBObject();
        if(dto.getEmail() != null && !"".equalsIgnoreCase(dto.getEmail()))
            updateFields.append("email", dto.getEmail());
        if(dto.getPhoneNumber() != null && !"".equalsIgnoreCase(dto.getPhoneNumber()))
            updateFields.append("phoneNumber", dto.getPhoneNumber());
        if(dto.getFirstName() != null && !"".equalsIgnoreCase(dto.getFirstName()))
            updateFields.append("firstName", dto.getFirstName());
        if(dto.getLastName() != null && !"".equalsIgnoreCase(dto.getLastName()))
            updateFields.append("lastName", dto.getLastName());

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(dto.getId()));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public String updateNameSetting(String id, String firstName, String lastName,String phoneNumber) {

        //MongoCollection  collection = getCollection();

        BasicDBObject updateFields = new BasicDBObject();
        if(!"".equalsIgnoreCase(firstName) && firstName != null)
            updateFields.append("firstName", firstName);
        if(!"".equalsIgnoreCase(lastName) && lastName != null)
            updateFields.append("lastName", lastName);
        if(!"".equalsIgnoreCase(phoneNumber) && phoneNumber != null)
            updateFields.append("phoneNumber", phoneNumber);



        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(id));
        if (searchQuery != null) {
            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
        }
        return "Succes";
    }

    public User findIdWithoutObject(String id) {

        //MongoCollection  collection = getCollection();
        User user = null;

        System.out.println(" id-->_ " + id);

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", id);

        FindIterable<Document> findIterable = mongoTemplate.getCollection("user").find(queryObj);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc : findIterable){
            user = Converter.documentToUser(doc);
        }
        return user;
    }


 /*   public void updateName(String Id,String name){
        MongoCollection  collection = getCollection();
        Document doc = findId(Id);
        //Document doc =   findName(name);

        doc.entrySet().stream()
                .filter(ep -> ep.getKey().length() > 0)
                .forEach(ep -> {
                    System.out.println(" ep value " + ep.getKey() + " Value " + ep.getValue());

                    BasicDBObject updateFields = new BasicDBObject();
                    updateFields.append("name", name);

                    BasicDBObject setQuery = new BasicDBObject();
                    setQuery.append("$set", updateFields);

                    if(ep.getKey().startsWith("_id")) {
                        BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(ep.getValue().toString()));
                        if (searchQuery != null) {
                            collection.updateOne(searchQuery, setQuery);
                        }
                    }
                });
    }*/



   // public MongoCollection getCollection(){
     //  return mongoTemplate.getCollection("user");
    //}

    public Document findName(String name) {



        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("name", name);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("_id","name")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("user").find(queryObj).limit(1);

        for(Document document : findIterable) {
            return document;
        }
        return null;
    }

    public String findNameId(String name) {

        //MongoCollection  collection = getCollection();

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("name", name);

        //FindIterable<Document> findIterable = collection.find(queryObj).projection(include("_id","name")).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("user").find(queryObj).limit(1);

        for(Document document : findIterable) {
            return document.get("_id").toString();
        }
        return null;
    }

    public void updateToken(String name,String token){


     //MongoCollection  collection = mongoTemplate.getCollection("user");
      Document doc =   findName(name);

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
                            mongoTemplate.getCollection("user").updateOne(searchQuery, setQuery);
                        }
                    }

                });
    }



    public List<User> getUsersByLocation(double latitude, double longitude, double distance){

        //double distanceInRad = 5.0 / 6371;
        //FindIterable<Document> result = collection.find(Filters.geoWithinCenterSphere("location", -0.1435083, 51.4990956, distanceInRad))

        List<User> result = null;
        MongoCollection  collection = mongoTemplate.getCollection("user");

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

    public void addUserOne(User driver){
        MongoCollection  collection = mongoTemplate.getCollection("user");
        String json = "{'name':'" +driver.getName().toString() + "'," +
                "'status':'" + driver.getStatus().toString() + "'," +
                "'token':'" + driver.getToken().toString() + "'," +
                //String json = "{'name':'dd', " +
                "'location': {'coordinates':" +
                "[" +
                String.valueOf(driver.getLongitude()) + "," +
                String.valueOf(driver.getLatitude()) +
                "]," +
                "'type':'Point'}}" ;
        collection.insertOne(Document.parse(json));
    }

    public String addUserOneRegistration(MobileDTO user,boolean facebook,String socialId){


     if(!facebook) {
         String generatedPassword = hashCodeUtils.generatePasswordFirstTime(user.getPassword());
         String json = "{'category':'" + user.getCategory().toString() + "'," +
                 "'domain':'" + user.getDomain() + "'," +
                 "'email':'" + user.getEmail() + "'," +
                 "'token':'" + user.getToken() + "'," +
                 "'firstName':'" + user.getFirstName() + "'," +
                 "'lastName':'" + user.getLastName() + "'," +
                 "'name':'" + user.getFirstName() + "'," +
                 "'role':'" + user.getRole() + "'," +
                 "'password':'" + generatedPassword + "'," +
                 "'phoneNumber':'" + user.getPhoneNumber().toString() + "'," +
                 "'website':'" + user.getWebsite() + "'," +
                 "'createdDate':'" + new Date() + "'," +
                 "'isDeleted':'" + 'N' + "'," +
                 "'location': {'coordinates':" +
                 "[" +
                 String.valueOf(user.getLongitude()) + "," +
                 String.valueOf(user.getLatitude()) +
                 "]," +
                 "'type':'Point'}}";
         InsertOneResult rs = mongoTemplate.getCollection("user").insertOne(Document.parse(json));

         return rs.getInsertedId().asObjectId().getValue().toString();
     }
     else {
         //String generatedPassword = hashCodeUtils.generatePasswordFirstTime(user.getPassword());
         String email = null;
         String phoneNumber = null;
                if(user.getPhoneNumber() != null){
                    phoneNumber =   user.getPhoneNumber().toString();
                }
                if(user.getEmail() != null){
                    email = user.getEmail();
                }
                System.out.println(" user.getId() "+ user.getId());
         String json = "{ 'domain':'" + user.getDomain() + "'," +
                 "'email':'" + email + "'," +
                 "'token':'" + user.getToken() + "'," +
                 "'firstName':'" + user.getFirstName() + "'," +
                 "'lastName':'" + user.getLastName() + "'," +
                 "'name':'" + user.getFirstName() + "'," +
                 "'role':'" + user.getRole() + "'," +
                 //"'password':'" + generatedPassword + "'," +
                 "'phoneNumber':'" + phoneNumber + "'," +
                 "'socialId':'" + socialId + "'," +
                 "'website':'" + user.getWebsite() + "'," +
                 "'createdDate':'" + new Date() + "'," +
                 "'isDeleted':'" + 'N' + "'," +
                 "'location': {'coordinates':" +
                 "[" +
                 String.valueOf(user.getLongitude()) + "," +
                 String.valueOf(user.getLatitude()) +
                 "]," +
                 "'type':'Point'}}";
         InsertOneResult rs = mongoTemplate.getCollection("user").insertOne(Document.parse(json));

         return rs.getInsertedId().asObjectId().getValue().toString();

     }
        //java.lang.IllegalArgumentException: invalid hexadecimal representation of an ObjectId:
        //return rs.getInsertedId().asObjectId().toString();
    }






    public void addUserOneRegistration(User user){

        MongoCollection  collection = mongoTemplate.getCollection("user");

        String generatedPassword = hashCodeUtils.generatePasswordFirstTime(user.getPassword());

        String json = "{'category':'" +user.getCategory().toString() + "'," +
                "'domain':'" + user.getDomain() + "'," +
                "'email':'" + user.getEmail() + "'," +
                "'token':'" + user.getToken().toString() + "'," +
                "'firstName':'" + user.getFirstName() + "'," +
                "'lastName':'" + user.getLastName() + "'," +
                "'name':'" + user.getName() + "'," +
                "'role':'" + user.getRole() + "'," +
                "'password':'" + generatedPassword + "'," +
                "'phoneNumber':'" + user.getPhoneNumber().toString() + "'," +
                "'phoneVerified':'" + user.getPhoneVerified().toString() + "'," +
                "'status':'" + user.getStatus() + "'," +
                "'website':'" + user.getWebsite() + "'," +
                "'location': {'coordinates':" +
                "[" +
                String.valueOf(user.getLatitude()) + "," +
                String.valueOf(user.getLongitude()) +
                "]," +
                "'type':'Point'}}" ;
        collection.insertOne(Document.parse(json));

        //Document child2 =
        /*Document child2 = new Document("coordinates", driver.getLatitude() + "," + driver.getLongitude());
        //Document child = new Document("location", child2);
        Document document = new Document("name", driver.getName())
                .append("status", driver.getStatus())
                .append("token", driver.getToken())
                .append("location", child2)
                .append("type","Point");
        System.out.println(".. document -->" + document.toString());
        collection.insertOne(document);*/
    }

    public void updateDriver(User driver){

        //FindIterable<document> cursor = db.getCollection("SOME_COLLECTION").find()sort(new Document("_id", -1)).limit(1);
        MongoCollection  collection = mongoTemplate.getCollection("driverone");
        String jsonLocation = "{'coordinates':" +
                "[" +
                String.valueOf(driver.getLatitude()) + "," +
                String.valueOf(driver.getLongitude()) +
                "]," +
                "'type':'Point'}}" ;
        collection.updateOne(eq("name", driver.getName()), Updates.set("location", jsonLocation));

    }
}
