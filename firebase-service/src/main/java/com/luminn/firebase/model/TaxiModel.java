package com.luminn.firebase.model;

import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.*;
import com.luminn.firebase.dto.MobileDTO;
import com.luminn.firebase.entity.Supplier;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.entity.VehicleType;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.SupplierRepository;
import com.luminn.firebase.repository.TaxiDetailRepository;
import com.luminn.firebase.repository.TaxiRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.request.AdvanceSearchDTO;
import com.luminn.firebase.request.SearchDTO;
import com.luminn.firebase.service.*;
import com.luminn.firebase.util.*;
import com.luminn.firebase.view.TaxiDetailView;
import com.luminn.firebase.view.TaxiView;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
public class TaxiModel {
    @Autowired
    TaxiService taxiService;

    @Autowired
    TaxiDetailService taxiDetailService;

    @Autowired
    UserMongoService userMongoService;

    @Autowired
    TaxiDetailRepository taxiDetailRepository;

    @Autowired
    TaxiRepository  taxiRepository;

    @Autowired
    UserModel userModel;

    @Autowired
    SupplierModel supplierModel;

    @Autowired
    DriverBillingModel driverBill;

    @Autowired
    DriverPhotoService photoService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationModel notificationModel;

    @Autowired
    SupplierService supplierService;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    DriverBillingModel driverBillingModel;

    private static final Logger log = LoggerFactory.getLogger(TaxiModel.class);

   /* public void addTaxiDetails(MobileDTO user){

        String id =  userMongoService.addUserOneRegistration(user);

        Taxi taxi = new Taxi();
        taxi.setId(id);
        taxi.setStatus(DRIVERSTATUS.valueOf("STOP"));
        taxi.setName(user.getFirstName());
        taxi.setToken(user.getToken());
        taxi.setLatitude(user.getLatitude());
        taxi.setLatitude(user.getLatitude());

        taxiService.addTaxiDetails(taxi);
        //taxiDetailService.addTaxiDetails(taxi);
    }*/

    public void addOnyTaxiDetails(MobileDTO user){

        String id =  userMongoService.addUserOneRegistration(user,false,null);

        Taxi taxi = new Taxi();
        taxi.setId(id);
        taxi.setStatus(DRIVERSTATUS.valueOf("STOP"));
        taxi.setName(user.getFirstName());
        taxi.setToken(user.getToken());
        taxi.setLatitude(user.getLatitude());
        taxi.setLatitude(user.getLatitude());
        taxi.setActive(false);

        taxiService.addTaxiDetails(taxi);
        //taxiDetailService.addTaxiDetails(taxi);


    }

    public String addTaxi(Taxi user){
        String taxiID = taxiService.addTaxiOne(user);
        return taxiID;
    }

    public ModelStatus createTaxiWithDriver(UserDetailsDTO userDTO, String supplierId, String driverName, String userKey)  {

        String vehicleBrand;
        String taxiNumber ;
        int seat;
        int vehicleYear;

        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setLongitude(userDTO.getLongitude());
        taxiDTO.setLatitude(userDTO.getLatitude());
        taxiDTO.setSupplierId(supplierId);

        taxiDTO.setDriverPhonenumber(userDTO.getPhoneNumber());
        if (driverName == null || "".equalsIgnoreCase(driverName))
            driverName = userDTO.getFirstName();

        taxiDTO.setDrivername(driverName);
        String carType = userDTO.getCarType();
        String role = userDTO.getRole();
        //String cityCode = userDTO.getCode();

        //taxiDTO.setVehicleBrand(userDTO.get);
        if (carType != null && CARTYPE.forValue(carType).name() != null) {
            //check category
            if (role.equalsIgnoreCase(Role.ROLE_DRIVER)) {
                taxiDTO.setCarType(CARTYPE.forValue(carType).name());
            }
        }
        CityDTO cityDTO = new CityDTO();
        taxiDTO.setCityDTO(cityDTO);

        vehicleBrand = userDTO.getVehicleBrand() != null ? userDTO.getVehicleBrand() : "Default";
        taxiNumber = userDTO.getTaxiNumber() != null ? userDTO.getTaxiNumber() : "Default";
        seat = userDTO.getSeats() > 0 ? userDTO.getSeats() : 1;
        vehicleYear = userDTO.getVehicleYear() > 0 ? userDTO.getVehicleYear() : 1980;

        if(userDTO.getVehicleBrand() != null)
            vehicleBrand = userDTO.getVehicleBrand();
        log.info(" TaxiId -->" + userKey );


        return createTaxiWithDetail(userKey,supplierId, userDTO, taxiDTO, true,seat,vehicleBrand,taxiNumber,vehicleYear);
    }

    public ModelStatus createTaxiWithDetail(String userKey,String supplierId, MobileDTO userDTO, TaxiDTO taxiDTO, boolean role,
                                            int seats,String vehicleBrand,String taxiNumber,int vehicleYear)  {

        VehicleType vehicleType = null;

        log.info(" createTaxiWithDetail supplierId" +  supplierId );
        String taxiId = null;
        if ((supplierId !=null )) {


            if (supplierId != null ) {
            //if (supplierId > 0 && validationSupplier(taxiDTO, role)) {



                //Taxi taxi = taxiHelperService.toEntityTaxi(taxiDTO, null);
                Taxi taxi = Converter.dtoToEntity(taxiDTO,null);

                taxi.setCarType(taxiDTO.getCarType());
                taxi.setCategory(getCarType(userDTO));
                taxi.setName(userDTO.getFirstName());
                taxi.setStatus(DRIVERSTATUS.valueOf("STOP"));
                taxi.setCode("8111");
                taxi.setActive(false);
                taxi.setLastUpdate(new Date());
                ////////////////////////////
                taxiId = taxiService.addTaxiOne(taxi);
                ///////////////////////////////////////////////

                System.out.println(" Taxi Id --" + taxiId);
                taxiDTO.setTaxiId(taxiId);
                TaxiDetail taxiDetail = Converter.updateTaxiDetail(taxiDTO, null);

                if (taxiDetail != null) {
                    taxiDetail.setLastUpdate(new Date());

                    taxiDetail.setTaxiId(taxiId);
                    String tdIds = taxiService.addTaxiDetails(taxiDetail);
                    log.info("taxiService TaxidetailsId -->" + tdIds);

                    //check values are coming
                    //DO UPDATE
                    taxiDetailService.updateTaxiId(tdIds,taxiId,supplierId,userKey,
                    seats,vehicleBrand,taxiNumber, vehicleYear);

                    //updateTopup
                    //driverBillingModel
                    DriverBillingDTO driverBillingDTO = new DriverBillingDTO();
                    driverBillingDTO.setDriverId(userKey);
                    UserStatus  status = driverBillingModel.createInsdieTaxiDetails(driverBillingDTO,supplierId);
                    log.info("Billing stats -->" + status.name());
                    return ModelStatus.CREATED;
                } else
                    return ModelStatus.NO_SUPPLIER;
            }
            return ModelStatus.NO_SUPPLIER;
        }
        return ModelStatus.NO_SUPPLIER;
    }


    public boolean validateTaxiDetailsId(TaxiDetail isTaxiDetailExisting) {
        if ((isTaxiDetailExisting != null && isTaxiDetailExisting.getId() != null)) {
            return true;
        } else
            return false;
    }


