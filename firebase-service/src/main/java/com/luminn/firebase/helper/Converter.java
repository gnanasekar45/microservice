package com.luminn.firebase.helper;


import com.google.gson.Gson;
import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.*;
import com.luminn.firebase.model.PushNotificationRequests;
import com.luminn.firebase.request.LongDTO;
import com.luminn.firebase.request.SearchDTO;
import com.luminn.firebase.util.DRIVERSTATUS;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.Validation;
import com.luminn.firebase.view.TaxiView;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public final class Converter {


    public static TripDTO entityTripToDTO(TripPool trip, TripDTO tripDTO) {
        if (tripDTO == null) {
            tripDTO = new TripDTO();
            //rideDTO.setId(ride.getIds());
            tripDTO.setId(trip.getId());
            tripDTO.setSource(trip.getSource());
            tripDTO.setDestination(trip.getDes());
            tripDTO.setKm(trip.getKm());
            tripDTO.setPrice(trip.getPrice());
            tripDTO.setDate(trip.getCreatedDate().toString());
            tripDTO.setTripId(trip.getTripId());

        } else
            tripDTO.setId(trip.getId());
        return tripDTO;
    }

    public static TripDetailDTO entityTripDetailToDTO(TripPool trip, TripDetailDTO tripDTO) {
        if (tripDTO == null) {
            tripDTO = new TripDetailDTO();
            //rideDTO.setId(ride.getIds());
            tripDTO.setId(trip.getId());
            tripDTO.setSource(trip.getSource());
            tripDTO.setDestination(trip.getDes());
            tripDTO.setKm(trip.getKm());
            tripDTO.setPrice(trip.getPrice());
            tripDTO.setDate(trip.getCreatedDate().toString());
            tripDTO.setTripId(trip.getTripId());

        } else
            tripDTO.setId(trip.getId());
        return tripDTO;
    }

      public static RideDTO entityToDTO(Ride ride, RideDTO rideDTO) {
        if (rideDTO == null) {
            rideDTO = new RideDTO();
            //rideDTO.setId(ride.getIds());
            if(ride !=null && ride.getId() != null)
                rideDTO.setId(ride.getId());

        } else
            rideDTO.setId(ride.getId());


        //null or o - not set a value
        // if (Validation.isEmpty(ride.getId()))
        //    rideDTO.setId(ride.getId());
        if(ride != null){
        if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.RESERVED))
            rideDTO.setRideStatus(RIDESTATUS.RESERVED);
        if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.ACCEPTED))
            rideDTO.setRideStatus(RIDESTATUS.ACCEPTED);
        else if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.REJECT))
            rideDTO.setRideStatus(RIDESTATUS.REJECT);
        else if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.CANCEL))
            rideDTO.setRideStatus(RIDESTATUS.CANCEL);
        else if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.CHANGE))
            rideDTO.setRideStatus(RIDESTATUS.CHANGE);
        else if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.FINISH))
            rideDTO.setRideStatus(RIDESTATUS.FINISH);
        else if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.PRECANCEL_BY_USER))
            rideDTO.setRideStatus(RIDESTATUS.PRECANCEL_BY_USER);
        else if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.PRECANCEL_BY_DRIVER))
            rideDTO.setRideStatus(RIDESTATUS.PRECANCEL_BY_DRIVER);
        else if (ride.getStatus() != null && ride.getStatus().equals(RIDESTATUS.CANCEL_BY_USER))
            rideDTO.setRideStatus(RIDESTATUS.CANCEL_BY_USER);

        if (ride.getStatus() != null && ride.getSource() != null && !"".equals(ride.getSource()))
            rideDTO.setSource(ride.getSource());
        if (ride.getStatus() != null && ride.getDestination() != null && !"".equals(ride.getDestination()))
            rideDTO.setDestination(ride.getDestination());


        if (ride.getLatitude() > 0)
            rideDTO.setLatitude(ride.getLatitude());
        if (ride.getLongitude() > 0)
            rideDTO.setLongitude(ride.getLongitude());

        if (ride.getPrice() > 0)
            rideDTO.setPrice(ride.getPrice());

        if (ride.getTotalPrice() > 0)
            rideDTO.setTotalPrice(ride.getTotalPrice());
        if (ride.getUserTotalPrice() > 0)
            rideDTO.setUserTotalPrice(ride.getUserTotalPrice());
        if (ride.getTravelTime() > 0)
            rideDTO.setTravelTime(ride.getTravelTime());
        if (ride.getDesc() != null)
            rideDTO.setDesc(ride.getDesc());
        if (ride.getEndDate()!= null)
            rideDTO.setEndTIme((ride.getEndDate().toString()));

        if (ride.getKm() > 0.0)
            rideDTO.setKm(ride.getKm());
        if (ride.getBasePrice() > 0)
            rideDTO.setBase(ride.getBasePrice());
        if(ride.getPayment() != null)
            rideDTO.setPayment(Payment.valueOf(ride.getPayment()));
        if(ride.getType()!= null)
            rideDTO.setType(ride.getType());
        if(ride.getCategory()!= null)
            rideDTO.setCategory(ride.getCategory());
        if(ride.getTax() > -1)
            rideDTO.setTax(ride.getTax());
        if(ride.getPercentage() > -1)
            rideDTO.setPercentage(ride.getPercentage());
        if(ride.getUserId() != null)
            rideDTO.setUserId(ride.getUserId());
        if(ride.getDriverId() != null)
            rideDTO.setDriverId(ride.getDriverId());
        }
        //review.setUpdatedOn(reviewDTO.getUpdatedOn());
        return rideDTO;
    }

    public static RideDTO entityToDTO(Document document, RideDTO rideDTO) {

        if (rideDTO == null) {
            rideDTO = new RideDTO();
            //rideDTO.setId(ride.getIds());
            rideDTO.setId(document.get("_id").toString());

        } else
            rideDTO.setId(document.get("_id").toString());


        //null or o - not set a value
        // if (Validation.isEmpty(ride.getId()))
        //    rideDTO.setId(ride.getId());

        if (document.get("status") != null) {
            if (document.get("status").toString().equals(RIDESTATUS.RESERVED.name()))
                rideDTO.setRideStatus(RIDESTATUS.RESERVED);
            else
                rideDTO.setRideStatus(RIDESTATUS.RESERVED);
            if (document.get("status") != null && document.get("status").toString().equals(RIDESTATUS.ACCEPTED))
                rideDTO.setRideStatus(RIDESTATUS.ACCEPTED);
            else if (document.get("status") != null && document.get("status").toString().equals(RIDESTATUS.REJECT))
                rideDTO.setRideStatus(RIDESTATUS.REJECT);
            else if (document.get("status") != null && document.get("status").toString().equals(RIDESTATUS.CANCEL))
                rideDTO.setRideStatus(RIDESTATUS.CANCEL);
            else if (document.get("status") != null && document.get("status").toString().equals(RIDESTATUS.CHANGE))
                rideDTO.setRideStatus(RIDESTATUS.CHANGE);
        }
        if (document.get("payment") != null && document.get("payment").toString().equals(Payment.CARD.name()))
            rideDTO.setPayment(Payment.CARD);
        if (document.get("payment") != null && document.get("payment").toString().equals(Payment.CASH.name()))
            rideDTO.setPayment(Payment.CASH);

        if (document.get("source").toString() != null && !"".equals(document.get("source").toString() != null))
            rideDTO.setSource(document.get("source").toString());
        if (document.get("destination").toString() != null && !"".equals(document.get("destination").toString()))
            rideDTO.setDestination(document.get("destination").toString());

        if(document.get("driverId")!= null)
            rideDTO.setDriverId(document.get("driverId").toString());
        if(document.get("userId")!= null)
            rideDTO.setUserId(document.get("userId").toString());
        if(document.get("type")!= null)
            rideDTO.setType(document.get("type").toString());
        if(document.get("category")!= null)
            rideDTO.setCategory(document.get("category").toString());
        if(document.get("startDate")!= null) {
            try {
                rideDTO.setStartTIme((document.get("startDate").toString()));
            }
            catch(Exception e){
                System.out.println(" End Date -->" + document.get("startDate"));
            }
        }
        if(document.get("endDate")!= null) {
            try {
                rideDTO.setEndTIme((document.get("endDate").toString()));
            }
            catch(Exception e){
                System.out.println(" End Date -->" + document.get("endDate"));
            }
        }


        rideDTO.setLatitude(converStringFloat("latitude", document));
        rideDTO.setLongitude(converStringFloat("longitude", document));

        rideDTO.setPrice(converStringFloat("price", document));

        rideDTO.setTotalPrice(converStringFloat("totalPrice", document));

        rideDTO.setUserTotalPrice(converStringFloat("userTotalPrice", document));

        rideDTO.setTravelTime(converStringFloat("travelTime", document));

        rideDTO.setDesc(valiudateString("desc", document));

        //if (ride.getUpdatedOn() != null)
        //    rideDTO.setUpdatedOn(ride.getUpdatedOn());
        rideDTO.setKm(converStringFloat("km", document));
        rideDTO.setBase(converStringFloat("basePrice", document));

        rideDTO.setTax(converStringFloat("tax", document));
        rideDTO.setPercentage(converStringFloat("percentage", document));

        /*if (ride.getKm() > 0.0)
            rideDTO.setKm(ride.getKm());
        if (ride.getBasePrice() > 0)
            rideDTO.setBase(ride.getBasePrice());*/
        //review.setUpdatedOn(reviewDTO.getUpdatedOn());*/*/


        return rideDTO;
    }

    public static DriverBillingDetailsDTO entityBillToDto(DriverBill userInfo) {

        DriverBillingDetailsDTO userInfoDTO = new DriverBillingDetailsDTO();

        if (userInfo != null && userInfo.getDriverId() != null) {

            userInfoDTO.setCredit(userInfo.getCredit());
            userInfoDTO.setPaymentType(userInfo.getPaymentType());
            userInfoDTO.setDriverId(userInfo.getDriverId());
            userInfoDTO.setBalance(userInfo.getBalance());

            userInfoDTO.setDebit(userInfo.getDebit());
            userInfoDTO.setId((userInfo.getId()));

            return userInfoDTO;
        } else
            return userInfoDTO;
    }

    public static DriverBillingDTO entityToDto(DriverBill userInfo) {

        DriverBillingDTO userInfoDTO = new DriverBillingDTO();

        if (userInfo != null && userInfo.getDriverId() != null) {

            userInfoDTO.setCredit(userInfo.getCredit());
            userInfoDTO.setPaymentType(userInfo.getPaymentType());
            userInfoDTO.setDriverId(userInfo.getDriverId());
            userInfoDTO.setBalance(userInfo.getBalance());

            userInfoDTO.setDebit(userInfo.getDebit());
            userInfoDTO.setId((userInfo.getId()));

            return userInfoDTO;
        } else
            return userInfoDTO;
    }

    public static ReviewDTO entityToDto(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setTaxiDetailId(review.getTaxiDetailId());
        if (review.getComment() != null)
            reviewDTO.setComment(review.getComment());

        reviewDTO.setRating(review.getRating());

        //CK
        //if(review.getFormattedDate() != null)
        //   reviewDTO.setFormattedDate(review.getFormattedDate());

        reviewDTO.setUserId(review.getUserId());
        review.setUpdatedOn(review.getUpdatedOn());
        return reviewDTO;
    }

    public static Review dtoToentity(ReviewDTO reviewDTO, Review review) {
        if (review == null)
            review = new Review();
        //null or o - not set a value
        //if (Validation.isEmpty(reviewDTO.getId()))
        if (reviewDTO.getId() != null)
            review.setId(reviewDTO.getId());
        if (reviewDTO.getTaxiDetailId() != null && reviewDTO.getTaxiDetailId() != null)
            review.setTaxiDetailId(reviewDTO.getTaxiDetailId());
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setFormattedDate(reviewDTO.getFormattedDate());
        if (reviewDTO.getUserId() != null)
            review.setUserId(reviewDTO.getUserId());
        if (reviewDTO.getDriverId() != null && reviewDTO.getDriverId() != null)
            review.setDriverId(reviewDTO.getDriverId());
        if (reviewDTO.getPostedBy() != null)
            review.setPostedBy(reviewDTO.getPostedBy());
        if (reviewDTO.getRideId() != null)
            review.setRideId(reviewDTO.getRideId());
        //review.setUpdatedOn(reviewDTO.getUpdatedOn());
        return review;

    }

    private static float converStringFloat(String key, Document document) {

        if (document != null) {
            if (document.get(key).toString() != null)
                return Float.valueOf(document.get(key).toString());
        }
        return 0f;
    }

    private static String valiudateString(String key, Document document) {

        if (document != null && document.get(key) != null) {
            if (document.get(key).toString() != null)
                return (document.get(key).toString());
        }
        return null;
    }

    public static Ride dtoToentity(RideDTO rideDTO, Ride ride) {
        if (ride == null)
            ride = new Ride();
        //null or o - not set a value
        //if(!Validation.isEmpty(rideDTO.getId()))
        //   ride.setId(rideDTO.getId());

        if (rideDTO.getUserId() != null)
            ride.setUserId(rideDTO.getUserId());
        if (rideDTO.getDriverId() != null)
            ride.setDriverId(rideDTO.getDriverId());

        ride.setStatus(rideDTO.getRideStatus());

        if (rideDTO.getSource() != null && !"".equals(rideDTO.getRideStatus()))
            ride.setSource(rideDTO.getSource());
        else
            ride.setSource("Source");

        if (rideDTO.getDestination() != null && !"".equals(rideDTO.getDestination()))
            ride.setDestination(rideDTO.getDestination());
        else
            ride.setSource("Destination");

        if (rideDTO.getLatitude() > 0)
            ride.setLatitude(rideDTO.getLatitude());
        if (rideDTO.getLongitude() > 0)
            ride.setLongitude(rideDTO.getLongitude());

        if (rideDTO.getPrice() > 0)
            ride.setPrice(rideDTO.getPrice());
        if (rideDTO.getTotalPrice() > 0)
            ride.setTotalPrice(rideDTO.getTotalPrice());
        ride.setDesc(rideDTO.getDesc());
        ride.setEstimateTime(rideDTO.getEstimateTime());
        //ride.setUserName(rideDTO.getUserName());
        //ride.setDriverName(rideDTO.getDriverName());
        //review.setUpdatedOn(reviewDTO.getUpdatedOn());
        return ride;
    }


    public static Supplier dtoToEntity(SupplierDTO dto, Supplier supplier) {

        if (supplier != null) {
            //supplier.setAddress(dto.getAddress());

            supplier.setLicenseNumber(dto.getLicenseNumber());
            // supplier.setPhoneNumber(dto.getPhoneNumber());
            supplier.setName(dto.getName());
            //supplier.setWebsite(dto.getWebsite());
            // supplier.setLatitude(dto.getLatitude());
            //supplier.setLongitude(dto.getLongitude());
        }
        return supplier;
    }

    public static Supplier dtoToEntity(Document document) {

        Supplier supplier = new Supplier();

        if (supplier != null) {
            //supplier.setAddress(dto.getAddress());
           String id = document.get("_id").toString();
            supplier.setId(id);


            // supplier.setPhoneNumber(dto.getPhoneNumber());
            supplier.setName(document.get("name").toString());
            //supplier.setWebsite(dto.getWebsite());
            // supplier.setLatitude(dto.getLatitude());
            //supplier.setLongitude(dto.getLongitude());
        }
        return supplier;
    }

    public static User documentToUser(Document document) {

        User user = new User();
        //if(document.get("name") != null)
        //    user.setFirstName(document.get("name").toString());
        if(document.get("firstName") != null)
            user.setFirstName(document.get("firstName").toString());
        if(document.get("lastName") != null)
            user.setLastName(document.get("lastName").toString());
        if(document.get("token") != null)
            user.setToken(document.get("token").toString());
        if(document.get("notificationToken") != null)
            user.setNotificationToken(document.get("notificationToken").toString());
        if(document.get("role") != null)
            user.setRole(document.get("role").toString());
        if(document.get("email") != null)
            user.setEmail(document.get("email").toString());
        if(document.get("phoneNumber") != null)
            user.setPhoneNumber(document.get("phoneNumber").toString());
        if(document.get("domain") != null)
            user.setDomain(document.get("domain").toString());
        if(document.get("passwordResetKey") != null)
            user.setPasswordResetKey(document.get("passwordResetKey").toString());

        user.setId(document.get("_id").toString());
        return user;
    }



    public static User getDocumentToObject(Document document) {
        User user = new User();

        if(document.get("email") != null)
            user.setEmail(document.get("email").toString());
        if(document.get("token")!= null)
            user.setToken(document.get("token").toString());
        if(document.get("role")!= null)
            user.setRole(document.get("role").toString());
        if(document.get("phoneNumber") != null)
            user.setPhoneNumber(document.get("phoneNumber").toString());
        if(document.get("password") != null)
            user.setPassword(document.get("password").toString());
        if(document.get("firstName") != null)
            user.setName(document.get("firstName").toString());
         if(document.get("_id")!= null)
            user.setId(document.get("_id").toString());
        return user;
    }

    public static TaxiDetail documentToTaxiDetail(Document taxiDetail) {

        TaxiDetail taxiDetailDTO = new TaxiDetail();
        taxiDetailDTO.setId(taxiDetail.get("_id").toString());

        if (taxiDetail.get("taxiId") != null)
            taxiDetailDTO.setTaxiId(taxiDetail.get("taxiId").toString());

        if(taxiDetail.get("supplierIds") != null && taxiDetail.get("supplierIds").toString() != null){
            taxiDetailDTO.setSupplierId(taxiDetail.get("supplierIds").toString());
            System.out.println(" supplierIds ");
        }
        else if (taxiDetail.get("supplierIds") == null && taxiDetail.get("supplierId") != null) {
            taxiDetailDTO.setSupplierId(taxiDetail.get("supplierId").toString());
            System.out.println(" supplierId ");
        }

        if (taxiDetail.get("driverId") != null)
            taxiDetailDTO.setDriverId(taxiDetail.get("driverId").toString());

        if (taxiDetail.get("name") != null)
            taxiDetailDTO.setName(taxiDetail.get("name").toString());

        if (taxiDetail.get("brand") != null)
            taxiDetailDTO.setVehicleBrand(taxiDetail.get("brand").toString());

        if (taxiDetail.get("numberPlate") != null)
            taxiDetailDTO.setNumberPlate(taxiDetail.get("numberPlate").toString());

        if (taxiDetail.get("year") != null ) {
            int value = 0;
            //int yearvalue = (Integer) taxiDetail.get("year");
            try {

                 value = Integer.parseInt(taxiDetail.get("year").toString());
                taxiDetailDTO.setYear(value);
            }
            catch (NumberFormatException e){
                System.out.println("NumberFormatException: " + e.getMessage());
            }

        }
        if (taxiDetail.get("basePrice") != null) {
            double value = 0.0;
            double valuedouble = (double)taxiDetail.get("basePrice");
            try {
               value =   (Double.valueOf(valuedouble));
            }
            catch (Exception e){

            }
            taxiDetailDTO.setBasePrice(value);
        }

        if (taxiDetail.get("km") != null) {
            Integer km = null;
            int kmvalue = (int)taxiDetail.get("km");
            try {
                km =   Integer.valueOf(kmvalue);
            }
            catch (Exception e){

            }
            taxiDetailDTO.setKm(km);
        }

        if (taxiDetail.get("seats") != null) {
            Integer seats = null;

            int seatsvalue = (int)taxiDetail.get("seats");
            try {
                seats =   Integer.valueOf(seatsvalue);
            }
            catch (Exception e){

            }
            taxiDetailDTO.setSeats(seats);
        }
        // if(taxiDetail.get("acVehicle") != null)
       //     taxiDetailDTO.setAcVehicle(new Boolean(taxiDetail.get("acVehicle")));

        /* "_id" -> {ObjectId@13806} "5ffd4a76237d815a456a7a38"
 "taxiId" -> {ObjectId@13808} "5ffd4a76237d815a456a7a37"
 "tags" -> {ArrayList@13810}  size = 0
 "acVehicle" -> {Boolean@13812} false
 "maxPassengerCapacity" -> {Integer@13814} 0
 "basePrice" -> {Double@13816} 0.0
 "waitingTime" -> {Double@13818} 0.0
 "miniMumFare" -> {Double@13820} 0.0
 "year" -> "2020"
 "seats" -> {Integer@13824} 14
 "hour" -> {Integer@13814} 0
 "km" -> {Integer@13814} 0
 "transporttype" -> "pointtopoint"
 "price" -> {Double@13830} 0.0
 "peakPrice" -> {Double@13832} 0.0
 "airPortprice" -> {Double@13834} 0.0
 "currency" -> "CHF"
 "_class" -> "com.luminn.firebase.entity.TaxiDetail"
 "driverId" -> {ObjectId@13840} "5ffd4a76237d815a456a7a36"
 "supplierId" -> {ObjectId@13842} "5ff52f22ed56290361db1900"
 "brand" -> "Honda"
 "numberPlate" -> "TN3333"*/


        return taxiDetailDTO;
    }


    public static TaxiSearchDTO documentToTaxiSearch(Document taxiDoc) {

        //List<> s = new ArrayList<>();
        float[] latlong = new float[2];
        TaxiSearchDTO taxi = new TaxiSearchDTO();
        taxi.setId(taxiDoc.get("_id").toString());

        if (taxiDoc.get("type") != null)
            taxi.setCarType(taxiDoc.get("type").toString());

        //if (taxiDoc.get("category") != null)
         //   taxi.setC(taxiDoc.get("category").toString());


        if (taxiDoc.get("status") != null)
            taxi.setStatus(taxiDoc.get("status").toString());

        if (taxiDoc.get("name") != null)
            taxi.setName(taxiDoc.get("status").toString());

        if (taxiDoc.get("phonenumber") != null)
            taxi.setPhoneNumber(taxiDoc.get("phonenumber").toString());

        if (taxiDoc.get("location") != null) {

            //FindIterable<Document>
            //Document d =
            Document d = (Document) taxiDoc.get("location");

            d.get("coordinates");
            System.out.println("" + d.get("coordinates"));

            //Document d = (Document)d.get("location");

            final ArrayList<?> jsonArray = new Gson().fromJson(d.get("coordinates").toString(), ArrayList.class);
            System.out.println(" jsonArray size" + jsonArray.size());


            Iterator<?> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {

                String latlomg = iterator.next().toString();
                System.out.println(latlomg);
                latlong[i] = Float.valueOf(latlomg);
                i++;
            }

            //log("\nArrayList: " + jsonArray);

            //gradesCollection.find(gte("student_id", 10000));
        }
        if (latlong[0] > 0)
            taxi.setLatitude(latlong[0]);
        if (latlong[1] > 0)
            taxi.setLongitude(latlong[1]);

        return taxi;
    }

    public static Taxi documentToTaxis(Document taxiDoc) {

        //List<> s = new ArrayList<>();
        System.out.println(" " + taxiDoc.toString());
        float[] latlong = new float[2];
        Taxi taxi = new Taxi();
        taxi.setId(taxiDoc.get("_id").toString());

        if (taxiDoc.get("type") != null)
            taxi.setCarType(taxiDoc.get("type").toString());

        if (taxiDoc.get("category") != null)
         taxi.setCategory(CATEGORY.valueOf(taxiDoc.get("category").toString()));


        if (taxiDoc.get("status") != null)
            taxi.setStatus(DRIVERSTATUS.valueOf(taxiDoc.get("status").toString()));

        if (taxiDoc.get("name") != null)
            taxi.setName(taxiDoc.get("status").toString());

        if (taxiDoc.get("active") != null) {
            System.out.println(" ---" + taxiDoc.get("active"));
            if(taxiDoc.get("active").equals("true")){
                taxi.setActive(true);
            }
            if(taxiDoc.get("active").toString().equals("true")){
                taxi.setActive(true);
            }
            else
                taxi.setActive(false);
        }


        if (taxiDoc.get("location") != null) {

            Document d = (Document) taxiDoc.get("location");

            d.get("coordinates");
            System.out.println("" + d.get("coordinates"));

            //Document d = (Document)d.get("location");

            final ArrayList<?> jsonArray = new Gson().fromJson(d.get("coordinates").toString(), ArrayList.class);
            System.out.println(" jsonArray size" + jsonArray.size());


            Iterator<?> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {

                String latlomg = iterator.next().toString();
                System.out.println(latlomg);
                latlong[i] = Float.valueOf(latlomg);
                i++;
            }

        }
        if (latlong[0] > 0)
            //taxi.setLatitude(latlong[0]);
            taxi.setLongitude(latlong[0]);
        if (latlong[1] > 0)
           // taxi.setLongitude(latlong[1]);
            taxi.setLatitude(latlong[1]);

        return taxi;
    }


    public static TaxisDTO documentToTaxiSearch(Document taxiDoc,String image) {

        //List<> s = new ArrayList<>();
        float[] latlong = new float[2];
        TaxisDTO taxi = new TaxisDTO();
        taxi.setId(taxiDoc.get("_id").toString());

        if (taxiDoc.get("type") != null)
            taxi.setCarType(taxiDoc.get("type").toString());


        if (taxiDoc.get("status") != null)
            taxi.setStatus(taxiDoc.get("status").toString());

        if (taxiDoc.get("name") != null)
            taxi.setName(taxiDoc.get("name").toString());



        if (taxiDoc.get("location") != null) {

            //FindIterable<Document>
            //Document d =
            Document d = (Document) taxiDoc.get("location");

            d.get("coordinates");
            System.out.println("" + d.get("coordinates"));

            final ArrayList<?> jsonArray = new Gson().fromJson(d.get("coordinates").toString(), ArrayList.class);

            Iterator<?> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {

                String latlomg = iterator.next().toString();
                System.out.println(latlomg);
                latlong[i] = Float.valueOf(latlomg);
                System.out.println(" lat or long --->" + latlong[i]);
                i++;
            }
        }

            taxi.setLongitude(latlong[0]);
            taxi.setLatitude(latlong[1]);

        if (taxiDoc.get("lastUpdate") != null) {
            System.out.println(" lastUpdate --->" + taxiDoc.get("lastUpdate"));
        }
        return taxi;
    }

    public static TaxiPriceDTO documentToTaxiPriceSearch(Document taxiDoc,String image) {

        //List<> s = new ArrayList<>();
        float[] latlong = new float[2];
        TaxiPriceDTO taxi = new TaxiPriceDTO();
        taxi.setId(taxiDoc.get("_id").toString());

        if (taxiDoc.get("type") != null)
            taxi.setCarType(taxiDoc.get("type").toString());
        if (taxiDoc.get("category") != null)
            taxi.setCategory(taxiDoc.get("category").toString());


        if (taxiDoc.get("status") != null)
            taxi.setStatus(taxiDoc.get("status").toString());

        if (taxiDoc.get("name") != null)
            taxi.setName(taxiDoc.get("name").toString());




        if (taxiDoc.get("location") != null) {

            //FindIterable<Document>
            //Document d =
            Document d = (Document) taxiDoc.get("location");

            d.get("coordinates");
            System.out.println("" + d.get("coordinates"));

            final ArrayList<?> jsonArray = new Gson().fromJson(d.get("coordinates").toString(), ArrayList.class);

            Iterator<?> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {

                String latlomg = iterator.next().toString();
                System.out.println(latlomg);
                latlong[i] = Float.valueOf(latlomg);
                System.out.println(" lat or long --->" + latlong[i]);
                i++;
            }
        }

        taxi.setLongitude(latlong[0]);
        taxi.setLatitude(latlong[1]);

        if (taxiDoc.get("lastUpdate") != null) {
            System.out.println(" lastUpdate --->" + taxiDoc.get("lastUpdate"));
        }
        return taxi;
    }

    public static TaxiCompanyDTO documentToTaxiCompanySearch(Document taxiDoc,String image) {

        //List<> s = new ArrayList<>();
        float[] latlong = new float[2];
        TaxiCompanyDTO taxi = new TaxiCompanyDTO();
        taxi.setId(taxiDoc.get("_id").toString());

        if (taxiDoc.get("type") != null)
            taxi.setCarType(taxiDoc.get("type").toString());
        if (taxiDoc.get("category") != null)
            taxi.setCategory(taxiDoc.get("category").toString());


        if (taxiDoc.get("status") != null)
            taxi.setStatus(taxiDoc.get("status").toString());

        if (taxiDoc.get("name") != null)
            taxi.setName(taxiDoc.get("name").toString());




        if (taxiDoc.get("location") != null) {

            //FindIterable<Document>
            //Document d =
            Document d = (Document) taxiDoc.get("location");
            d.get("coordinates");

            final ArrayList<?> jsonArray = new Gson().fromJson(d.get("coordinates").toString(), ArrayList.class);

            Iterator<?> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {

                String latlomg = iterator.next().toString();
                System.out.println(latlomg);
                latlong[i] = Float.valueOf(latlomg);
                System.out.println(" lat or long --->" + latlong[i]);
                i++;
            }
        }

        taxi.setLongitude(latlong[0]);
        taxi.setLatitude(latlong[1]);

        if (taxiDoc.get("lastUpdate") != null) {
            System.out.println(" lastUpdate --->" + taxiDoc.get("lastUpdate"));
        }
        return taxi;
    }
    /*public static TaxisDTO documentToTaxiDateSearch(Document taxiDoc,String image) {

        //List<> s = new ArrayList<>();
        float[] latlong = new float[2];
        TaxisOnlineDTO taxi = new TaxisOnlineDTO();
        taxi.setId(taxiDoc.get("_id").toString());

        if (taxiDoc.get("type") != null)
            taxi.setCarType(taxiDoc.get("type").toString());

        if (taxiDoc.get("status") != null)
            taxi.setStatus(taxiDoc.get("status").toString());

        if (taxiDoc.get("name") != null)
            taxi.setName(taxiDoc.get("name").toString());



        if (taxiDoc.get("location") != null) {

            //FindIterable<Document>
            //Document d =
            Document d = (Document) taxiDoc.get("location");

            d.get("coordinates");
            System.out.println("" + d.get("coordinates"));

            final ArrayList<?> jsonArray = new Gson().fromJson(d.get("coordinates").toString(), ArrayList.class);

            Iterator<?> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {

                String latlomg = iterator.next().toString();
                System.out.println(latlomg);
                latlong[i] = Float.valueOf(latlomg);
                System.out.println(" lat or long --->" + latlong[i]);
                i++;
            }
        }

        taxi.setLongitude(latlong[0]);
        taxi.setLatitude(latlong[1]);

        if (taxiDoc.get("lastUpdate") != null) {
            System.out.println(" lastUpdate --->" + taxiDoc.get("lastUpdate"));
        }

        return taxi;
    } */

    public static DriverBill dtoToEntityInfo(DriverBillingDTO userInfoDTO, DriverBill entity,String supplierId) {

        if (entity == null) {
            entity = new DriverBill();
            entity.setDriverId(userInfoDTO.getDriverId());
            entity.setCredit(userInfoDTO.getCredit());
            entity.setPaymentType(userInfoDTO.getPaymentType());
            entity.setSupplierId(supplierId);
            return entity;
        }
        return entity;

    }

    public static AuditBill dtoToEntityInfoClone(DriverBill userInfoDTO) {

        AuditBill entity = null;
        entity = new AuditBill();
        entity.setDriverId(userInfoDTO.getDriverId());
        entity.setCredit(userInfoDTO.getCredit());
        entity.setPaymentType(userInfoDTO.getPaymentType());
        entity.setBalance(userInfoDTO.getBalance());
        entity.setComments("reduced/credit by user wallet");
        return entity;
    }
    public static AuditBill dtoToEntityInfoClone(DriverBillingDTO userInfoDTO) {

        AuditBill entity = null;
        if (entity == null) {
            entity = new AuditBill();
            entity.setDriverId(userInfoDTO.getDriverId());
            entity.setCredit(userInfoDTO.getCredit());
            entity.setPaymentType(userInfoDTO.getPaymentType());
            entity.setBalance(userInfoDTO.getBalance());
            return entity;
        }
        else {
                entity.setDriverId(userInfoDTO.getDriverId());
                entity.setCredit(userInfoDTO.getCredit());
                entity.setPaymentType(userInfoDTO.getPaymentType());
                entity.setBalance(userInfoDTO.getBalance());
                return entity;

        }

    }

    public static Taxi documentToTaxi(Document taxiDoc) {

        Taxi taxiSearch = documentToTaxis(taxiDoc);
        return taxiSearch;

    }

    public static Taxi dtoToEntity(TaxiDTO taxiDTO, Taxi entity) {

        if (!isEmpty(taxiDTO.getId()) && entity == null)
            entity = new Taxi();

        //This is TAXI ID (only for UPDATE)
        if (isEmpty(taxiDTO.getTaxiId()))
            entity.setId(taxiDTO.getTaxiId());


            entity.setLatitude(taxiDTO.getLatitude());
            entity.setLongitude(taxiDTO.getLongitude());

        return entity;
    }

    public static TaxiDetail updateTaxiDetail(TaxiDTO dto, TaxiDetail taxiDetail) {
        return new TaxiDetail();
    }

    private static boolean isEmpty(String id) {
        if (id == null)
            return false;

        return true;
    }

    public static PhoneBooking dtoToEntity(PhoneBookingDTO phoneBookingDTO) {

        PhoneBooking pb = new PhoneBooking();
        pb.setDriverId(phoneBookingDTO.getDriverId());
        pb.setDestination(phoneBookingDTO.getDestination());
        pb.setSource(phoneBookingDTO.getSource());
        pb.setComments(phoneBookingDTO.getComment());
        pb.setUserPhoneNumber(phoneBookingDTO.getUserPhoneNumber());
        pb.setStartDate(phoneBookingDTO.getStartDate());
        pb.setEndDate(phoneBookingDTO.getEndDate());
        pb.setTotalPrice(phoneBookingDTO.getTotalPrice());

        pb.setSourceLatitude(phoneBookingDTO.getSourceLatitude());
        pb.setSourceLongitude(phoneBookingDTO.getSourceLongitude());

        pb.setDestLatitude(phoneBookingDTO.getDestLatitude());
        pb.setDestLongitude(phoneBookingDTO.getDestLongitude());
        pb.setCategory(phoneBookingDTO.getCategory());
        pb.setIsMobile(phoneBookingDTO.getIsMobile());
        // pb.setKm(phoneBookingDTO.get);
        return pb;
    }

    public static PhoneBookingDTO entityToDto(PhoneBooking phoneBooking){
        PhoneBookingDTO dto = new PhoneBookingDTO();

        dto.setSource(phoneBooking.getSource());
        dto.setDestination(phoneBooking.getDestination());
        dto.setComment(phoneBooking.getComments());
        dto.setUserPhoneNumber(phoneBooking.getUserPhoneNumber());
        dto.setComment(phoneBooking.getComments());
        dto.setStartDate(phoneBooking.getStartDate());
        dto.setEndDate(phoneBooking.getEndDate());
        dto.setTotalPrice(phoneBooking.getTotalPrice());
        dto.setUpdatedOn(phoneBooking.getUpdatedOn());


        dto.setSourceLatitude(phoneBooking.getSourceLatitude());
        dto.setSourceLongitude(phoneBooking.getSourceLongitude());
        dto.setDestLatitude(phoneBooking.getDestLatitude());
        dto.setDestLongitude(phoneBooking.getDestLongitude());
        dto.setCategory(phoneBooking.getCategory());

        dto.setKm(phoneBooking.getKm());
        dto.setTotalPrice(phoneBooking.getTotalPrice());
        dto.setUserName(phoneBooking.getUserName());
        dto.setIsMobile(phoneBooking.getIsMobile());





        return dto;
    }

    public static PhoneBooking dtoToEntity(PhoneBookingSMS phoneBookingSMS,String supplierID){

        PhoneBooking pb = new PhoneBooking();
        pb.setDriverId(phoneBookingSMS.getDriverId());
        pb.setDestination(phoneBookingSMS.getDestination());
        pb.setSource(phoneBookingSMS.getSource());
        pb.setUserPhoneNumber(phoneBookingSMS.getUserPhoneNumber());
        pb.setKm(phoneBookingSMS.getKm());
        pb.setTotalPrice(phoneBookingSMS.getTotalPrice());
        pb.setSupplierId(supplierID);
        //pb.setSupplierId(phoneBookingSMS.getS);

        pb.setSourceLatitude(phoneBookingSMS.getSourceLatitude());
        pb.setSourceLongitude(phoneBookingSMS.getSourceLongitude());
        pb.setDestLatitude(phoneBookingSMS.getDestLatitude());
        pb.setDestLongitude(phoneBookingSMS.getDestLongitude());
        pb.setCategory(phoneBookingSMS.getCategory());
        pb.setKm(phoneBookingSMS.getKm());
        pb.setUserName(phoneBookingSMS.getUserName());
        pb.setIsMobile(phoneBookingSMS.getIsMobile());


        return pb;
    }

    public static Ride createRide(ShortRide shortRide){
        Ride ride = new Ride();
        ride.setStatus(RIDESTATUS.RESERVED);
        ride.setUserId(shortRide.getUserId());
        ride.setDriverId(shortRide.getDriverId());
        ride.setSource(shortRide.getSource());
        ride.setDestination(shortRide.getDestination());
        return ride;
    }

    public static Ride createRideComment(ShortRides shortRide){
        Ride ride = new Ride();
        ride.setStatus(RIDESTATUS.RESERVED);
        ride.setUserId(shortRide.getUserId());
        ride.setDriverId(shortRide.getDriverId());
        ride.setSource(shortRide.getSource());
        ride.setDestination(shortRide.getDestination());
        ride.setDesc(shortRide.getDes());
        return ride;
    }

    public static User dtoToEntity(UserDTO userDTO, User user) {

        if (user == null) {
            user = new User();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            //user.setAddress(userDTO.getAddress());
            //user.setIsSocialUser(userDTO.getIsSocialUser());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            user.setLang(userDTO.getLang());
            user.setLongitude(userDTO.getLongitude());
            user.setLatitude(userDTO.getLatitude());

            //user.setOneSignalValue(userDTO.getDeviceId());
            //user.setPush(userDTO.getPush());
            user.setToken(userDTO.getToken());

            user.setStatus(userDTO.getStatus());
        } else {
            //user.setId(userDTO.getId());
            if(!userDTO.getFirstName().isEmpty())
                user.setFirstName(userDTO.getFirstName());
            if(isValid(userDTO.getLastName()))
                user.setLastName(userDTO.getLastName());
            if(isValid(userDTO.getEmail()))
                user.setEmail(userDTO.getEmail());
           // if(isValid(userDTO.getAddress()))
            //    user.setAddress(userDTO.getAddress());
            if(isValid(userDTO.getLang()))
                user.setLang(userDTO.getLang());
           // if (userDTO.getImageInfo() != null)
            //    user.setImageInfo(userDTO.getImageInfo());
            //if(! "".equalsIgnoreCase(userDTO.getWebsite()) && userDTO.getWebsite() != null && userDTO.getWebsite().length() > 3 )
                //user.setWebSite(userDTO.getWebsite());
            //// user.setPhoneNumber(userDTO.getPhoneNumber());
            //user.setLongitude(userDTO.getLongitude());
            //user.setLatitude(userDTO.getLatitude());
            //if (userDTO.getDeviceId() != null)
            //    user.setOneSignalValue(userDTO.getDeviceId());
            //if(isValid(userDTO.getPush()))
            //    user.setPush(userDTO.getPush());
            //if(isValid(userDTO.getNotification()))
            //    user.setNotification(userDTO.getNotification());
            //user.setImageInfo(userDTO.getImageInfo());
            //user.setSocialwebsite(userDTO.getS);
            //user.setIsSocialUser(userDTO.getIsSocialUser());
            //if(isValid(userDTO.getStatus()))
            //    user.setStatus(userDTO.getStatus());
            //user.setPassword(userDTO.getPassWord());
            //user.setRole(userDTO);
            //Time being
        }
        return user;
    }

    public static boolean isValid(String stirng){
        if(stirng != null && !stirng.isEmpty()){
            System.out.println("String is not null and not empty");
            return true;
        }
        return false;
    }

    public static UserDTO entityToDto(User user) {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLang(user.getLang());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        //userDTO.setNotification(user.getNotification());
        //userDTO.setDeviceId(user.getOneSignalValue());
        userDTO.setPhoneVerified(user.getPhoneVerified());

        //if (user.getImageInfo() != null) {
          //  userDTO.setImageInfo(user.getImageInfo());
        //}

        //if(userDTO.getRole().equals(Path.ROLE.DRIVER))

        userDTO.setLatitude((float) user.getLatitude());
        userDTO.setLongitude((float) user.getLongitude());

        //userDTO.setWebsite(user.getWebSite());
        userDTO.setDomain(user.getDomain());
        userDTO.setRole(user.getRole());

        //userDTO.setPush(user.getPush());
        //userDTO.setIsSocialUser(user.getIsSocialUser());
        //if (user.getAddress() != null)
          //  userDTO.setAddress(user.getAddress());
        return userDTO;
    }

    public static UserInfoDTO entityToUserDto(User user) {

        UserInfoDTO userDTO = new UserInfoDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLang(user.getLang());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setPhoneVerified(user.getPhoneVerified());
        userDTO.setLatitude((float) user.getLatitude());
        userDTO.setLongitude((float) user.getLongitude());
        userDTO.setDomain(user.getDomain());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public static UsersDTO entityToDtoUser(User user) {

        UsersDTO userDTO = new UsersDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLang(user.getLang());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        //userDTO.setNotification(user.getNotification());
        //userDTO.setDeviceId(user.getOneSignalValue());
        userDTO.setPhoneVerified(user.getPhoneVerified());

        //if (user.getImageInfo() != null) {
        //  userDTO.setImageInfo(user.getImageInfo());
        //}

        //if(userDTO.getRole().equals(Path.ROLE.DRIVER))

        userDTO.setLatitude((float) user.getLatitude());
        userDTO.setLongitude((float) user.getLongitude());

        //userDTO.setWebsite(user.getWebSite());
        userDTO.setDomain(user.getDomain());
        userDTO.setRole(user.getRole());
        if(user.getCreatedDate() != null)
            userDTO.setCreationDate(user.getCreatedDate().toString());

        //userDTO.setPush(user.getPush());
        //userDTO.setIsSocialUser(user.getIsSocialUser());
        //if (user.getAddress() != null)
        //  userDTO.setAddress(user.getAddress());
        return userDTO;
    }

    public static TaxiView entityToViewDetail(Taxi taxi, TaxiDetail detail) {
        if (taxi != null && detail != null) {
            TaxiView tx = new TaxiView(taxi, detail);
            return tx;
        }
        return new TaxiView();
    }

    public static TaxiDTO entityToDetailsDTO(TaxiDetail detail, TaxiDTO dto) {


          //if (Validation.isEmpty(detail.getId()))
        dto.setId(detail.getId());

        // if(detail.getTaxiId() != null)
        //    dto.setTaxiId(detail.getTaxiId());
        if (isEmpty(detail.getTaxiId()))
            dto.setTaxiId(detail.getTaxiId());

        if (isEmptyString(detail.getName()))
            dto.setName(detail.getName());
        // if(detail.getPrice() >= 0)
        //    dto.setPrice(detail.getPrice());

        System.out.println("Part 1 =");
        if (isEmptyString(detail.getDescription()))
            dto.setDescription(detail.getDescription());

        if (isEmptyString(detail.getAdditionalInformation()))
            dto.setAdditionalInformation(detail.getAdditionalInformation());

        System.out.println("Part 2 =");

        if (detail.getPerDay() != null)
            dto.setPerDay(detail.getPerDay());
        if (detail.getWeekEndOffer() != null)
            dto.setWeekEndOffer(detail.getWeekEndOffer());

        if (isEmptyString(detail.getDriverName()))
            dto.setDrivername(detail.getDriverName());

        if (isEmptyString(detail.getDriverPhoneNumber()))
            dto.setDriverPhonenumber(detail.getDriverPhoneNumber());

        System.out.println("Part 2 2 =");

        if (isEmptyString(detail.getSource()))
            dto.setSource(detail.getSource());

        if (isEmptyString(detail.getDestination()))
            dto.setDestination(detail.getDestination());

        System.out.println("Part 2 3 =");

        if (detail.getWaitingTime() >= 0)
            dto.setWaitingTime(detail.getWaitingTime());

        System.out.println("Part 2 4 =");

        //if(detail.getPrice() >= 0)
        // dto.setPrice(detail.getPrice());

        if (detail.getBasePrice() >= 0)
            dto.setBasePrice(detail.getBasePrice());
        if (detail.getPrice() >= 0)
            dto.setPrice(detail.getPrice());
        if (detail.getPeakPrice() >= 0)
            dto.setPeakPrice(detail.getPeakPrice());

        //if (detail.getTransporttype() != null)
         //   dto.setTransporttype(detail.getTransporttype());

        //dto.setCurrency(detail.getCurrency());
        //dto.setTaxiNumber(detail.getT);

        //if (detail.getPickUpLocation() != null)
         //   dto.setPickUpLocation(detail.getPickUpLocation());

        if (detail.getSeats() > 0)
            dto.setSeats(detail.getSeats());
        if (detail.getNumberPlate() != null)
            dto.setTaxiNumber(detail.getNumberPlate());



        //DriverId
        //Supplier can see Driver id
        if (detail.getDriverId() != null)
            dto.setUserId(detail.getDriverId());

        dto.setSupplierId(null);
        //dto.setUserId(1L);

       /* if(detail.getCity() != null ){
            City city = detail.getCity().get();
            dto.setCityDTO(getCityDTO(city));
        }*/
       /* if (detail.getCity() != null && detail.getCity().get() != null) {
            City city = detail.getCity().get();
            if (city != null) {
                CityDTO cityDTO = new CityDTO(city.getId(), city.getCode(), city.getName());
                dto.setCityDTO(cityDTO);
            }
        }

        if (detail.getImageInfos() != null)
            dto.setImageInfos(detail.getImageInfos());*/


        try {
            // if (detail.getVehicleYear() > 0)
            dto.setVehicleYear(detail.getYear());
        } catch (Exception e) {
          System.out.println(" year " + detail.getYear());
        }

        if (isEmptyString(detail.getVehicleBrand()))
            dto.setVehicleBrand(detail.getVehicleBrand());

        if (isEmptyString(detail.getCurrency()))
            dto.setCurrency(detail.getCurrency());

        return dto;
    }

    private static boolean isEmptyString(String text) {
        if (text == null)
            return false;
        if ("".equals(text.trim()))
            return false;
        if (!"".equals(text))
            return true;
        return true;
    }
    public static TaxiDTO entityToDto(Taxi taxi) {

        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setTaxiId(taxi.getId());

       /* if(taxi.getCartype().trim().equalsIgnoreCase(CARTYPE.values()[0].toString()))
            taxiDTO.setCartype(CARTYPE.values()[0]);
        if(taxi.getCartype().trim().equalsIgnoreCase(CARTYPE.values()[1].toString()))
            taxiDTO.setCartype(CARTYPE.values()[1]);*/

        if (!"".equals(taxi.getCarType())) {
            taxiDTO.setCarType(taxi.getCarType());
        }

        taxiDTO.setStatus(taxi.getStatus().name());
        //taxiDTO.setActive(taxi.getActive());
        //taxiDTO.setUpdatedOn(df.format(taxi.getUpdatedOn()));
        //tx.setStatus(taxi.getStatus());

       /* if (taxi.getGeoPt() != null) {
            if (taxi.getGeoPt().getLatitude() > 0)
                taxiDTO.setLatitude(taxi.getGeoPt().getLatitude());
            if (taxi.getGeoPt().getLongitude() > 0)
                taxiDTO.setLongitude(taxi.getGeoPt().getLongitude());
        }*/
        return taxiDTO;
    }

    public static LongDTO longToDto(User user) {
        LongDTO longDTO = new LongDTO();
        longDTO.setLatitude((float) user.getLatitude());
        longDTO.setLongitude((float) user.getLongitude());
        longDTO.setValues(user.getStatus());
        return longDTO;
    }

    public static ContactMail dtoToEntity(ContactDTO contact) {
        ContactMail contactMail = new ContactMail();
        contactMail.setMessage(contact.getMessage());
        contactMail.setEmail(contact.getEmail());
        contactMail.setName(contact.getName());
        contactMail.setStartDate(contact.getStartDate());
        contactMail.setEndDate(contact.getEndDate());
        contactMail.setTaxiId(contact.getTaxiId());
        contactMail.setSource(contact.getSource());
        contactMail.setDestination(contact.getDestination());

        return contactMail;
    }

    public static VehicleType toEntity(VehicleTypeDTO dto, VehicleType type) {
        if (type == null) {
            type = new VehicleType();
        }
        type.setCode(dto.getCode());
        type.setDescription(dto.getDescription());
        type.setName(dto.getName());
        if (!Validation.isEmptyLang(dto.getLang()))
            type.setLang(Path.LANG.lang);
        else
            type.setLang(dto.getLang());
        return type;
    }

    public static VehicleTypeDTO toDTO(VehicleType vehicleType, VehicleTypeDTO vehicleTypeDTO) {

        if (vehicleTypeDTO == null) {
            vehicleTypeDTO = new VehicleTypeDTO(vehicleType.getId(), vehicleType.getCode(), vehicleType.getName());
        }

        vehicleTypeDTO.setName(vehicleType.getName());
        vehicleTypeDTO.setDescription(vehicleType.getDescription());
        vehicleTypeDTO.setLang(vehicleType.getLang());
        return vehicleTypeDTO;
    }

    public static TripPool toDTO(PushNotificationRequests notification){

        TripPool  tripPool = new TripPool();

        tripPool.setPrice(notification.getPrice());

        tripPool.setUserId(notification.getUserId());
        tripPool.setDriverId(notification.getDriverId());

        tripPool.setSource(notification.getSource());
        tripPool.setDes(notification.getDes());
        tripPool.setKm(notification.getKm());

        tripPool.setTripId(notification.getTripId());
        tripPool.setToken(notification.getToken());
        tripPool.setCreatedDate(new Date());

        return tripPool;
    }

    //PushNotificationRequests
    public static PushNotificationRequests toDTO(SearchDTO searchDTO){

        PushNotificationRequests pushNotificationRequests = new PushNotificationRequests();
        if("".equalsIgnoreCase(searchDTO.getId()) && searchDTO.getId() != null && searchDTO.getId().length() > 7){

            pushNotificationRequests.setSource(searchDTO.getSource());
            pushNotificationRequests.setDes(searchDTO.getDestination());

            pushNotificationRequests.setUserId(searchDTO.getId());

            pushNotificationRequests.setKm(String.valueOf(searchDTO.getKm()));
            pushNotificationRequests.setPrice(String.valueOf(searchDTO.getPrice()));



        }
        return pushNotificationRequests;
    }
}
