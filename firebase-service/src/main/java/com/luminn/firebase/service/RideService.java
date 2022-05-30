package com.luminn.firebase.service;

import com.luminn.firebase.dto.RideDTO;
import com.luminn.firebase.entity.*;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.repository.RideRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class RideService {


    @Autowired
    RideRepository rideRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    UserModel userModel;

    @Autowired
    UserMongoService userService;

    //public MongoCollection getConnection(){
      //  return mongoTemplate.getCollection("ride");
    //}

    public void save(RideDTO rideDTO){
        Ride ride = new Ride();

        ride.setSource(rideDTO.getSource());
        ride.setDesc(rideDTO.getDesc());

        rideRepository.save(ride);
    }

    public void add(String tripId,RideDTO rideDTO,String driverId,String userId){

        System.out.println(" RIDE SAVE ");
        Ride ride = new Ride();
        ride.setTripId(tripId);


        if(driverId != null)
            ride.setDriverIds(driverId);
        if(userId != null)
            ride.setUserIds(userId);

        Ride rs = Converter.dtoToentity(rideDTO,ride);
        rideRepository.save(rs);
    }

   /* public void update(){
        Query query1 = new Query(Criteria.where("id").is("123"));
        Update update1 = new Update();
        update1.set("available", false);
        rideRepository.updateFirst(query1, update1, Customer.class);
    }*/

    public void updateName(String Id,String name){
        MongoCollection collection = mongoTemplate.getCollection("ride");
        Document doc = findId(Id,collection);

        doc.entrySet().stream()
                .filter(ep -> ep.getKey().length() > 0)
                .forEach(ep -> {
                    System.out.println(" ep value " + ep.getKey() + " Value " + ep.getValue());

                    BasicDBObject updateFields = new BasicDBObject();
                    updateFields.append("desc", name);

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

    public Document findId(String id,MongoCollection collection) {

        System.out.println(" id-->_ " + id);

        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put("_id", new ObjectId(id));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("ride").find(queryObj);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        MongoCursor<Document> cursor = findIterable.iterator();
        if (cursor.hasNext()) {
            Document document = cursor.next();
            //return document.getString("identifier");
            System.out.println(" DATA " + cursor.toString() +  document.get("name") );
            return document;
        } else {
            System.out.println(" NO DATA 2 ");
            return null;
        }
    }



    public List<RideDTO> getAll() {

        List<RideDTO> listDto = new ArrayList<RideDTO>();
        /*List<Ride> rides = rideRepository.findAll();
        for(Ride rd: rides){
            RideDTO rideDTOs = new RideDTO();
            RideDTO dto =  Converter.entityToDTO(rd,rideDTOs);
            listDto.add(dto);
        }*/
        return  listDto;
    }


    public List<RideDTO> listByDrivers(String driverId){

        List<RideDTO> list = new ArrayList<>();
        BasicDBObject queryObj = new BasicDBObject();
        //queryObj.put("driverId", new ObjectId(driverId));
        queryObj.put("driverId", driverId);

        FindIterable<Document> findIterable = mongoTemplate.getCollection("ride").find(queryObj).sort(new BasicDBObject("startDate",-1)).limit(20);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc : findIterable){
            list.add(Converter.entityToDTO(doc,null));
        }
        return list;
    }

    public List<RideDTO> listBySupplierId(String supplierId){

        List<RideDTO> list = new ArrayList<>();
        BasicDBObject queryObj = new BasicDBObject();
        //queryObj.put("driverId", new ObjectId(driverId));
        queryObj.put("supplierId", supplierId);

        FindIterable<Document> findIterable = mongoTemplate.getCollection("ride").find(queryObj).sort(new BasicDBObject("startDate",-1)).limit(20);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc : findIterable){
            list.add(Converter.entityToDTO(doc,null));
        }
        return list;
    }

    public List<RideDTO> listBySupplierId(String supplierId,String status){

        List<RideDTO> list = new ArrayList<>();
        BasicDBObject queryObj = new BasicDBObject();
        //queryObj.put("driverId", new ObjectId(driverId));
        queryObj.put("supplierId", supplierId);
        queryObj.put("status", status);

        FindIterable<Document> findIterable = mongoTemplate.getCollection("ride").find(queryObj).sort(new BasicDBObject("startDate",-1)).limit(20);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc : findIterable){
            list.add(Converter.entityToDTO(doc,null));
        }
        return list;
    }

    public List<RideDTO> listByCreationDate(String date1){

        //db.collectionName.find({"start_date":{"$lte":new Date()}}).pretty()
        List<RideDTO> list = new ArrayList<>();

        final Calendar cal = Calendar.getInstance();
        final long time = cal.getTime().getTime();
        final Date date = cal.getTime();

        System.out.println(" current date cal " + cal + " time ;;; " +time + " ::: "+ date) ;
    
        final BasicDBObject query = new BasicDBObject("startDate", new BasicDBObject("$lte", date));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("ride").find(query).sort(new BasicDBObject("startDate",-1)).limit(20);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc : findIterable){
            list.add(Converter.entityToDTO(doc,null));
        }
        return list;
    }


    public List<RideDTO> listByuser(String userId){


        List<RideDTO> list = new ArrayList<>();

        BasicDBObject queryObj = new BasicDBObject();
        //queryObj.put("driverId", new ObjectId(driverId));
        queryObj.put("userId", userId);

        //sort(new BasicDBObject("_id",-1)).limit(1);
        FindIterable<Document> findIterable = mongoTemplate.getCollection("ride").find(queryObj).sort(new BasicDBObject("startDate",-1)).limit(10);

        if (findIterable == null) {
            System.out.println(" NO DATA ");
        }

        for(Document doc : findIterable){
            list.add(Converter.entityToDTO(doc,null));
        }
        return list;
    }
}