    public ModelStatus updateTaxiType(TaxiUpdateDTO dto)  {
        Taxi taxi = null;
        //supplier
        TaxiDetail ltaxiDetail = taxiDetailService.findBySupplierId(dto.getSupplierId());
        //Driver id

       // log.info(" --Before --" + ltaxiDetail.getId());

        if (validateTaxiDetailsId(ltaxiDetail)) {

            User user = null;
            TaxiDTO dto2 = new TaxiDTO();
            dto2.setSupplierId(dto.getSupplierId());
            if (ltaxiDetail != null && ltaxiDetail.getId() != null) {

                log.info(" --After  --" + ltaxiDetail.getDriverId());
                if(ltaxiDetail.getDriverId() != null){
                     Optional<User> opUser =   userRepository.findById(ltaxiDetail.getDriverId());
                     if(opUser.isPresent()) {
                         user = opUser.get();
                         user.setFirstName(dto.getFirstName());
                         user.setLastName(dto.getLastName());
                         userRepository.save(user);
                     }
                }

                if ((ltaxiDetail != null && ltaxiDetail.getId() != null)) {

                    log.info(" --now updating --" + ltaxiDetail.getId());
                    log.info(" .. getVehicleBrand .." + dto.getVehicleBrand());

                    if (dto.getVehicleBrand() != null) {
                        try {
                            ltaxiDetail.setVehicleBrand(dto.getVehicleBrand());
                            //CK BIG MISTAKE
                            //taxiDetailRepository.save(ltaxiDetail);
                             // taxiDetailService.updateTaxiId()
                            log.info(" --now     Taxi Details completed --" + ltaxiDetail.getId());
                        } catch (Exception e) {
                        }
                    }

                   /* if(taxiId != null && CARTYPE.check(dto.getCarType()) && CARTYPE.valueOf(dto.getCarType()) != null) {
                        taxi = taxiService.getById(Taxi.class, taxiId);
                        taxi.setCarType(dto.getCarType());
                        taxiService.update(Taxi.class,taxi.getId(),taxi);
                        log.info(" --now Taxi updated --" + ltaxiDetail.getId());
                    }
                    else {
                        return ModelStatus.CAR_TYPE_NOT_EXISTING;
                    }*/
                    return ModelStatus.UPDATED;
                }
                return ModelStatus.NO_SUPPLIER_EXISTING;
            }
            return ModelStatus.NO_TAXIDETAILS_ID;
        }
        return ModelStatus.NO_SUPPLIER_EXISTING;
    }


    /*public List<TaxiView> listTaxiForApprovalss(String supplierId)  {


        List<TaxiView> taxiListNew = new ArrayList<TaxiView>(0);

        List<TaxiDetail > taxiList = taxiDetailRepository.findBySupplierId(supplierId);
        TaxiView taxiview = null;
        Taxi taxis = null;

        for(TaxiDetail taxisDetail :taxiList){

            taxis = getTaxiIds(taxisDetail.getTaxiId());
            if(taxis != null && !taxis.isActive()) {
                taxiview = new TaxiView(taxis, taxisDetail);
                taxiListNew.add(taxiview);
            }
        }

        return taxiListNew;
    }*/


    public TaxiView setActivate(String id, boolean flag)  {

        Taxi isExistingTaxi = null;

        //Optional<Taxi> opt = taxiRepository.findById(id);
        //Optional<Taxi> opt =  taxiRepository.findById(id);
        isExistingTaxi =  taxiService.findId(id,null);


        if (isExistingTaxi != null) {

            //isExistingTaxi = opt.get();
            //only update taxi
            //isExistingTaxi = opt.get();

            isExistingTaxi.setActive(flag);
            taxiService.updateApprove(isExistingTaxi.getId(),flag);

            //approve taxiDetails as well
            TaxiDetail isExistingDetail = taxiDetailService.findTaxiId(isExistingTaxi.getId());
            //have disable at the moment

            if(isExistingDetail != null ) {
                TaxiView tc =  Converter.entityToViewDetail(isExistingTaxi, isExistingDetail);
                return tc;
            }
            return new TaxiView();

        } else
            return new TaxiView();
    }

    /*public boolean validationSupplier(TaxiDTO taxiDTO, boolean ROLE) throws DatabaseException {
        //TURE when Driver registration
        if (ROLE) {
            return true;
        } else if (taxiDTO != null && taxiDTO.getUserId() != null) {
            //create a Taxi user
            //Supplier is not UserTable
            User user = userService.getById(User.class, taxiDTO.getUserId());
            if (user != null) {
                if (user.getRole().equals(Path.ROLE.SUPPLIER)) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }*/

    //CK
    public DRIVERSTATUS addGeoLocationWithStatus(User user, String id, float latitude, float longitude, String driverStatus) {


        Taxi isExistingTaxi = taxiService.findId(id,null);
        //not working
        //Optional<Taxi> isExistingTaxi = taxiRepository.findById(id);

        if(isExistingTaxi.getId() != null) {
            //GeoPt geoPt = new GeoPt(latitude, longitude);

            /*  LatLng geoPt = LatLng.of(latitude,longitude);
            Point point = new Point();
            point.setLat(geoPt.getLatitude());
            point.setLon(geoPt.getLongitude());


            isExistingTaxi.setGeoPt(geoPt);
            isExistingTaxi.setGeocells(GeocellManager.generateGeoCell(point));*/


            if(driverStatus != null && !"".equals(driverStatus)) {

                if(DRIVERSTATUS.valueOf(driverStatus).name().equals(DRIVERSTATUS.STOP.name()) ||
                        DRIVERSTATUS.valueOf(driverStatus).name().equals(DRIVERSTATUS.RIDE_FINISH.name())){

                    //before status should be start
                    //if(userModel.ge)
                    if(user != null && user.getStatus() != null && user.getStatus().equalsIgnoreCase(STEPS.START))
                        userModel.loginStatus("LOGOUT",user);
                    if(DRIVERSTATUS.valueOf(driverStatus).name().equals(DRIVERSTATUS.RIDE_FINISH.name())){
                        if(user != null && user.getStatus() != null && user.getStatus().equalsIgnoreCase(STEPS.RIDE_FINISH))
                            userModel.loginStatus("LOGOUT",user);
                    }
                }
                if(DRIVERSTATUS.valueOf(driverStatus).name().equals(DRIVERSTATUS.START.name())){
                }

                if(DRIVERSTATUS.valueOf(driverStatus).name().equals(DRIVERSTATUS.WAITING.name())){
                    //isExistingTaxi.setStatusTime(new Date());
                }
                if(DRIVERSTATUS.valueOf(driverStatus).name().equals(DRIVERSTATUS.LOGOUT.name())){
                    //isExistingTaxi.setStatusTime(new Date());
                }
                taxiService.updateLocations(id,latitude,longitude,driverStatus);
                //taxiService.update(Taxi.class, isExistingTaxi.getId(), isExistingTaxi);
                //return DRIVERSTATUS.valueOf(driverStatus);
                return DRIVERSTATUS.valueOf(DRIVERSTATUS.UPDATED.name());
            }
            else {
                return DRIVERSTATUS.valueOf(DRIVERSTATUS.UPDATED.name());
            }
        }
        return DRIVERSTATUS.NOTEXISTS;
    }

    public List<TaxiDTO> validationApproves(String supplierId) {

       // List<TaxiDetail> ltaxiDetail  = taxiDetailRepository.findBySupplierId(supplierId,);

        Page<TaxiDetail> ltaxiDetail = taxiDetailRepository.findBySupplierIds(supplierId,null);
        List<TaxiDTO> lTaxiDTO = new ArrayList<TaxiDTO>(0);
        for(TaxiDetail td : ltaxiDetail){
            TaxiDTO tx1 = new TaxiDTO();
            TaxiDTO count = Converter.entityToDetailsDTO(td,tx1);
            lTaxiDTO.add(count);
        }


        return lTaxiDTO;
    }

    public List<TaxiDTO> 	listMyTaxiesDriver(String id,String driverId)  {

        //List<TaxiDetail> ltaxiDetail = taxiDetailRepository.findBySupplierId(id,null);

        Page<TaxiDetail> ltaxiDetail = taxiDetailRepository.findBySupplierIds(id,null);
        List<TaxiDTO> taxiListNew = new ArrayList<TaxiDTO>(0);

        for (TaxiDetail taxisDetail : ltaxiDetail) {
            Taxi taxi = getTaxiIds(taxisDetail.getTaxiId());

            System.out.println("taxi ID *** = " + taxi.getId());
            if ((taxi.isActive())) {
                TaxiDTO taxiDTO = toDTODetail(taxi, taxisDetail);
                System.out.println("taxiDTO User *** = " + taxiDTO.getUserId());
                log.info("driverId Before=" + driverId);
                if (taxiDTO.getUserId() == driverId) {
                    taxiListNew.add(taxiDTO);
                    log.info("driverId =" + driverId);
                }
            }
        }
        return taxiListNew;
    }

    public TaxiDTO toDTODetail(Taxi entity,TaxiDetail entityDetail){
        TaxiDTO taxiDTO = toDTOTaxi(entity);

        TaxiDTO taxiDTODetail =	Converter.entityToDetailsDTO(entityDetail,taxiDTO);

        System.out.println("taxiDTODetail ==" + taxiDTO);

        return taxiDTODetail;
    }

    public TaxiDTO getTaxiDetail(String id) {


        Taxi taxi = taxiService.findId(id,null);
        //Optional<Taxi> taxis = taxiRepository.findById(id);
        TaxiDTO taxiDTO = new TaxiDTO();

        if (taxi != null) {


            log.info("taxi.getId() =" + taxi.getId());
            System.out.println("taxiId = " + taxi.getId());
            TaxiDetail taxidetail = taxiDetailService.findTaxiId(taxi.getId());

            log.info("taxidetail if object exist " + taxidetail.getId());
            //System.out.println("taxidetail Id = " + taxidetail.getId());
            taxiDTO.setLatitude(taxi.getLatitude());
            taxiDTO.setLongitude(taxi.getLongitude());
            User user = null;

            if (taxidetail != null) {

                taxiDTO = toDTODetail(taxi, taxidetail);
                taxiDTO.setTaxiId(id);
                taxiDTO.setSupplierId(taxidetail.getSupplierId());
                String driverId = taxidetail.getDriverId();

                //Optional<User> ouser = userRepository.findById(driverId);

                 user =  userMongoService.findId(driverId);
                 if(user != null) {
                        if (!"".equalsIgnoreCase(user.getFirstName()) && user.getFirstName() != null )
                            taxiDTO.setName(user.getFirstName());
                        if (!"".equalsIgnoreCase(user.getLastName()) && user.getLastName() != null)
                          taxiDTO.setDrivername(user.getLastName());
                        //else
                          //  taxiDTO.setName("NO NAME");

                        taxiDTO.setPhoneNumber(user.getPhoneNumber());
                        taxiDTO.setDriverPhonenumber(user.getPhoneNumber());
                }
                return taxiDTO;
            }
            return new TaxiDTO();
        } else {
             taxiDTO = new TaxiDTO();
            taxiDTO.setId("-1l");
            return taxiDTO;
        }

    }


    public String getPhotoServiceurl(String driver,String type){

        if(photoService.getDriverUserId(driver,type) != null)
            if(photoService.getDriverUserId(driver,type).getData() != null)
                return driver;
            else
                return "";
        return "";
    }

    //https://stackoverflow.com/questions/20706783/put-byte-array-to-json-and-vice-versa/20706988
    //https://howtodoinjava.com/java/array/convert-byte-array-string/
    public List<TaxisDTO> getUsersImageByLocation(SearchDTO searchDTO){

        //double distanceInRad = 10.0 / 6371;
        //FindIterable<Document> result = collection.find(Filters.geoWithinCenterSphere("location", -0.1435083, 51.4990956, distanceInRad))

        List<TaxisDTO> result = new ArrayList<>();

        double distanceInRad = 0;
        BasicDBList coords= new BasicDBList();
        coords.add(Double.valueOf(searchDTO.getLatitude()));
        coords.add(Double.valueOf(searchDTO.getLongitude()));
        BasicDBObject loc2 = new BasicDBObject("type", "Point")
                .append("coordinates", (coords));
        Point currentLoc = new Point(new Position(searchDTO.getLongitude(),searchDTO.getLatitude()));

        //Query failed with error code 2 and error message '$within not supported with provided geometry: { $geoWithin: { $geometry: { type: "Point", coordinates:
        FindIterable<Document> docs = mongoTemplate.getCollection("Taxi").find(Filters.near("location",currentLoc,distanceInRad,distanceInRad));

        System.out.println(" Total result1 -->" + docs);
        TaxisDTO taxisDTO = null;


        for(Document doc : docs) {


            System.out.println(" result1 -->" + docs.first().get("_id") );
            taxisDTO = Converter.documentToTaxiSearch(doc,null);



            //ORGINAL
            //byte[] bytes = driverPhotoService.getDriverUserId(taxiDetail.getDriverId(),"").getData();
            //search.setImageBype(bytes);
            //String byteString = Base64.getEncoder().encodeToString(bytes);
            //search.setImage(byteString);
            String url = null;
            TaxiDetail taxiDetail = null;
            if(taxisDTO.getId() != null && !"".equalsIgnoreCase(taxisDTO.getId()))
                taxiDetail = taxiDetailService.findTaxiId(taxisDTO.getId());
            if(taxiDetail != null) {

                url = getPhotoServiceurl(taxiDetail.getDriverId(),"profile");
                if(url != null && url.length() > 5) {
                    taxisDTO.setImage(url);
                }
                if(taxiDetail.getDriverId() != null)
                    taxisDTO.setDriverId(taxiDetail.getDriverId());

                taxisDTO = getUserObject(taxisDTO,taxiDetail);
            }

            //byte[] bytes = Base64.getDecoder().decode(string);
            //String base64String = Base64.encodeBase64String(bytes);

            result.add(taxisDTO);
        }

        //must check DRIVER ARE ONLINE
        sendNotification(searchDTO);


        log.info(" notificationModel " );

        return result;
    }

  @Autowired
  RideModel rideModel;

    public String priceUpdate(TaxiPriceDTO taxisDTO,AdvanceSearchDTO searchDTO,String supplierID){
        //String category,String type,String region,float distance,String domain,String supplierId)
        String domain = "TAXIDEALS";
        if(searchDTO.getDomain() != null && searchDTO.getDomain().length() > 3){
            domain = (searchDTO.getDomain());
        }
       String price =  rideModel.updateCalculation(searchDTO.getCategory(),taxisDTO.getCarType(),searchDTO.getRegion(),
                searchDTO.getDistance(),domain,supplierID);
       return price;
    }

    public  List<TaxiPriceDTO> getUsersByLocationAdvance(AdvanceSearchDTO searchDTO){

        double distanceInRad = 2000.0;
        double distanceInRad2 = 0.0;

        BasicDBList coords= new BasicDBList();
        coords.add(Double.valueOf(searchDTO.getLongitude()));
        coords.add(Double.valueOf(searchDTO.getLatitude()));
        BasicDBObject loc2 = new BasicDBObject("type", "Point").append("coordinates", (coords)).append("active", "true");

        FindIterable<Document> docs = null;
        docs = mongoTemplate.getCollection("Taxi").find(Filters.near("location",loc2,distanceInRad,distanceInRad2)).sort(new BasicDBObject("lastUpdate",-1)).limit(5);

        List<TaxiPriceDTO> result = new ArrayList<>();
        System.out.println(" Total result1 -->" + docs.first());
        TaxiPriceDTO taxiPriceDTO = null;

        List<Supplier> supplierList = supplierRepository.findByName("TAXIDEALS");
        Supplier supplier = null;
        String supplierID = null;
        if(supplierList != null) {
            supplier = supplierList.get(0);
            supplierID = supplier.getId();
        }


        for(Document doc : docs) {


            if (doc.get("active") != null && parseBoolean(doc.get("active").toString()) ) {
                //taxi.set(taxiDoc.get("name").toString());

                taxiPriceDTO = Converter.documentToTaxiPriceSearch(doc,null);

                String url = null;
                TaxiDetail taxiDetail = null;
                if(taxiPriceDTO.getId() != null && !"".equalsIgnoreCase(taxiPriceDTO.getId()))
                    taxiDetail = taxiDetailService.findTaxiId(taxiPriceDTO.getId());
                if(taxiDetail != null) {

                        /*url = getPhotoServiceurl(taxiDetail.getDriverId(),"profile");
                        if(url != null && url.length() > 5) {
                            taxisDTO.setImage(url);
                        }*/
                    if(taxiDetail.getDriverId() != null)
                        taxiPriceDTO.setDriverId(taxiDetail.getDriverId());

                    taxiPriceDTO = getUserObject(taxiPriceDTO,taxiDetail);

                    if(taxiPriceDTO != null) {
                        String price = priceUpdate(taxiPriceDTO, searchDTO, supplierID);
                        taxiPriceDTO.setPrice(price);
                    }
                }
                result.add(taxiPriceDTO);
            }

        }
        //must check DRIVER ARE ONLINE
        return result;
    }
    
    public  List<TaxiCompanyDTO> getUsersByLocationAdvanceV2(AdvanceSearchDTO searchDTO){

        double distanceInRad = 4000.0;
        double distanceInRad2 = 0.0;

        BasicDBList coords= new BasicDBList();
        coords.add(Double.valueOf(searchDTO.getLongitude()));
        coords.add(Double.valueOf(searchDTO.getLatitude()));
        BasicDBObject loc2 = new BasicDBObject("type", "Point").append("coordinates", (coords)).append("active", "true");

        FindIterable<Document> docs = null;
        docs = mongoTemplate.getCollection("Taxi").find(Filters.near("location",loc2,distanceInRad,distanceInRad2)).sort(new BasicDBObject("lastUpdate",-1)).limit(5);

        List<TaxiCompanyDTO> result = new ArrayList<>();
        System.out.println(" Total result1 -->" + docs.first());
        TaxiCompanyDTO taxiCompanyDTO = null;

        List<Supplier> supplierList = supplierRepository.findByName("TAXIDEALS");
        Supplier supplier = null;
        String supplierID = null;
        if(supplierList != null) {
            supplier = supplierList.get(0);
            supplierID = supplier.getId();
        }


        for(Document doc : docs) {


            if (doc.get("active") != null && parseBoolean(doc.get("active").toString()) ) {
                //taxi.set(taxiDoc.get("name").toString());

                taxiCompanyDTO = Converter.documentToTaxiCompanySearch(doc,null);

            String url = null;
            TaxiDetail taxiDetail = null;
            if(taxiCompanyDTO.getId() != null && !"".equalsIgnoreCase(taxiCompanyDTO.getId()))
                taxiDetail = taxiDetailService.findTaxiId(taxiCompanyDTO.getId());
                    if(taxiDetail != null) {

                        /*url = getPhotoServiceurl(taxiDetail.getDriverId(),"profile");
                        if(url != null && url.length() > 5) {
                            taxisDTO.setImage(url);
                        }*/
                        if(taxiDetail.getDriverId() != null)
                            taxiCompanyDTO.setDriverId(taxiDetail.getDriverId());

                        taxiCompanyDTO = getUserObject(taxiCompanyDTO,taxiDetail);

                        if(taxiCompanyDTO != null) {
                            String price = priceUpdate(taxiCompanyDTO, searchDTO, supplierID);
                            taxiCompanyDTO.setPrice(price);
                        }
                    }
                result.add(taxiCompanyDTO);
            }

        }
        //must check DRIVER ARE ONLINE
        return result;
    }
    public String getSupplierId(String domain){

        Supplier supplier = null;
        String supplierID = null;
        List<Supplier> supplierList = null;

        if(!"".equalsIgnoreCase(domain) && domain != null && domain.equalsIgnoreCase(domain)){
             supplierList = supplierRepository.findByName(domain);
        }
        else {
             supplierList = supplierRepository.findByName("TAXIDEALS");
        }

        if(supplierList != null) {
            supplier = supplierList.get(0);
            supplierID = supplier.getId();
        }
        return supplierID;
    }

    public  List<TaxiCompanyDTO> getUsersByLocationAdvanceV3(AdvanceSearchDTO searchDTO){

        double distanceInRad = 4000.0;
        double distanceInRad2 = 0.0;

        List<TaxiCompanyDTO> result = new ArrayList<>();
        TaxiCompanyDTO taxiCompanyDTO = null;
        FindIterable<Document> docs = null;


        Supplier supplier = null;
        String supplierID = null;

        BasicDBList coords= new BasicDBList();
        coords.add(Double.valueOf(searchDTO.getLongitude()));
        coords.add(Double.valueOf(searchDTO.getLatitude()));

        BasicDBObject loc2 = new BasicDBObject("type", "Point").append("coordinates", (coords)).append("active", "true");

        docs = mongoTemplate.getCollection("Taxi").find(Filters.near("location",loc2,distanceInRad,distanceInRad2)).
                sort(new BasicDBObject("lastUpdate",-1)).limit(5);

        //String supplierId = getSupplierId(searchDTO.getDomain());

        for(Document doc : docs) {

            if (doc.get("active") != null && parseBoolean(doc.get("active").toString()) ) {

                taxiCompanyDTO = Converter.documentToTaxiCompanySearch(doc,null);

                String url = null;
                TaxiDetail taxiDetail = null;
                if(taxiCompanyDTO.getId() != null && !"".equalsIgnoreCase(taxiCompanyDTO.getId()))
                    taxiDetail = taxiDetailService.findTaxiId(taxiCompanyDTO.getId());

                if(taxiDetail != null) {

                    if(taxiDetail.getDriverId() != null)
                        taxiCompanyDTO.setDriverId(taxiDetail.getDriverId());
                    taxiCompanyDTO = getUserObject( taxiCompanyDTO,taxiDetail);

                    if(taxiCompanyDTO != null) {
                        String price = priceUpdate(taxiCompanyDTO, searchDTO, supplierID);
                        taxiCompanyDTO.setPrice(price);
                    }
                }
                result.add(taxiCompanyDTO);
            }
        }
        //must check DRIVER ARE ONLINE
        return result;
    }

    public static boolean parseBoolean(String b)
    {
        if(b != null)
        return "true".equalsIgnoreCase(b) ? true : false;
        else
            return false;
    }
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    public List<TaxisDTO> getUsersGeoByLocation(SearchDTO searchDTO){

        //double distanceInRad = 10.0 / 6371;
        //FindIterable<Document> result = collection.find(Filters.geoWithinCenterSphere("location", -0.1435083, 51.4990956, distanceInRad))

      /*  org.springframework.data.geo.Point location = new org.springframework.data.geo.Point(Double.valueOf(searchDTO.getLongitude()),Double.valueOf(searchDTO.getLatitude()));
        NearQuery query = NearQuery.near(location).maxDistance(new Distance(1000, Metrics.MILES));
          taxiRepository.findByLocationNear(location,new Distance(1000, Metrics.MILES));*/

        /*System.out.println( " COUNT --> " + query.getCollation());
        GeoResults<Taxi> t = mongoOperations.geoNear(query, Taxi.class);
        System.out.println( " t  --> " + t);*/


        //////////////////
       // List<TaxisDTO> result = new ArrayList<>();
        List<TaxisDTO> result = new ArrayList<>();

        double distanceInRad = 5000.0;
        double distanceInRad2 = 0.0;

        BasicDBList coords= new BasicDBList();
        coords.add(Double.valueOf(searchDTO.getLongitude()));
        coords.add(Double.valueOf(searchDTO.getLatitude()));
        BasicDBObject loc2 = new BasicDBObject("type", "Point").append("coordinates", (coords));

        //Point currentLoc = new Point(new Position(Double.valueOf(searchDTO.getLongitude()),Double.valueOf(searchDTO.getLatitude())));

        //Query failed with error code 2 and error message '$within not supported with provided geometry: { $geoWithin: { $geometry: { type: "Point", coordinates:
        //FindIterable<Document> docs = collection.find(Filters.near("location",currentLoc,Double.valueOf(distanceInRad),Double.valueOf(distanceInRad2))).iterator();

        FindIterable<Document> docs = mongoTemplate.getCollection("Taxi").find(Filters.near("location",loc2,distanceInRad,distanceInRad2));

        System.out.println(" Total result1 -->" + docs);
        TaxisDTO taxisDTO = null;


        for(Document doc : docs) {


            System.out.println(" result1 -->" + docs.first().get("_id") );
            taxisDTO = Converter.documentToTaxiSearch(doc,null);

            //ORGINAL
            //byte[] bytes = driverPhotoService.getDriverUserId(taxiDetail.getDriverId(),"").getData();
            //search.setImageBype(bytes);
            //String byteString = Base64.getEncoder().encodeToString(bytes);
            //search.setImage(byteString);
            String url = null;
            TaxiDetail taxiDetail = null;
            if(taxisDTO.getId() != null && !"".equalsIgnoreCase(taxisDTO.getId()))
                taxiDetail = taxiDetailService.findTaxiId(taxisDTO.getId());
            if(taxiDetail != null) {

                url = getPhotoServiceurl(taxiDetail.getDriverId(),"profile");
                if(url != null && url.length() > 5) {
                    taxisDTO.setImage(url);
                }
                if(taxiDetail.getDriverId() != null)
                    taxisDTO.setDriverId(taxiDetail.getDriverId());

                taxisDTO = getUserObject(taxisDTO,taxiDetail);
            }

            result.add(taxisDTO);
        }

        //must check DRIVER ARE ONLINE
        sendNotification(searchDTO);

        log.info(" notificationModel " );

        return result;
    }

    public  Predicate<TaxisDTO> findTypeFromList(String type) {
        log.info("type->" + type);
        if(!"".equals(type) && type != null && type.equalsIgnoreCase(type))
            return p -> p.getCarType().equalsIgnoreCase(type);
        else
            return  null;
    }

    public List<TaxisDTO> getUsersGeoByLocationstatus(SearchDTO searchDTO){

        List<TaxisDTO> waitingResult = new ArrayList<>();
        List<TaxisDTO> stopResult = new ArrayList<>();

        double distanceInRad = 2000.0;
        //double distanceInRad = 7000.0;
        double distanceInRad2 = 0.0;

        BasicDBList coords= new BasicDBList();
        coords.add(Double.valueOf(searchDTO.getLongitude()));
        coords.add(Double.valueOf(searchDTO.getLatitude()));
        BasicDBObject loc2 = new BasicDBObject("type", "Point").append("coordinates", (coords));
        TaxisDTO taxisDTO = null;
        FindIterable<Document> docs = null;
         docs = mongoTemplate.getCollection("Taxi").find(Filters.near("location",loc2,distanceInRad,distanceInRad2)).sort(new BasicDBObject("lastUpdate",-1)).limit(5);

        //if(docs != null && docs.collation()){
        //}

        for(Document doc : docs) {

            System.out.println(" result1 -->" + docs.first().get("_id") );
            taxisDTO = Converter.documentToTaxiSearch(doc,null);

            String url = null;
            TaxiDetail taxiDetail = null;
            if(taxisDTO.getId() != null && !"".equalsIgnoreCase(taxisDTO.getId()))
                taxiDetail = taxiDetailService.findTaxiId(taxisDTO.getId());
            if(taxiDetail != null) {

                //TIME BEING
                /*url = getPhotoServiceurl(taxiDetail.getDriverId(),"profile");
                if(url != null && url.length() > 5) {
                    taxisDTO.setImage(url);
                }*/
                if(taxiDetail.getDriverId() != null)
                    taxisDTO.setDriverId(taxiDetail.getDriverId());

                taxisDTO = getUserObject(taxisDTO,taxiDetail);
            }
            if(taxisDTO.getStatus().equalsIgnoreCase("WAITING"))
                waitingResult.add(taxisDTO);
            //Time being disabled
            //else
               // stopResult.add(taxisDTO);
        }

        //must check DRIVER ARE ONLINE
        //TIME BEING
        //sendNotification(searchDTO);

        log.info(" notificationModel " );

        if(waitingResult.size() == 0)
            return stopResult;
        else
            return waitingResult;
    }

    public List<TaxisDTO> getUsersGeoByLocation1(SearchDTO searchDTO){

        //double distanceInRad = 5.0 / 6371;
        //FindIterable<Document> result = collection.find(Filters.geoWithinCenterSphere("location", -0.1435083, 51.4990956, distanceInRad))

        List<TaxisDTO> result = new ArrayList<>();

        double distanceInRad = 1000.0;
        double distanceInMaxRad = 10.0;

        /*Consumer<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };*/



         Consumer<Document> printBlock = document -> System.out.println(document.toJson());

        //FindIterable<Document> docs = collection.find(Filters.geoWithin("location",loc2));
        //        coordinates : [ <longitude>, <latitude> ]

        //Double.valueOf(driver.getLongitude()) + "," +
        //Double.valueOf(driver.getLatitude()) +

        mongoTemplate.getCollection("Taxi").createIndex(Indexes.geo2dsphere("location.coordinates"));

       // Point currentLoc = new Point(new Position(Double.valueOf(searchDTO.getLongitude()),Double.valueOf(searchDTO.getLatitude())));
        //8.649629592895508 47.40761947631836
         Point ref = new Point(new Position(8.649629592895508,47.40761947631836));

        //Point currentLoc = new Point(new Position(47.40761947631836,8.649629592895508));
        //Point currentLoc = new Point(new Position(searchDTO.getLatitude(),searchDTO.getLongitude()));

        //Query failed with error code 2 and error message '$within not supported with provided geometry: { $geoWithin: { $geometry: { type: "Point", coordinates:

        FindIterable<Document> docs =  mongoTemplate.getCollection("Taxi").find(Filters.near("location.coordinates",ref,1000.0, 1.0));
        //    collection.find(Filters.near("location.coordinates",currentLoc,1000.0, 1.0)).forEach(printBlock);

        System.out.println(" Total result1 -->" + docs.first().toJson());

        for(Document doc : docs){
            System.out.println(" Total result1 -->" + doc.toJson());
        }

        //Point currentLoc = new Point(new Position(searchDTO.getLatitude(), searchDTO.getLongitude()));
        //FindIterable<Document> docs = collection.find(Filters.near("location", currentLoc, 1000.0, 10.0));

     /*   System.out.println(" Total result1 -->" + docs.toString());

        for(Document doc : docs){
            System.out.println(" Total result1 -->" + doc.get(0).toString());
        }
        TaxisDTO taxisDTO = null;


        for(Document doc : docs) {


            System.out.println(" result1 -->" + docs.first().get("_id") );
            taxisDTO = Converter.documentToTaxiSearch(doc,null);



            //ORGINAL
            //byte[] bytes = driverPhotoService.getDriverUserId(taxiDetail.getDriverId(),"").getData();
            //search.setImageBype(bytes);
            //String byteString = Base64.getEncoder().encodeToString(bytes);
            //search.setImage(byteString);
            String url = null;
            TaxiDetail taxiDetail = null;
            if(taxisDTO.getId() != null && !"".equalsIgnoreCase(taxisDTO.getId()))
                taxiDetail = taxiDetailService.findTaxiId(taxisDTO.getId());
            if(taxiDetail != null) {

                url = getPhotoServiceurl(taxiDetail.getDriverId(),"profile");
                if(url != null && url.length() > 5) {
                    taxisDTO.setImage(url);
                }
                taxisDTO = getUserObject(taxisDTO,taxiDetail);
            }

            //byte[] bytes = Base64.getDecoder().decode(string);
            //String base64String = Base64.encodeBase64String(bytes);
            result.add(taxisDTO);
        }

        //must check DRIVER ARE ONLINE
        sendNotification(searchDTO);*/


        log.info(" notificationModel " );

        return result;
    }




    public TaxisDTO  getUserObject(TaxisDTO taxisDTO,TaxiDetail taxiDetail ){

        if(taxiDetail.getDriverId() != null) {
            User user = userMongoService.findId(taxiDetail.getDriverId());
            if (user != null && user.getName() != null)
                taxisDTO.setName(user.getFirstName());
            if (user != null && user.getPhoneNumber() != null)
                taxisDTO.setPhoneNumber(user.getPhoneNumber());
            if (user != null && user.getToken() != null)
                taxisDTO.setToken(user.getToken());
            if (user != null && user.getNotificationToken() != null)
                taxisDTO.setNotificationToken(user.getNotificationToken());
        }
        return taxisDTO;
    }

    public TaxiPriceDTO  getUserObject(TaxiPriceDTO taxisDTO,TaxiDetail taxiDetail ){

        if(taxiDetail.getDriverId() != null) {
            User user = userMongoService.findId(taxiDetail.getDriverId());
            if (user != null && user.getName() != null)
                taxisDTO.setName(user.getFirstName());
            if (user != null && user.getPhoneNumber() != null)
                taxisDTO.setPhoneNumber(user.getPhoneNumber());
            if (user != null && user.getToken() != null)
                taxisDTO.setToken(user.getToken());
            if (user != null && user.getNotificationToken() != null)
                taxisDTO.setNotificationToken(user.getNotificationToken());
        }
        return taxisDTO;
    }

    public TaxiCompanyDTO  getUserObject(TaxiCompanyDTO taxisDTO,TaxiDetail taxiDetail ){

        if(taxiDetail.getDriverId() != null) {
            User user = userMongoService.findId(taxiDetail.getDriverId());
            if (user != null && user.getName() != null)
                taxisDTO.setName(user.getFirstName());
            if (user != null && user.getPhoneNumber() != null)
                taxisDTO.setPhoneNumber(user.getPhoneNumber());
            if (user != null && user.getToken() != null)
                taxisDTO.setToken(user.getToken());
            if (user != null && user.getNotificationToken() != null)
                taxisDTO.setNotificationToken(user.getNotificationToken());
        }
        return taxisDTO;
    }

    public ModelStatus validationTaxiDetailbyIdSupplierID(TaxiUpdateDTO dto) {
        System.out.println("getTaxiDetailbyId ID " + dto.getTaxiId());
        User user = null;

        TaxiDetail taxiDetails = null;

        log.info(" driverId " + dto.getUserId() + " == taxiID ==" +  " == supplierId ===" + dto.getSupplierId());

        String driverId = dto.getUserId();
        String carType = dto.getCarType();
        String taxiId = dto.getTaxiId();
        String supplierId = dto.getSupplierId();
        int hour = dto.getHour();
        int km = dto.getKm();
        int seats = dto.getSeats();

        if (driverId != null) {


            if (!CARTYPE.check(carType)) {
                return ModelStatus.CAR_TYPE_NOT_EXISTING;
            }
            //first
            Optional<User> ouser=  userRepository.findById(driverId);
            if(ouser.isPresent()) {
                user = ouser.get();
                if (!"".equalsIgnoreCase(dto.getPhoneNumber()) && dto.getPhoneNumber() != null) {
                    userModel.changeNamePhoneNumber(user.getId(),dto.getFirstName(),null,dto.getPhoneNumber());
                }
            }

            if (user != null && user.getRole() != null && (user.getRole().equals(Path.ROLE.DRIVER) || user.getRole().equals(Path.ROLE.DELIVERY))) {
                //second check taxidetails
                 Taxi taxi = taxiService.findId(taxiId,null);

               if(taxi != null)
                    taxiService.updateTaxiType(taxiId,carType);

                taxiDetails = taxiDetailService.findTaxiId(taxiId);
                if (taxiDetails != null && taxiDetails.getId() != null) {
                    //Third chec supplier
                    //log.info(" before  taxiDetails.getSupplier().get().getId() supplierId " + taxiDetails.getSupplier().get().getId());

                    if (taxiDetails.getSupplierId().equals(supplierId)) {
                        log.info(" after taxiDetails.getSupplier().get().getId() supplierId " + taxiDetails.getSupplierId());

                        taxiDetails.setHour(hour);
                        taxiDetails.setKm(km);
                        taxiDetails.setSeats(seats);
                        //taxiDetailRepository.save(taxiDetails);
                        taxiDetailService.updateTaxiIdKm(taxiDetails.getId(),hour,km,seats,dto.getVehicleBrand(),dto.getVehicleYear(),dto.getTaxiNumber());
                        return ModelStatus.SUCCESS;
                    } else
                        return ModelStatus.NO_SUPPLIER_EXISTING;
                } else
                    return ModelStatus.NO_TAXIDETAILS_ID;
            } else
                return ModelStatus.NO_DRIVER_ROLE;
        }
        return ModelStatus.NOTEXISTS;
    }


    public void sendNotification(SearchDTO searchDTO){

        if(searchDTO.getId() != null && !"".equalsIgnoreCase(searchDTO.getId()) && !searchDTO.getId().equalsIgnoreCase("string")) {

            User user = null;
            Optional<User> opuser = userRepository.findById(searchDTO.getId());
            if(opuser.isPresent()) {
                user = opuser.get();
            }
            else {
                log.info("No token found");
            }

            PushNotificationRequests pushNotificationRequests = Converter.toDTO(searchDTO);

            if(user != null)
                pushNotificationRequests.setToken(user.getToken());
            else {
                pushNotificationRequests.setToken("11");
                log.info("No token");
            }

            pushNotificationRequests.setMessage("booking");
            pushNotificationRequests.setTopic("");
            pushNotificationRequests.setTitle("Booking");

            if(searchDTO.getSource() != null)
                pushNotificationRequests.setSource(searchDTO.getSource());
            if(searchDTO.getDestination() != null)
                pushNotificationRequests.setDes(searchDTO.getDestination());

            if(searchDTO.getKm() > 1)
                pushNotificationRequests.setKm(String.valueOf(searchDTO.getKm()));
            else
                pushNotificationRequests.setKm("0");


            notificationModel.add(pushNotificationRequests);
        }

    }
    public TaxiDTO toDTOTaxi(Taxi entity) {
        TaxiDTO taxiDTO = Converter.entityToDto(entity);
        return taxiDTO;
    }
    public TaxiDetailView getTaxiDetailValue(TaxiDetail taxisDetail,TaxiDetailView tv,Taxi taxi){

        if(taxi.getCarType() != null){
            tv.setCartype(taxi.getCarType());
        }
        if(taxi.getCategory() != null){
            tv.setCategory(taxi.getCategory().name());
        }
        if(taxi.getStatus() != null){
            tv.setStatus(taxi.getStatus().name());
        }
        tv.setActive(taxi.isActive());

        if (taxisDetail.getNumberPlate() != null && !taxisDetail.getNumberPlate().isEmpty())
            tv.setNumberPlate(taxisDetail.getNumberPlate());
        if(taxi != null) {
            tv.setLatitude(taxi.getLatitude());
            tv.setLongitude(taxi.getLongitude());
        }
                String  url = getPhotoServiceurl(taxisDetail.getDriverId(),"profile");
                if(url != null && url.length() > 5) {
                    tv.setImageUrl(url);
                }

        return tv;
    }


        //listMyTaxiesBySupplierApproves
    public List<TaxiDetailView> listMyTaxiesDetailBySupplier(String supplierId,String userId)  {

        List<TaxiDetail> ltaxiDetail = null;
        Supplier supplier = supplierModel.getSupplierId(supplierId);

        ltaxiDetail = taxiDetailService.findSupplierId(supplierId);
        //ltaxiDetail = taxiDetailRepository.findBySupplierId(supplierId);
        List<TaxiDetailView> taxiListNew = new ArrayList<TaxiDetailView>(0);

        System.out.println("userId 2=> " + userId  + " total count " + ltaxiDetail.size());

        //if(!(supplier.getUser() != null) && (supplier.getUser().get().getId().equals(userId)))
        //  return null;

        if (supplier != null && supplier.getId() != null){
            System.out.println("supplier => " + supplier.getId());
            for (TaxiDetail taxisDetail : ltaxiDetail) {
                Taxi taxi = null;
                String driverId = null;
                User driver = null;

                try {
                    taxi = getTaxiIds(taxisDetail.getTaxiId());
                    driverId = taxisDetail.getDriverId();
                    System.out.println("taxi ==> " + taxi + " driverId ==> " + driverId);

                    if (driverId != null)
                        driver = userModel.getById(driverId);
                  if ((taxi != null && taxi.getId() != null && taxi.getId() != null ) && (taxi.isActive() || !taxi.isActive())) {

                        TaxiDetailView tv = new TaxiDetailView(taxi, taxisDetail);

                        //TaxiDetailView tv = new TaxiDetailView(taxi, taxisDetail);
                        //tv.setPhoneNumber(taxisDetail.);


                        tv = getTaxiDetailValue(taxisDetail,tv,taxi);

                        if (driver != null && driver.getPhoneNumber() != null && !"".equalsIgnoreCase(driver.getPhoneNumber()))
                            tv.setPhoneNumber(driver.getPhoneNumber());
                        if (driver != null && !"".equals(driver.getFirstName())) {
                            tv.setName(getName(driver));
                        }
                        else if (driver != null) {
                            tv.setName(getName(driver));
                        }

                        else if(driver == null){
                            System.out.println(" delete this,no user driver id --->" + taxi.getId());
                        }
                        taxiListNew.add(tv);
                    }
                }
                catch(Exception e){
                    log.info("Exception " + e);
                }
                System.out.println( "Count --->" + taxiListNew.size());
            }
        }
        return taxiListNew;
    }

    public List<TaxiDetailView> listMyTaxiesDetailBySupplierPhoneNumber(String supplierId,String userId,String phoneNumber,String numberPlate,String name)  {

        List<TaxiDetail> ltaxiDetail = null;

        Supplier supplier = supplierModel.getSupplierId(supplierId);

        ltaxiDetail = taxiDetailService.findSupplierId(supplierId);

        if(numberPlate != null && !numberPlate.startsWith("string") && numberPlate.length() > 1)
            ltaxiDetail = taxiDetailService.findSupplierIdNumberPlate(supplierId,numberPlate);
       else if(name != null && !name.startsWith("string") && name.length() > 1) {
           User user = userRepository.findByfirstNameContainingIgnoreCase(name);
            ltaxiDetail = taxiDetailService.findSupplierIdDriverId(supplierId, user.getId());
        }
        else if(phoneNumber != null && !phoneNumber.startsWith("string") && phoneNumber.length() > 1) {
             User user = userModel.getByPhoneNumber(phoneNumber);
             ltaxiDetail = taxiDetailService.findSupplierIdDriverId(supplierId, user.getId());
        }


        //ltaxiDetail = taxiDetailRepository.findBySupplierId(supplierId);
        List<TaxiDetailView> taxiListNew = new ArrayList<TaxiDetailView>(0);

        System.out.println("userId 2=> " + userId  + " total count " + ltaxiDetail.size());

        //if(!(supplier.getUser() != null) && (supplier.getUser().get().getId().equals(userId)))
        //  return null;

        if (supplier != null && supplier.getId() != null){
            System.out.println("supplier => " + supplier.getId());
            for (TaxiDetail taxisDetail : ltaxiDetail) {
                Taxi taxi = null;
                String driverId = null;
                User driver = null;

                try {
                    taxi = getTaxiIds(taxisDetail.getTaxiId());
                    driverId = taxisDetail.getDriverId();
                    System.out.println("taxi ==> " + taxi + " driverId ==> " + driverId);

                    if (driverId != null)
                        driver = userModel.getById(driverId);
                    if ((taxi != null && taxi.getId() != null && taxi.getId() != null ) && (taxi.isActive() || !taxi.isActive())) {

                        TaxiDetailView tv = new TaxiDetailView(taxi, taxisDetail);

                        //TaxiDetailView tv = new TaxiDetailView(taxi, taxisDetail);
                        //tv.setPhoneNumber(taxisDetail.);


                        tv = getTaxiDetailValue(taxisDetail,tv,taxi);

                        if (driver != null && driver.getPhoneNumber() != null && !"".equalsIgnoreCase(driver.getPhoneNumber()))
                            tv.setPhoneNumber(driver.getPhoneNumber());
                        if (driver != null && !"".equals(driver.getFirstName())) {
                            tv.setName(getName(driver));
                        }
                        else if (driver != null) {
                            tv.setName(getName(driver));
                        }

                        else if(driver == null){
                            System.out.println(" delete this,no user driver id --->" + taxi.getId());
                        }



                        taxiListNew.add(tv);
                    }
                }
                catch(Exception e){
                    log.info("Exception " + e);
                }
                System.out.println( "Count --->" + taxiListNew.size());
            }
        }
        return taxiListNew;
    }



    public List<TaxiDetailView> listMyTaxiesDetail(String phoneNumber,boolean isActive)  {

        //phoneNUmberUser
        User user = userRepository.findByPhoneNumber(phoneNumber);


        List<TaxiDetailView> taxiListNew = new ArrayList<TaxiDetailView>(0);

        //TaxiDetail ltaxiDetail = taxiDetailRepository.findByDriverId(user.getId());
        TaxiDetail ltaxiDetail = taxiDetailService.findDriverId(user.getId());

        if(ltaxiDetail != null){
            //for (TaxiDetail taxisDetail : ltaxiDetail) {
                Taxi taxi = null;
                String driverId = null;
                User driver = null;

                try {
                    taxi = getTaxiIds(ltaxiDetail.getTaxiId());




                    driverId = ltaxiDetail.getDriverId();

                    System.out.println("taxi ==> " + taxi + " driverId ==> " + driverId);

                    if (driverId != null)
                        driver = userModel.getById(driverId);



                    if ((taxi != null && taxi.getId() != null && taxi.getId() != null ) && (taxi.isActive() == isActive)) {

                        TaxiDetailView tv = new TaxiDetailView(taxi, ltaxiDetail);

                        //TaxiDetailView tv = new TaxiDetailView(taxi, taxisDetail);
                        //tv.setPhoneNumber(taxisDetail.);
                        if(taxi.getCarType() != null){
                            tv.setCartype(taxi.getCarType());
                        }
                        if(taxi.getCategory() != null){
                            tv.setCategory(taxi.getCategory().name());
                        }
                        if(taxi.getStatus() != null){
                            tv.setStatus(taxi.getStatus().name());
                        }
                        tv.setActive(taxi.isActive());

                        if (ltaxiDetail.getNumberPlate() != null && !ltaxiDetail.getNumberPlate().isEmpty())
                            tv.setNumberPlate(ltaxiDetail.getNumberPlate());
                        if (driver != null && driver.getPhoneNumber() != null && !"".equalsIgnoreCase(driver.getPhoneNumber()))
                            tv.setPhoneNumber(driver.getPhoneNumber());
                        //if (driver != null && driver.getImageInfo() != null) {
                        //   tv.setImageInfo(driver.getImageInfo());
                        //}
                        if (driver != null && !"".equals(driver.getFirstName())) {
                            tv.setName(getName(driver));
                        }
                        else if (driver != null) {
                            tv.setName(getName(driver));
                        }
                        else if(driver == null){
                            System.out.println(" delete this,no user driver id --->" + taxi.getId());
                        }

                        System.out.println( "id --->" + tv.getId());
                        taxiListNew.add(tv);
                    }

                }
                catch(Exception e){
                    log.info("Exception " + e);
                }

                System.out.println( "Count --->" + taxiListNew.size());
            }
        return taxiListNew;
    }

    public List<TaxiDetailView> listMyTaxiesDetail(List<TaxiDetail> ltaxiDetail,boolean isActive)  {

        List<TaxiDetailView> taxiListNew = new ArrayList<TaxiDetailView>(0);
         if(ltaxiDetail != null && ltaxiDetail.size() > 0)
            for (TaxiDetail taxisDetail : ltaxiDetail) {
                Taxi taxi = null;
                String driverId = null;
                User driver = null;

                try {
                    taxi = getTaxiIds(taxisDetail.getTaxiId());




                    driverId = taxisDetail.getDriverId();

                    System.out.println("taxi ==> " + taxi + " driverId ==> " + driverId);

                    if (driverId != null)
                        driver = userModel.getById(driverId);



                    if ((taxi != null && taxi.getId() != null && taxi.getId() != null ) && (taxi.isActive() == isActive)) {

                        TaxiDetailView tv = new TaxiDetailView(taxi, taxisDetail);

                        //TaxiDetailView tv = new TaxiDetailView(taxi, taxisDetail);
                        //tv.setPhoneNumber(taxisDetail.);
                        if(taxi.getCarType() != null){
                            tv.setCartype(taxi.getCarType());
                        }
                        if(taxi.getCategory() != null){
                            tv.setCategory(taxi.getCategory().name());
                        }
                        if(taxi.getStatus() != null){
                            tv.setStatus(taxi.getStatus().name());
                        }
                        tv.setActive(taxi.isActive());

                        if (taxisDetail.getNumberPlate() != null && !taxisDetail.getNumberPlate().isEmpty())
                            tv.setNumberPlate(taxisDetail.getNumberPlate());
                        if (driver != null && driver.getPhoneNumber() != null && !"".equalsIgnoreCase(driver.getPhoneNumber()))
                            tv.setPhoneNumber(driver.getPhoneNumber());
                        //if (driver != null && driver.getImageInfo() != null) {
                        //   tv.setImageInfo(driver.getImageInfo());
                        //}
                        if (driver != null && !"".equals(driver.getFirstName())) {
                            tv.setName(getName(driver));
                        }
                        else if (driver != null) {
                            tv.setName(getName(driver));
                        }
                        else if(driver == null){
                            System.out.println(" delete this,no user driver id --->" + taxi.getId());
                        }


                        //CK
                        /*if (driverId != null) {
                            tv.setAmount(getTopup(driverId));
                        }*/

                        System.out.println( "id --->" + tv.getId());
                        taxiListNew.add(tv);
                    }

                }
                catch(Exception e){
                    log.info("Exception " + e);
                }

                System.out.println( "Count --->" + taxiListNew.size());
            }
        return taxiListNew;
    }


    public boolean validateSupplier(TaxiDetail isTaxiDetailExisting, TaxiDTO dto) {

        if (isTaxiDetailExisting.getSupplierId().equals(dto.getSupplierId())) {
            return true;
        }
        return false;
    }

    public ModelStatus updateTaxiInfo(TaxiDTO dto)  {
        Taxi taxi = null;
        //supplier
        List<TaxiDetail> ltaxiDetailList = taxiDetailService.findSupplierId(dto.getSupplierId());
        TaxiDetail ltaxiDetail = null;
        //Driver id
        for(TaxiDetail ltaxiDetail2 : ltaxiDetailList) {

            String driverID = ltaxiDetail2.getDriverId();
            if (driverID != null && driverID.equalsIgnoreCase(dto.getUserId())) {
                ltaxiDetail = ltaxiDetail2;
            }

        }


        log.info(" --Before --" + ltaxiDetail.getId());

        if (validateTaxiDetailsId(ltaxiDetail)) {
            if (ltaxiDetail != null && validateSupplier(ltaxiDetail, dto)) {

                TaxiDetail isTaxiDetailExisting = taxiDetailService.findTaxiId(dto.getTaxiId());
                String taxiId = isTaxiDetailExisting.getTaxiId();

                log.info(" --isTaxiDetailExisting--" + ltaxiDetail.getId());
                //number
                //seats
                // yearBefore
                if ((isTaxiDetailExisting != null && isTaxiDetailExisting.getId() != null)) {
                    log.info(" --now updating --" + ltaxiDetail.getId());

                    if (dto.getYear() > 0)
                        isTaxiDetailExisting.setYear(dto.getYear());
                    if (!"".equals(dto.getTaxiNumber()) && dto.getTaxiNumber() != null)
                        isTaxiDetailExisting.setNumberPlate(dto.getTaxiNumber());
                    if (dto.getSeats() > 0)
                        isTaxiDetailExisting.setSeats(dto.getSeats());
                    if (dto.getTaxiNumber() != null && !dto.getTaxiNumber().equals(""))
                        isTaxiDetailExisting.setNumberPlate(dto.getTaxiNumber());
                    if (dto.getVehicleBrand() != null && !dto.getVehicleBrand().equals(""))
                        isTaxiDetailExisting.setVehicleBrand(dto.getVehicleBrand());
                    if (!"".equals(dto.getPrice()) && dto.getPrice() != null)
                        isTaxiDetailExisting.setPrice(dto.getPrice());
                    if (!"".equals(dto.getBasePrice()) && dto.getBasePrice() > 0)
                        isTaxiDetailExisting.setBasePrice(dto.getBasePrice());
                    if (!"".equals(dto.getPeakPrice()) && dto.getPeakPrice() > 0)
                        isTaxiDetailExisting.setPeakPrice(dto.getPeakPrice());
                    else
                        isTaxiDetailExisting.setPeakPrice(3.0);
                    if (dto.getPerDay() != null && !"".equals(dto.getPerDay()) && dto.getPerDay() > 0)
                        isTaxiDetailExisting.setPerDay(dto.getPerDay());
                    else
                        isTaxiDetailExisting.setPerDay(100.0);

                    isTaxiDetailExisting.setHour(dto.getHour());
                    isTaxiDetailExisting.setKm(dto.getKm());
                    //log.info(" -- before Car Type --" + dto.getCarType());

                    if (taxiId != null && CARTYPE.check(dto.getCarType()) && CARTYPE.valueOf(dto.getCarType()) != null) {
                        taxi = taxiService.findId(taxiId,null);
                        taxi.setCarType(dto.getCarType());
                        //taxiRepository.save(taxi);
                        log.info(" --now Taxi updated --" + ltaxiDetail.getId());
                    } else {
                        return ModelStatus.CAR_TYPE_NOT_EXISTING;
                    }

                    log.info(" .. getTags .." + dto.getTags());

                    try {
                        taxiDetailRepository .save(isTaxiDetailExisting);
                        log.info(" --now Taxi Details completed --" + ltaxiDetail.getId());
                    } catch (DatabaseException e) {

                    }
                    return ModelStatus.UPDATED;

                }
                return ModelStatus.NO_SUPPLIER_EXISTING;
            }
            return ModelStatus.NO_TAXIDETAILS_ID;
        }
        return ModelStatus.NO_SUPPLIER_EXISTING;
    }


    private boolean getTopup(){

        return false;
    }

    public float getTopup(String driverId){
        DriverBillingDTO dto =  driverBill.get(driverId);
        if(dto != null && dto.getCredit() > 0)
            return dto.getCredit();
        else
            return 0;
    }

    public String getName(User user){
        String driverName = user.getFirstName();
        if(driverName != null)
            return driverName = (user.getFirstName() != null) ? user.getFirstName() : user.getLastName();
        else
            return driverName = (user.getLastName() != null) ? user.getLastName() : user.getLastName();

    }

    public List<TaxiView> listTaxiForApprovals(String supplierId)  {

        /*List<Taxi> ltaxi = taxiService.listTaxiForApprovals();
        List<TaxiView> taxiListNew = new ArrayList<TaxiView>(0);

        List<TaxiDetail > taxiList = taxiDetailService.listBySupplier(supplierId);
        TaxiView taxiview = null;
        Taxi taxis = null;
        for(TaxiDetail taxisDetail :taxiList){

            taxis = getTaxiById(taxisDetail.getTaxiId());

            if(taxis != null && !taxis.getActive()) {
                taxiview = new TaxiView(taxis, taxisDetail);
                taxiListNew.add(taxiview);
            }
        }


        return taxiListNew;*/
        return null;
    }

    public CATEGORY getCarType(MobileDTO userDTO) {
        if (!"".equalsIgnoreCase(userDTO.getCategory()) && userDTO.getCategory() != null && !userDTO.getCategory().equalsIgnoreCase("String")) {
            return CATEGORY.valueOf(userDTO.getCategory().toUpperCase());
        }
        return CATEGORY.TAXI;
    }



    public Taxi getTaxiIds(String id){
       return taxiService.findId(id,null);
    }

    public boolean deleteTaxiDetails(String id){

        taxiService.deleteTaxi(id);
        log.info("taxiDetail id --->" + id);

        TaxiDetail taxiDetail = taxiDetailService.findTaxiId(id);
        log.info("taxiDetail id --->" + taxiDetail.getId());

        taxiDetailService.deleteTaxiDetail(id);
        log.info("deleteTaxiDetails id --->" + taxiDetail.getDriverId());
        //userMongoService.updateDeleteFlag(taxiDetail.getDriverId());
        userMongoService.delete(taxiDetail.getDriverId());

        return true;
    }



}
