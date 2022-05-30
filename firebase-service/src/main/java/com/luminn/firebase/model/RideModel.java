package com.luminn.firebase.model;

import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.*;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.*;
import com.luminn.firebase.request.TripPoolRequest;
import com.luminn.firebase.service.RideService;
import com.luminn.firebase.service.TaxiDetailService;
import com.luminn.firebase.service.TripPoolService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.firebase.util.*;
import com.luminn.view.StatusModelResponse;
import com.luminn.view.StatusResponse;

import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class RideModel {

    @Autowired
    RideService rideService;

    @Autowired
    TripPoolService tripPool;

    @Autowired
    UserModel userModel;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    TaxiDetailModel taxiDetailModel;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    TaxiDetailService taxiDetailService;


   @Autowired
   CouponRepository coupanRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaxiDetailRepository taxiDetailRepository;

    @Value("${timeZone}")
    private String timeZone;

    @Value("${cancelFee}")
    private float cancelFee;

    @Value("${country}")
    private String country;

    @Autowired
    UserMongoService userService;

    @Autowired
    PriceModel priceModeL;

    @Autowired
    ReviewModel reviewModel;

    @Autowired
    private EmailModel emailModel;


    @Autowired
    private DistanceCalculation distanceCalculation;


    @Autowired
    DriverBillingModel driverBillingModel;

    @Autowired
    DriverBillRepository driverBillRepository;

    public String globalRideId = null;
    private static final Logger log = LoggerFactory.getLogger(RideModel.class);

    public void add(TripPoolRequest tripPoolRequest){

        String tripId = tripPoolRequest.getTripId();
        String status = tripPoolRequest.getStatus();
        String driverID = tripPoolRequest.getDriverId();

        RideDTO rideDTO = new RideDTO();
        String driverIds = null;
        rideDTO.setRideStatus(RIDESTATUS.ACCEPTED);
        log.info(" RideModel ");

        if(driverID == null){
            Document tripPoolDocument =  tripPool.findTripId(tripId);
            User user = userModel.getToken(tripPoolDocument.get("token").toString());
            driverIds = user.getId();

            rideDTO = getTripPoolInfo(rideDTO,tripPoolDocument);

        }
        else {

            log.info(" RideModel out ");
            //User user = userModel
            //ride.setDriverIds(driverId);
        }

        rideService.add(tripId,rideDTO,driverIds,driverIds);
    }

    public RideDTO getTripPoolInfo(RideDTO rideDTO,Document tripPoolDocument){

        rideDTO.setDestination(tripPoolDocument.get("desc").toString());
        rideDTO.setSource(tripPoolDocument.get("source").toString());
        rideDTO.setKm(Float.valueOf(tripPoolDocument.get("km").toString()));
        return rideDTO;

    }

    public String getGlobalRideId() {
        return globalRideId;
    }

    public void setGlobalRideId(String globalRideId) {
        this.globalRideId = globalRideId;
    }

    public String getDriver(String tripId){
        Document document =  this.tripPool.findTripId(tripId);
        User user =  userModel.getToken(document.get("token").toString());
        return user.getId();
        //Document document = tripPool.get(token);
    }

    public String getUserRole(String id) {

         User user = null;
         user = userService.getById(id);

        if (user != null && user.getRole() != null)
            return user.getRole();

        if (user == null)
            return "";
        return "";
    }


    public List<RideDTO> getRidetoDTO(List<Ride> rides)  {

        List<RideDTO> rideDTOList = new ArrayList<>();
                for(Ride ride : rides) {
                    if ( ride != null) {

                        RideDTO rideDTO = Converter.entityToDTO(ride, null);

                        if (ride.getDriverId() != null) {
                            rideDTO.setDriverName(getUserorDriverName(ride.getDriverId()));
                        }
                        if (ride.getUserId() != null) {
                            User users = getUserorDriver(ride.getUserId());
                            rideDTO.setUserName(getUserorDriverName(ride.getDriverId()));
                        }
                        rideDTOList.add(rideDTO);
                    }
                }
            return rideDTOList;
    }


    public List<RideDTO> getRideIdByDTO(String id)  {

        User user = userService.getById(id);
        List<RideDTO> rideDTOList = new ArrayList<>();
        Long taxiDetailId = null;
        RideDTO rideDto = null;
        List<Ride> rides = null;
       // Page<Ride> page = null;
        if (user != null) {

            if (user != null && user.getRole().equalsIgnoreCase(Path.ROLE.DRIVER)) {
                rides = rideRepository.findByDriverIdOrderByStartDateDesc(id);
            }
            if (user != null && user.getRole().equalsIgnoreCase(Path.ROLE.USER)) {
                rides = rideRepository.findByUserIdOrderByStartDateDesc(id);
            }
            if (rides != null && rides.size() >0) {

                for(Ride ride : rides) {
                    if ( ride != null) {

                        RideDTO rideDTO = Converter.entityToDTO(ride, null);

                        if (ride.getDriverId() != null) {
                            rideDTO.setDriverName(getUserorDriverName(ride.getDriverId()));
                        }
                        if (ride.getUserId() != null) {
                            User users = getUserorDriver(ride.getUserId());
                            rideDTO.setUserName(getUserorDriverName(ride.getDriverId()));
                        }
                        rideDTOList.add(rideDTO);
                    }
                }

                return rideDTOList;
            }
            return rideDTOList;
        } else
            return rideDTOList;
    }



    private String getUserorDriverName(String id) {
        try {
            User user =  userModel.getById(id);
            if(user != null)
                return user.getFirstName();
        } catch (Exception e) {
            return "NO NAME";
        }
        return "NO NAME";
    }


    public List<RideDTO> getAllDriversByUser(String id, Date today)  {
        User user = userService.getById(id);
        List<Ride> lride = null;
        List<RideDTO> rideDTO = new ArrayList<>(0);
        String userId = null;
        List<RideDTO> rideDTOFromDocument = null;
        if (user != null) {
            /*if (today != null) {
                lride = rideService.listByUsersByDate(id, new Date());
            } */
            {
                //listByuser
                rideDTOFromDocument = rideService.listByuser(id);
                // lride =  rideRepository.findByUserIdOrderByStartDateDesc(id);

            }

            rideDTO = new ArrayList<>(0);
            userId = user.getId();
            for (RideDTO ridedtos : rideDTOFromDocument) {
                //RideDTO dto = Converter.entityToDTO(ride, null);

                /*if (ride.getDriverId() != null) {
                    dto.setDriverName(getUserorDriverName(ride.getDriverId()));
                   /* if (driver.getImageInfo() != null) {
                        dto.setImageInfo(driver.getImageInfo());
                    }*/
                //}

                /*if (ride.getUserId() != null) {
                    dto.setUserName(getUserorDriverName(ride.getUserId()));
                }*/

                if (ridedtos != null && ridedtos.getId() != null ) {
                    //rideDto = Converter.entityToDTO(ride, null);
                    User users = null;
                    if (getUserorDriver(ridedtos.getUserId()) != null) {
                        {
                            users = getUserorDriver(ridedtos.getUserId());
                            if(users != null && users.getId() != null) {
                                //String idss = users.getId().toString();
                                //System.out.println(" idss -->" + idss);
                                //rideDto.setUserId(idss);
                            }
                        }
                        //Image is missing
                    }
                    ridedtos = getName(ridedtos, false);
                }
                rideDTO.add(ridedtos);
                //CK
                //dto = getRatingComments(ride.getId(), dto, "USER");
                //log.info(" ---PART 1 -AFTER double getPrice --" + dto.getPrice() + " ..getTotalPrice..." + dto.getTotalPrice() + " userTotalPrice" + dto.getUserTotalPrice());
                //CK
                //dto = getName(ride, dto, true);
            }
            return rideDTO;
        } else
            return rideDTO;
    }
    public List<RideDTO> listBySupplierId(String supplierId){
           return rideService.listBySupplierId(supplierId);
    }

    public List<RideDTO> listBySupplierIdStatus(String supplierId,String status){
        return rideService.listBySupplierId(supplierId,status);
    }

    public List<RideDTO> listBySupplierPageinationId(String supplierId){
        return rideService.listBySupplierId(supplierId);
    }

    public List<RideDTO> listByCreationDate(String date){
        return rideService.listByCreationDate(date);
    }

    public List<RideDTO> getAllUsersByDriver(String id, Date today)  {

        User user = userService.getById(id);
        List<Ride> lride = null;

        List<RideDTO> rideDTOFromDocument = null;
        List<RideDTO> rideDTOList = null;
        Long taxiDetailId = null;

        log.info(" --user --" + user.getId());

        if (user != null) {

            //if (today != null) {
            //lride = rideService.listByDriversByDate(id, DateUtil.getNumberDay(today));
            //  log.info("--Date--" + lride.size());
            //}
            {
                rideDTOFromDocument = rideService.listByDrivers(id);
            }

            // lride = rideService.listByDrivers(id);
            rideDTOList = new ArrayList<>(0);

            for (RideDTO ridedtos : rideDTOFromDocument) {
                RideDTO rideDto = null;
                log.info(" --ride.getId().longValue() --" + ridedtos.getId());

                if (ridedtos != null && ridedtos.getId() != null ) {
                    //rideDto = Converter.entityToDTO(ride, null);
                    User users = null;
                    if (getUserorDriver(ridedtos.getUserId()) != null) {
                        {
                            users = getUserorDriver(ridedtos.getUserId());
                            if(users != null && users.getId() != null) {
                                //String idss = users.getId().toString();
                                //System.out.println(" idss -->" + idss);
                                //rideDto.setUserId(idss);
                            }
                        }
                        //Image is missing
                    }
                    //CK
                    //rideDto = getRatingComments(ride.getId(), rideDto, "DRIVER");
                    //CK
                    ridedtos = getName(ridedtos, false);
                }
                rideDTOList.add(ridedtos);
            }
            return rideDTOList;
        } else
            return rideDTOList;
    }


    public ModelStatus rideUpdate(String category,String rideId, float distance, float priceInput,float elapsedTime,float waiting,String region,String status,
                                  double latitude ,double longitude,double sourceLatitude ,double sourceLongitude,float travelTime,float fixedPrice,String coupan)  {
        float baseFare = 0.0f;

        float miniMumFare = 0.1f;
        float times = 0.1f;
        float  percentage =0;
        float  price = 0;
        float totalTravelTimes = 0;
        float originalKm =0;

        //float waitingTime = waitingTime;
        float localDistance = 0;
        Ride ride = null;
        DistanceTime matrix = null;
        String domain = null;
        String supplierId = null;

        originalKm = distance;
        if(rideId != null) {
            Optional<Ride> opt = rideRepository.findById(rideId);

            if(opt.isPresent())
                ride = opt.get();
            else
                return ModelStatus.RIDE_EXCEPTION;
        }
        if (!validation(rideId, distance, priceInput)) {
            return ModelStatus.RIDE_EXCEPTION;
        } else {

            ride = updateLogStatus(ride,status);
            loginUpdate(ride.getUserId(),ride.getDriverId());
            PriceMap priceMapInput = new PriceMap();

            //int rideChangeTime = ride.getUpdatedOn().getHours();
            TaxiDetail taxiDetail =  taxiDetailService.finByDriverId(ride.getDriverId());

            if(taxiDetail == null)
                 return ModelStatus.EXCEPTION;

            if(taxiDetail != null && taxiDetail.getSupplierId() != null)
                supplierId = taxiDetail.getSupplierId();
            //we dont this if we take cancel fee in TaxiDetails table
            //Taxi taxi = taxiModel.getTaxi(taxiDetail.getTaxiId());

            //taxiDetail.setBasePrice(0);
            if( taxiDetail.getBasePrice() > 1){
                priceMapInput =  getPricefromTaxiDetailsTable(taxiDetail);
                baseFare = priceMapInput.getBasePrice();
            }
            else {
                baseFare = 0;
            }

            //take category from
            category = getCategory(ride,category);

            if(status.equalsIgnoreCase(DRIVERSTATUS.RIDE_FINISH.name())) {

                //CK
                //taxiModel.addGeoLocationWithStatus(taxiDetail.getTaxiId(),(float)latitude,(float)longitude,DRIVERSTATUS.STOP.name());
            }
            else if(status.equalsIgnoreCase(DRIVERSTATUS.RIDE_ON.name())) {

            }

            if(matrix != null && matrix.getDistance() > 0) {
                distance = checkDistance(distance, matrix.getDistance() / 1000);
                log.info("Distance -->" + distance);
            }

            baseFare = 0;

            //Get domain from driver
            //find a value for discount label and code
            String driverDomain = getDomain(ride.getDriverId());
            log.info(" DriverDomain -->" + driverDomain +  "CarType " + ride.getType()  + "distance " +  distance  +"Waiting --->" + waiting  + " TravelTime --> " + travelTime);

            // we dont this for fixed Price
            PriceMap map = new PriceMap();
            if(fixedPrice == 0) {
                map = updateCalculation(category, ride.getType(), region, baseFare, distance, waiting, ride.getPayment(),
                        ride.getDiscountPrice(), driverDomain, elapsedTime, travelTime,supplierId,coupan);

                log.info("PriceMap --> output " + map.toString());
                log.info("gm --> tTotalTravelTime " + map.getTotalTravelTime());
            }
            else {
                log.info("Fixedprice calucation ");
            }

            //REDUCE MONEY FROM DEPOSIT
            if( !"".equalsIgnoreCase(status) && (status != null && status.equalsIgnoreCase(DRIVERSTATUS.RIDE_FINISH.name()) )
                    && ride.getPayment().equalsIgnoreCase("CASH")){
                percentage =  driverBillingModel.calculatePercentage(map.getPercentage(),ride.getDriverId());
                ride.setDebit(percentage);
                ride.setEndDate(new Date());
            }

            else if( !"".equalsIgnoreCase(status) && (status != null && status.equalsIgnoreCase(DRIVERSTATUS.RIDE_FINISH.name()) )
                    && ride.getPayment().equalsIgnoreCase("CARD")){
                //percanetage calucation
                //map.setUserTotalPrice(100);
                //map.setTotalTravelTime(100);
                ride.setTax(10);
                ride.setEndDate(new Date());
            }

            //ride.setTravelTime(map.getTotalTravelTime());
            ride.setTax(map.getTax());

            // ride.setTravelTime(map.getT);
            ride.setBasePrice(map.getBasePrice());

            log.info("--- map.getPrice() --->"  + map.getPrice() + "TotalWaitPrice" + map.getTotalWaitTimePrice());
            ride.setPrice(map.getPrice());

            log.info("--- km --->" + map.getKm() + "Base km" + map.getBaseKm());
            ride.setBaseKm(map.getBaseKm());

            if(map.getKm() > 0) {
                float total = map.getKm() + map.getBaseKm();
                ride.setKm(total);
            }
            else
                ride.setKm(map.getRealKm());

            ride.setLocalKm(distance);
            // what is different ---elaspsed --NOT USING IT AT THE MOMENT
            ride.setEstimateTime(map.getEstimateTime());
            //ride.setTotalwaitTime(map.getTotalWaitTime());
            ride.setTotalwaitTime(map.getTotalWaitTime());
            ride.setDiscountPrice(map.getDiscount());

            //check Minimum Price or Not
            System.out.println("Only Fixed price -->" + fixedPrice);


            if(fixedPrice != 0){
                ride.setUserTotalPrice(fixedPrice);
                ride.setTotalPrice(fixedPrice);
                ride.setPrice(fixedPrice);
                log.info("1yes_fixedPrice " + fixedPrice );
            }
            else if(fixedPrice == 0 && map.getMinimumFare()>  0 && map.getMinimumFare()> map.getUserTotalPrice())
            {
                ride.setUserTotalPrice(map.getMinimumFare());
                ride.setTotalPrice(map.getMinimumFare());

                log.info("no_fixedPrice> 1"  );
            }
            else if(fixedPrice == 0 ) {
                ride.setUserTotalPrice(map.getUserTotalPrice());
                ride.setTotalPrice(map.getTotalPrice());

                log.info("no_fixedPrice> 2"  );
            }

            log.info("---Map.getTotalTravelTime() --->" + map.getTotalTravelTime());
            if(map.getTotalTravelTime() > 0) {
                ride.setTotalTravelTimePrice(map.getUserTravelPrice());
                log.info("---Inside .getUserTravelPrice() --->" + ride.getTotalTravelTimePrice());
            }

            ride.setEndDate(new Date());
            ride.setPercentage(map.getPercentage());
            ride.setTravelTime(elapsedTime);
            //setUserTravelPrice
            log.info("Original " + originalKm + " km" + ride.getKm());

            setJsonValue(ride,null);
            log.info("---ride.getTotalPrice() --->" + ride.getTotalPrice());
            //rideRepository.update(Ride.class, ride.getId(), ride);

            rideRepository.save(ride);

            //once save, call to billing to reduce the rate
            Optional<DriverBill> optlist = driverBillRepository.findByDriverId(ride.getDriverId());
            if(optlist.isPresent()){
                DriverBill driverBill = optlist.get();
                driverBillingModel.updateDebit(ride.getDriverId(),ride.getPercentage());
            }

            return ModelStatus.RIDE_UPDATE;
        }
    }

    public String getDomain(String driverId){

        String domain = null;
        User user =  userModel.getById(driverId);
        if(user != null){
            domain =  user.getDomain() ;
            //if this null, get it from supplier
        }

        return domain;
    }



    public float checkDistance(float appDistance,float mapDistance){

        float dis = 0;
        if(appDistance >= mapDistance)
            return appDistance;

        return mapDistance;

    }
    private String getCategory(Ride ride,String category){

        //Taxi taxi = taxiModel.getTaxiIds(taxiDetail.getTaxiId());
        if(ride != null && ride.getCategory() != null) {
            //category = taxi.getCategory().name();
            return ride.getCategory();
        }
        else if(!"".equalsIgnoreCase(category) && category != null)
            return category;
        else
            return category = "Taxi";

    }

    public PriceMap getPricefromTaxiDetailsTable(TaxiDetail taxiDetail){

        float baseFare = 3.0f;
        float waitingTime = 0.30f;

        float miniMumFare = 0.1f;
        float times = 0.1f;
        float  percentage =0;
        float  price = 0;
        PriceMap priceMapInput = new PriceMap();
        baseFare = (float)taxiDetail.getBasePrice();
        String hr = DateUtil.getTimeZone(null,country);
        int hours = Integer.valueOf(hr).intValue();

        if(findPeakTime(hours)) {
            price = (float)taxiDetail.getPeakPrice();
        }
        else {
            price = (float)taxiDetail.getPrice();
        }
        if((float)taxiDetail.getWaitingTime() > 0) {
            waitingTime = (float) taxiDetail.getWaitingTime();
        }
        if(taxiDetail.getMiniMumFare() > 0){
            miniMumFare = taxiDetail.getMiniMumFare();
        }

        priceMapInput.setPrice(price);
        priceMapInput.setBasePrice(baseFare);
        priceMapInput.setWaitTime(waitingTime);
        priceMapInput.setMinimumFare(miniMumFare);
        return priceMapInput;
    }

    private Ride updateLogStatus(Ride ride,String status){
        if(!"".equals(status) && status.equalsIgnoreCase(RIDESTATUS.CANCEL_BY_USER.name())) {
            ride.setStatus(RIDESTATUS.CANCEL_BY_USER);
        }
        else if(!"".equals(status) && status.equalsIgnoreCase(RIDESTATUS.CANCEL_BY_DRIVER.name())){
            ride.setStatus(RIDESTATUS.CANCEL_BY_DRIVER);
        }
        else {
            ride.setStatus(RIDESTATUS.FINISH);
        }
        return ride;
    }

    private void loginUpdate(String userId,String driverId){
        //Driver update
        if (getUserorDriver(userId) != null)
            loginStatus(STEPS.RIDE_FINISH, getUserorDriver(userId));
        if (getUserorDriver(driverId) != null)
            loginStatus(STEPS.RIDE_FINISH, getUserorDriver(driverId));
    }

    public void loginStatus(String status, User user) {
        user.setStatus(status);

            //userService.update(User.class, user.getId(), user);

    }

    public Ride getRide(String rideId)  {
        Optional<Ride> ride =  rideRepository.findById(rideId);
         if(ride.isPresent())
             return ride.get();
        return null;
    }

    public boolean validation(String ride, float distance, float price) {
        if (ride == null) {
            return false;
        }
        if (distance > 20000f) {
            return false;
        }
        if (price > 1008) {
            return false;
        }
        return true;
    }

   /* public String getRideIdByDriver(String id)  {

        User user = userService.getById(id);
        List<Ride> lride = null;
        List<RideDTO> rideDTOList = null;
        Long taxiDetailId = null;
        log.info(" ---- user --" + id);
        Ride ride = null;

        if (user != null) {


            if (user != null && user.getRole().equalsIgnoreCase(Path.ROLE.DRIVER))
                ride = rideRepository.findByDriverId(id);
            else if (user != null && user.getRole().equalsIgnoreCase(Path.ROLE.USER))
                ride = rideRepository.findByUserId(id);

            if (ride != null && ride.getId() != null && ride.getId() !=  null) {


                log.info(" ---- double getPrice --" + ride.getId());
                return ride.getId();


            } else
                return "-1L";
        } else
            return "-1L";
    }*/


    private User getUserorDriver(String id) {
        try {
            return userModel.getById(id);
        } catch (Exception e) {

        }
        return null;
    }

    public RideDTO getName(RideDTO rideDTO, boolean driverObject)  {


        User user = null;
        User driver = null;
        if (rideDTO.getUserId() != null && rideDTO.getUserId() != null) {
            user = userService.getById(rideDTO.getUserId());
            //user = lride.getUser().get();
        }
        if (rideDTO.getDriverId() != null && rideDTO.getDriverId() != null) {
            // driver = lride.getDriver().get();
            driver = userService.getById(rideDTO.getDriverId());
        }

        //if (driver != null && driver.getOneSignalValue() != null)
        //    rideDTO.setOneSignalId(driver.getOneSignalValue());

        //driver object false..get only user details
        log.info(" Error ");
        if (!driverObject && user != null && getUserName(user) != null) {
            String name = getUserName(user);
            if (name != null && !"".equals(name))
                rideDTO.setUserName(name);
            else
                rideDTO.setUserName("NONAME");
        }

        //user object and getting Taxi tpye
        if ((!driverObject && driver != null && driver.getId() != null)) {

            String name = getDriverName(driver);
            if (!"".equals(name)) {
                rideDTO.setDriverName(name);
            } else {
                rideDTO.setDriverName("NONAME");
            }
        }
        return rideDTO;
    }

    public String getUserName(User user)  {
        String name = null;
        if (user == null)
            return null;
        else if (user != null)
            return name = (user.getFirstName() != null) ? user.getFirstName() : user.getLastName();
        return null;
    }

    public String getDriverName(User driverName)  {
        String name = null;
        if (driverName == null)
            return null;
        else if (driverName != null && driverName.getFirstName() != null)
            return driverName.getFirstName();
        else if (driverName != null && driverName.getLastName() != null)
            return driverName.getLastName();
        return "NO NAME";

    }

    public RideDTO getRideId(String rideId) {
        try {
            Optional<Ride> rides = rideRepository.findById(rideId);
            Ride ride = null;

            if(rides.isPresent())
                 ride = rides.get();

            RideDTO dto = Converter.entityToDTO(ride, null);
            if (ride != null)
                return dto;
        } catch (DatabaseException e) {

        }
        return new RideDTO();
    }

    public RideDetailDTO sendEmail(String rideId) throws DatabaseException, IOException {


        Optional<Ride> rides = rideRepository.findById(rideId);
        Ride ride = null;

        if(rides.isPresent())
            ride = rides.get();

        User userId = null;
        if(ride !=null && ride.getUserId() != null)
             userId = getUserorDriver(ride.getUserId());
        RideDTO dto = Converter.entityToDTO(ride, null);


        RideDetailDTO detailDTO = new RideDetailDTO(dto);

        if(userId != null && userId.getEmail() == null)
            detailDTO.setUserEmailId(userId.getEmail());
        else
            detailDTO.setUserEmailId("taxideals.ch@gmail.com");

        if(userId != null && userId.getFirstName() == null)
            detailDTO.setUserName(userId.getFirstName());
        if(ride != null) {
            detailDTO.setSource(ride.getSource());
            detailDTO.setDestination(ride.getDestination());
            detailDTO.setUserTotalPrice(ride.getUserTotalPrice());
        }

        //selectAndSendEmail
        //String FirstName = detail.
        emailModel.selectAndSendEmail(detailDTO);

        return detailDTO;
    }

    public StatusModelResponse createComment(ShortRides rideDTO, String name)  {

        StatusModelResponse response = new StatusModelResponse();

        double basePrice = 3.0;
        double price = 2.0;
        String supplierId = null;
        String carType = "Taxi";
        Taxi taxi =null;
        User user = null;
        User driver = null;

        StatusResponse sr = new StatusResponse();
        TaxiDetail taxiDetail = null;

        if (rideDTO.getUserId() != null && rideDTO.getDriverId() != null) {
            user = userModel.getById(rideDTO.getUserId());
            driver = userModel.getById(rideDTO.getDriverId());
        }

        if (user == null) {
            response.setStatus(ModelStatus.USER_IS_NOT_EXISTING);
            return response;
        }
        if (driver == null) {
            response.setStatus(ModelStatus.USER_IS_NOT_EXISTING);
            return response;
        }


        if (rideDTO.getDriverId() != null ) {
            if(driver != null && driver.getId() != null ) {
                taxiDetail = taxiDetailService.findDriverId(rideDTO.getDriverId());
                //rideDTO = getDriverBasePrice(driver, rideDTO);
                //taxiDetail = taxiModel.findTaxiDetailId(driver.getId());
            }
            if(taxiDetail != null && taxiDetail.getId() != null ){
                String taxiId = taxiDetail.getTaxiId();
                taxi     = taxiModel.getTaxiIds(taxiId);
                if(taxi.getCarType() != null)
                    carType = taxi.getCarType();
                if(taxiDetail.getSupplierId() != null)
                    supplierId = taxiDetail.getSupplierId();
            }
        }

        Ride ride = Converter.createRide(rideDTO);
        ride.setSupplierId(supplierId);
        ride.setType(carType);
        ride.setDesc(rideDTO.getDes());

        if(rideDTO.getPayment() != null)
            ride.setPayment(rideDTO.getPayment().name());
        else
            ride.setPayment(Payment.CASH.name());

        String status = rideDTO.getStatus().name().toUpperCase();

        if (status.equalsIgnoreCase(RIDESTATUS.CANCEL_BOOKING.name())) {
            ride.setBasePrice(3.0f);
            ride.setTotalPrice(5.0f);
            tractUser(STEPS.LOGIN, user);
            tractUser(STEPS.LOGIN, driver);
        }
        else if (rideDTO.getStatus().name().equalsIgnoreCase(RIDESTATUS.ACCEPTED.name())) {
            if (user.getId() != null) {
                tractUser(STEPS.BOOKING_CONFIRMED, user);
            }
            if (driver.getId() != null) {
                tractUser(STEPS.BOOKING_CONFIRMED, driver);
                tractUser(STEPS.BOOKING_CONFIRMED, user);
            }
        }
        else if (rideDTO.getStatus().name().startsWith("PRECANCEL")) {

            if(rideDTO.getStatus().name().equalsIgnoreCase(RIDESTATUS.PRECANCEL_BY_USER.name())){
                ride.setStatus(RIDESTATUS.PRECANCEL_BY_USER);
                ride.setTotalPrice((cancelFee));
            }
            else if(rideDTO.getStatus().name().equalsIgnoreCase(RIDESTATUS.PRECANCEL_BY_DRIVER.name())){
                ride.setStatus(RIDESTATUS.PRECANCEL_BY_DRIVER);
                ride.setTotalPrice((cancelFee));
            }
            if (user.getId() != null) {
                tractUser(STEPS.PRECANCEL_BY_USER, user);
                ride.setTotalPrice((cancelFee));
            }
            if (driver.getId() != null) {
                tractUser(STEPS.PRECANCEL_BY_USER, driver);
                tractUser(STEPS.PRECANCEL_BY_USER, user);
            }
            ride.setEndDate(new Date());
        }
        if (name.equalsIgnoreCase("USER")) {
            ride.setCreatedBy(rideDTO.getUserId());
        } else {
            ride.setCreatedBy(rideDTO.getDriverId());
        }

        if(rideDTO.getCategory() != null){
            ride.setCategory(getCategoryType(rideDTO.getCategory()).name());
        }
        else {
            ride.setCategory("Taxi");
        }

        ride.setStartDate(new Date());
        ride.setEndDate(new Date());
        String id =   rideRepository.save(ride).getId();
        System.out.println( "SAVED ID -->" + ride.getId());

        if ( id != null) {
            //rideDTO.setId(rideId);
            setGlobalRideId( ride.getId());
            response.setStatus(ModelStatus.CREATED);
            response.setId(id);
            return response;
        }
        response.setStatus(ModelStatus.EXCEPTION);
        return response;

    }
    public StatusModelResponse create(ShortRide rideDTO, String name)  {

        StatusModelResponse response = new StatusModelResponse();

        double basePrice = 3.0;
        double price = 2.0;
        String supplierId = null;
        String supplierIds = null;
        String carType = "Taxi";
        Taxi taxi =null;
        StatusResponse sr = new StatusResponse();
        //validation
        //user
        //driver
        //
        User user = null;
        User driver = null;
        TaxiDetail taxiDetail = null;
        if (rideDTO.getUserId() != null && rideDTO.getDriverId() != null) {
            user = userModel.getById(rideDTO.getUserId());
            driver = userModel.getById(rideDTO.getDriverId());
        }

        if (user == null) {
             response.setStatus(ModelStatus.USER_IS_NOT_EXISTING);
             return response;
        }
        if (driver == null) {

            response.setStatus(ModelStatus.USER_IS_NOT_EXISTING);
            return response;
        }


        if (rideDTO.getDriverId() != null ) {

            if(driver != null && driver.getId() != null ) {
                taxiDetail = taxiDetailService.findDriverId(rideDTO.getDriverId());
                //rideDTO = getDriverBasePrice(driver, rideDTO);
                //taxiDetail = taxiModel.findTaxiDetailId(driver.getId());
            }
            if(taxiDetail != null && taxiDetail.getId() != null ){
                String taxiId = taxiDetail.getTaxiId();
                taxi     = taxiModel.getTaxiIds(taxiId);
                if(taxi.getCarType() != null)
                    carType = taxi.getCarType();
                if(taxiDetail.getSupplierId() != null)
                    supplierId = taxiDetail.getSupplierId();
            }
        }

        Ride ride = Converter.createRide(rideDTO);
        ride.setSupplierId(supplierId);
        ride.setType(carType);



        if(rideDTO.getPayment() != null)
            ride.setPayment(rideDTO.getPayment().name());
        else
            ride.setPayment(Payment.CASH.name());

        String status = rideDTO.getStatus().name().toUpperCase();
        if (status.equalsIgnoreCase(RIDESTATUS.CANCEL_BOOKING.name())) {
            ride.setBasePrice(3.0f);
            ride.setTotalPrice(5.0f);
            tractUser(STEPS.LOGIN, user);
            tractUser(STEPS.LOGIN, driver);
        }
        else if (rideDTO.getStatus().name().equalsIgnoreCase(RIDESTATUS.ACCEPTED.name())) {
            if (user.getId() != null) {
                tractUser(STEPS.BOOKING_CONFIRMED, user);
            }
            if (driver.getId() != null) {
                tractUser(STEPS.BOOKING_CONFIRMED, driver);
                tractUser(STEPS.BOOKING_CONFIRMED, user);
            }
        }
        else if (rideDTO.getStatus().name().startsWith("PRECANCEL")) {

            if(rideDTO.getStatus().name().equalsIgnoreCase(RIDESTATUS.PRECANCEL_BY_USER.name())){
                ride.setStatus(RIDESTATUS.PRECANCEL_BY_USER);
                ride.setTotalPrice((cancelFee));
            }
            else if(rideDTO.getStatus().name().equalsIgnoreCase(RIDESTATUS.PRECANCEL_BY_DRIVER.name())){
                ride.setStatus(RIDESTATUS.PRECANCEL_BY_DRIVER);
                ride.setTotalPrice((cancelFee));
            }
            if (user.getId() != null) {
                tractUser(STEPS.PRECANCEL_BY_USER, user);
                ride.setTotalPrice((cancelFee));
            }
            if (driver.getId() != null) {
                tractUser(STEPS.PRECANCEL_BY_USER, driver);
                tractUser(STEPS.PRECANCEL_BY_USER, user);
            }
            ride.setEndDate(new Date());
        }
        if (name.equalsIgnoreCase("USER")) {
            ride.setCreatedBy(rideDTO.getUserId());
        } else {
            ride.setCreatedBy(rideDTO.getDriverId());
        }


        if(rideDTO.getCategory() != null){
            ride.setCategory(getCategoryType(rideDTO.getCategory()).name());
        }
        else {
            ride.setCategory("Taxi");
        }

        ride.setStartDate(new Date());
        ride.setEndDate(new Date());
        String id =   rideRepository.save(ride).getId();
        System.out.println( "SAVED ID -->" + ride.getId());

        if ( id != null) {
            //rideDTO.setId(rideId);
            setGlobalRideId( ride.getId());
            response.setStatus(ModelStatus.CREATED);
            response.setId(id);
            return response;
        }
        response.setStatus(ModelStatus.EXCEPTION);
        return response;

        //taxiModel.addGeoLocationWithStatus(taxi.getId(),rideDTO.getLatitude(),rideDTO.getLongitude(),DRIVERSTATUS.START.name());
        //userService.se
    }
    public String updateCalculation(String category,String type,String region,float distance,String domain,String supplierId){

        String payment = "CASH";
        float discount= 0.0f;
        float elapsedTime = 0;
        float travelTime = 0;
        PriceMap priceMap = updateCalculation(category, type, region,0, distance, 0,
         payment, discount, domain, elapsedTime, travelTime, supplierId,null);
        if(priceMap.getUserTotalPrice() > 0)
          return String.valueOf(priceMap.getUserTotalPrice());
        else return "0";
    }
    public PriceMap updateCalculation(String category,String type,String region,float baseFare,float distance,float totalWaitTimes,
                                      String payment,float discount,String domain,float elapsedTime,float travelTime,String supplierId,String coupan){

        float totalPrice = 0.0f;
        float userTotalPrice = 0.0f;

        //float totalTravelTimes = 0f;

        float price;
        float waitTime;
        float miniMunFare;
        float minimumFare;
        float percentages = 0;
        float totalWaitTimePrice = 0;
        float distancelessthanfourKm =0;
        float totalTravelTimePrice = 0;
        float originalDistance =0;

        originalDistance = distance;

        log.info("originalDistance -->" + originalDistance + " distance"+distance);
        String whichCountry = "ch";
        PriceMap priceMapInput = new PriceMap();

        if(baseFare == 0 || baseFare < 0 || baseFare > 0) {

            if(domain == null || "".equalsIgnoreCase(domain))
                domain = Path.DOMAIN.MYDOMAIN;

            log.info("New Price is adding in DB -->" + region);
            //cal base price
            Price value = priceModeL.getPriceDomainRegions(category,type,region,domain);


            if(value == null){
                if(country != null ) {
                    value = priceModeL.createDummyPrice(type, region,country,domain,supplierId);
                }
                else {
                    value = priceModeL.createDummyPrice(type, region,country,domain,supplierId);
                }
                log.info("New Price is adding in DB -->" + type  + "region -->" + region +  " domain  -->" + domain);
            }
            else {
                log.info("Price checking -->" + value  + "region -->" + region + " domain" + value.getDomain());
            }

            //validate waitingTime
            totalWaitTimes =  validate_WaitingTime(totalWaitTimes);
            distance       =  validate_distance(distance);

            //call Map
            priceMapInput =  getPriceFromDB(value,country);

            //now take base Fare from Price Table not from TaixDetail table
            baseFare      = priceMapInput.getBasePrice();
        }

        //REDUCING BASE KM
        //NO STATIC VALUE
        log.info("\n"+ "Base km ...." + baseFare + "-----Before reduction-- Base distance  ------ >" + distance + " travelTime " + travelTime);
        //distance more than 4km dp subrraction
        //special calculation for base km and km for base fare price (free)
        if(baseFare > 0){
            BaseDistance db =  distanceCalculation.getBasePrice(distance,priceMapInput.getBaseKm(),priceMapInput.getBasePrice(),
                    priceMapInput.getBaseAddPrice());

            distance = db.getDistance();
            baseFare = db.getBase();
            log.info("\n"+"-----baseFare " + db.toString());
        } //else take real km like 0.44 to 333 as example
        else {
            distancelessthanfourKm = distance;
            //keep orignial
            //distance =0;
            log.info("\n"+"-----distancelessthanfourKm " + distance);
        }


        log.info("\n"+"-----AFter reduction-- Base distance  ------ >" + distance + " travelTime " + travelTime +" DB travel time" + priceMapInput.getTravelTime() );
        float travelTimePrice = priceMapInput.getTravelTime();
        //waitTime = priceMapInput.getWaitTime();
        minimumFare = priceMapInput.getMinimumFare();
        // price = priceMapInput.getPrice();



        PriceTime  priceTime = distanceCalculation.getKmandPriceCalculation(distance,priceMapInput);
        waitTime = priceTime.getWaitingTime();
        price = priceTime.getPrice();

        //totalWaitTimes
        if( waitTime > 0 ) {
            totalWaitTimePrice = waitTime * totalWaitTimes;
        }
        log.info("\n"+"-----WaitTime  -----------" + waitTime
                + "\n"+"----totalWaitTimes ----" +  totalWaitTimes +
                "\n" +"-----TotalWaitingPrice ----" + totalWaitTimePrice );



        log.info("value price-->" + price);


        totalPrice = distance * price + baseFare + totalWaitTimePrice ;

        log.info("\n"+"-- BaseFare----"+ + baseFare +  "\n"
                +"--  Price      -->" + price +   "\n"
                +"--- Distance   -->" +  distance  + "\n"
                +"--- TotalPrice -->"  + totalPrice + "\n"
                +"--- travelTimePrice   -->" +  travelTimePrice  + "\n"
                +"--- travelTime   -->" +  travelTime  + "\n");

        float travelTimeaftersubraction = 0;

        if(travelTime > 0 || travelTimePrice > 0){

            //subtrace base fare time also ---
            BaseDistance db = distanceCalculation.getTimeCalulation(priceMapInput.getBaseTime(),travelTime,
                    priceMapInput.getBasePrice(),priceMapInput.getBaseAddPrice());
            travelTimeaftersubraction = db.getTotalTravelTime();
            float baseFareTmp = db.getBase();

            log.info(" ..travelTimeaftersubraction..-->" + travelTimeaftersubraction + " baseFare -->" + baseFare);

            //Switch of   baseFareTmp
            if(travelTimeaftersubraction > 0 && priceMapInput.getTravelTime() > 0) {
                //totalTravelTimePrice = +baseFareTmp + travelTimeaftersubraction * priceMapInput.getTravelTime();
                totalTravelTimePrice =  travelTimeaftersubraction * priceMapInput.getTravelTime();
            }
            else  {
                //totalTravelTimePrice = +baseFare + travelTime * travelTimePrice;
                totalTravelTimePrice =  travelTime * travelTimePrice;
            }

            log.info(" ..travelTimeaftersubraction.." + totalTravelTimePrice);
        }

        log.info("\n"+"--  BaseFare----"+ + baseFare +  "\n"
                + "TriptravelTIme  " + travelTime  + "\n"
                + "travelTimeaftersubraction " + travelTimeaftersubraction + "\n"
                +" --- totalTravelTimePrice --"  + totalTravelTimePrice );

        //traveeling time
        PriceMap  newMap = new PriceMap();
        newMap.setRealKm(distancelessthanfourKm);
        newMap.setPercentage(priceMapInput.getPercentage());
        newMap.setBasePrice(priceMapInput.getBasePrice());
        newMap.setPrice(price * distance);
        newMap.setEstimateTime(elapsedTime);

        userTotalPrice = totalPrice;

        // Take Percentage IF CARD OR CASH ////////////////////////////////////////////////////////////////////////
        if(payment != null && payment.equalsIgnoreCase(Payment.CARD.name()))
        {
            //newMap.setMinimumFare(0);
            if(newMap.getPercentage() > 0.0) {
                percentages = (totalPrice * newMap.getPercentage()/100);
                totalPrice = totalPrice - percentages;
            }
            newMap.setTotalPrice(totalPrice);
            newMap.setUserTotalPrice(totalPrice);
        }

        //calculate percentage
        if(newMap.getPercentage() > 0)
        {
            percentages = (userTotalPrice * newMap.getPercentage()/100);
            newMap.setPercentage(percentages);

            System.out.println("Percentage -->" + percentages);
            totalPrice = totalPrice - percentages;
            newMap.setTotalPrice(totalPrice);
        }

        //DISCOUNT/////////////////////////////////////////////////////////////////////////
        if(discount > 0 ){
            float discounts = (userTotalPrice * newMap.getDiscount())/100;

            System.out.println("discounts -->" + discounts);
            totalPrice = totalPrice - discounts;

            newMap.setDiscount(discounts);
            newMap.setTotalPrice(totalPrice);
        }

        //COUPAN/////////////////////////////////////////////////////////////////////////
        if(coupan != null && coupan.length() > 0){

            Coupon coupans = coupanRepository.findByCoupan(coupan);

            System.out.println("coupans -->" + coupans);

            if(coupans != null){

                float coupanDiscount= coupans.getPercentage();

                System.out.println("coupans Percentage -->" + coupanDiscount);

                float discountss = (userTotalPrice * coupanDiscount)/100;

                System.out.println("Before discount -->" + totalPrice);

                totalPrice = totalPrice - discountss;

                System.out.println("After discount -->" + totalPrice);
                newMap.setDiscount(discountss);
                newMap.setTotalPrice(totalPrice);
            }

        }

        //TAKE OUR MONEY THEN PUT TAX FOR TAXI DRIVERS WHILE GIVING
        //Tax//////////////////////////////////////////////////////////////////////////////
        if(priceMapInput.getTax() > 0){
            float tax = userTotalPrice * priceMapInput.getTax()/100;
            totalPrice = totalPrice - tax;

            System.out.println("tax -->" + tax);
            newMap.setTax(tax);
            newMap.setTotalPrice(totalPrice);
        }

        //totalTravelTimePrice -
        newMap.setTravelTime(travelTime);
        newMap.setKm(distance);
        newMap.setBaseKm(priceMapInput.getBaseKm());
        newMap.setTotalWaitTime(totalWaitTimes);
        newMap.setTotalWaitTimePrice(totalWaitTimePrice);
        newMap.setUserTotalPrice(userTotalPrice);
        newMap.setMinimumFare(minimumFare);
        newMap.setTotalTravelTime(travelTime);
        newMap.setUserTravelPrice(totalTravelTimePrice);
        newMap.setOriginalDistance(originalDistance);

        //if discount coupans:
        //finally add
        log.info("\n"+"MAP price ----"+ newMap.getPrice() );
        log.info("\n"+"Map Total Price ----"+ newMap.getTotalPrice() );
        return newMap;
    }

    public JSONObject json = null;


    public JSONObject getJsonValue(){
        return json;
    }

    public void setJsonValue(Ride ride,PriceMap map){

        //System.out.println(" JSON " + ride.getId());

        JSONObject json = new JSONObject();
        if(ride != null) {
            json.put("Km", ride.getKm());
            json.put("Total Price", ride.getTotalPrice());
            json.put("User Total Price", ride.getUserTotalPrice());
            json.put("Base Price", ride.getBasePrice());
            json.put("Price ", ride.getPrice());
            //json.put("Tax ",ride.getTax());
            //getUserTravelPrice
            json.put("Total Time", ride.getEstimateTime());
        }
        else if(map != null) {
            json.put("Map Km..", map.getKm());
            json.put("Total Price", map.getUserTotalPrice());
            json.put("User Total Price", map.getUserTotalPrice());
            json.put("Base Price", map.getBasePrice());
            //json.put("Tax ",ride.getTax());
            json.put("Total Time", map.getEstimateTime());
        }
    }

    public void setRideId(String rideId) {
        this.globalRideId = rideId;
    }

    public CATEGORY getCategoryType(String type) {
        if (!"".equalsIgnoreCase(type)  && !type.equalsIgnoreCase("String")) {
            return CATEGORY.valueOf(type.toUpperCase());
        }
        return CATEGORY.TAXI;
    }

    private void tractUser(String status, User user){
        //userModel.loginStatus(status,user);
    }
    public float validate_WaitingTime(float waitingTime){

        //more than 3 hrsours (60 * 3)
        if(waitingTime > 150)
            return 99;
        else
            return waitingTime;
    }

    public float validate_distance(float distance){
        //more than 3 hrsours (60 * 3)
        if(distance > 700)
            return 500;
        else
            return distance;
    }

    public boolean findPeakTime(int timeOfDay){
        if(timeOfDay >= 0 && timeOfDay < 4){
            log.info("VERY Good Morning");
            return true;
        }
        if(timeOfDay >= 4 && timeOfDay < 8){
            log.info("Good Morning");
            return false;
        }
        else if(timeOfDay >= 12 && timeOfDay < 16){
            log.info("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            log.info("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            log.info("Good Night");
            return true;
        }
        return false;
    }

    private PriceMap getPriceFromDB(Price value,String country){
        int hours;
        float baseFare;
        float price;
        float waitingTime;
        float miniMunFare;
        float percentage = 0f;
        float discountCoupans;
        float baseAddPrice;
        PriceMap priceMap = new PriceMap();
        float baseTime;
        //default value
        baseFare = 0.0f;
        waitingTime = 0.0f;
        miniMunFare = 0.0f;

        if (value != null){

            baseFare = value.getBasePrice();
            baseAddPrice = value.getBaseAddPrice();
            baseTime = value.getBaseTime();
            String hr = DateUtil.getTimeZone(null,country);

            hours = Integer.valueOf(hr).intValue();
            if(value.getPercentage() > 0)
                percentage = value.getPercentage();

            if(findPeakTime(hours)) {
                price = value.getPeakPrice();

                //sometime peake is not set
                if(price <= 0 ) {
                    price = value.getPrice();
                    log.info(" -PEAK PRICE IS NOT SET -->" + price);
                }
            }
            else {
                price = value.getPrice();
            }
            log.info(" -NORMAL PRICE OR PEAK PRICE -->" + price);

            if(value.getTax() > 0){
                priceMap.setTax(value.getTax());
            }
            //BASE KMMMM
            if(value.getKm() > 0)
                priceMap.setBaseKm(value.getKm());
            if(value.getKmTwo() > 0) {
                priceMap.setKmTwo(value.getKmTwo());
                priceMap.setPriceTwo(value.getPriceTwo());
                priceMap.setWaitTimeTwo(value.getWaitingTimeTwo());
            }

            if(value.getKmThree() > 0) {
                priceMap.setKmThree(value.getKmThree());
                priceMap.setPriceThree(value.getPriceThree());
                priceMap.setWaitTimeThree(value.getWaitingTimeThree());
            }
            if(value.getKmFour() > 0) {
                priceMap.setKmFour(value.getKmFour());
                priceMap.setPriceFour(value.getPriceFour());
                priceMap.setWaitingTimeFour(value.getWaitingTimeFour());
            }
            if(value.getKmFive() > 0) {
                priceMap.setKmFive(value.getKmFive());
                priceMap.setPriceFive(value.getPriceFive());
                priceMap.setWaitingTimeFive(value.getWaitingTimeFive());
            }
            if(value.getKmSix() > 0) {
                priceMap.setKmSix(value.getKmSix());
                priceMap.setPriceSix(value.getPriceSix());
                priceMap.setWaitingTimeSix(value.getWaitingTimeSix());
            }


            waitingTime = value.getWaitingTime();
            miniMunFare = value.getMinimumPrice();

            priceMap.setWaitTime(waitingTime);
            priceMap.setMinimumFare(miniMunFare);
            priceMap.setPrice(price);
            priceMap.setBasePrice(baseFare);
            priceMap.setBaseTime(baseTime);
            priceMap.setBaseAddPrice(baseAddPrice);
            priceMap.setPercentage(percentage);


            log.info("Travel Time -->" + value.getTravelTime());

            if(value.getTravelTime() >= 0){
                priceMap.setTravelTime(value.getTravelTime());
            }
            if(value.getCarType() != null)
                priceMap.setType(value.getCarType());

        }
        log.info(" PriceMap -->" + priceMap.toString());

        return priceMap;
    }

    @Autowired
    SupplierModel supplierModel;

    @Autowired
    SupplierRepository supplierRepository;
    public List<RideDTO> getAllDriversBySupplier(String id, Date today)  {
        //User user = userService.getById(User.class, id);
        Optional<Supplier> opsupplier =  supplierRepository.findById(id);
        List<Ride> lride = null;
        List<RideDTO> rideDTO = new ArrayList<>(0);
        Long userId = null;
        Supplier supplier = null;

        if(opsupplier.isPresent())
            supplier = opsupplier.get();

        if (supplier != null || supplier == null) {

            /*if (today != null) {
                lride = rideService.listByUsersByDate(id, new Date());
            } */
             {
                lride = rideRepository.findBySupplierId(id);
            }

            rideDTO = new ArrayList<>(0);

            for (Ride ride : lride) {
                RideDTO dto = Converter.entityToDTO(ride, null);
                Long taxiDetailId = null;

                /*if (getUserorDriver(ride.getDriverId()) != null) {
                    User driver = getUserorDriver(ride.getDriverId());
                    dto.setDriverId(driver.getId());
                    if (driver.getImageInfo() != null) {
                        dto.setImageInfo(driver.getImageInfo());
                    }
                }*/

                dto = getRatingComments(ride.getId(), dto, "USER");
                //log.info(" ---PART 1 -AFTER double getPrice --" + dto.getPrice() + " ..getTotalPrice..." + dto.getTotalPrice() + " userTotalPrice" + dto.getUserTotalPrice());
                dto = getName(ride, dto, true);
                rideDTO.add(dto);
            }
            return rideDTO;
        } else
            return rideDTO;
    }

    public RideDTO getRatingComments(String rideId, RideDTO rideDTO, String postedBy) {

        //Review
        Review review = null;
        if (rideId != null && !"".equalsIgnoreCase(postedBy)) {

            log.info(" --Ride Id 1---" + rideId  + ".."+ "postedBy" + postedBy);

            List<Review> reviews =   reviewModel.getReviewByRideByPost(rideId);
            review = getReview(reviews,postedBy);

            //log.info(" --review" + review);
            //check condition if postedBYUser ???
            // if(review != null && review.getRideId() > 0 && review.getPostedBy().equalsIgnoreCase(postedBy)) {
            if (review != null && review.getRideId() != null && review.getRideId() != null) {

                //log.info(" --review Id---" + review.getId());
                //reviewDTO = reviewModel.getByTaxiDetailIds(taxiDetailId, userId);

                if (review.getRating() != null && review.getRating() > 0) {
                    rideDTO.setStar(review.getRating().intValue());
                    //log.info(" --getRating---" + review.getRating());
                }

                if (review.getComment() != null && !review.getComment().isEmpty()) {
                    rideDTO.setComments(review.getComment());
                    //log.info(" --getComment---" + review.getComment());
                }
            }
            return rideDTO;
        }
        return rideDTO;
    }

    public Review getReview(List<Review> reviews,String postedBy){
        Optional<Review> value = reviews
                .stream()
                .filter(a -> a.getPostedBy().equalsIgnoreCase(postedBy))
                .findFirst();
        if(value != null && value.isPresent())
            return value.get();
        return new Review();
    }

    public RideDTO getName(Object lride, RideDTO rideDTO, boolean driverObject)  {


        User user = null;
        User driver = null;
        if (rideDTO.getUserId() != null && rideDTO.getUserId() != null ) {
            Optional<User> optuser = userRepository.findById(rideDTO.getUserId());
            //user = lride.getUser().get();
            if(optuser.isPresent())
                user =  optuser.get();
        }
        if (rideDTO.getDriverId() != null && rideDTO.getDriverId() != null) {
            // driver = lride.getDriver().get();

            Optional<User> optuser = userRepository.findById(rideDTO.getDriverId());
            if(optuser.isPresent())
                driver =  optuser.get();

        }

        //CK
        //if (driver != null && driver.getOneSignalValue() != null)
         //   rideDTO.setOneSignalId(driver.getOneSignalValue());

        //driver object false..get only user details
        log.info(" Error ");
        if (!driverObject && user != null && getUserName(user) != null) {
            String name = getUserName(user);
            if (name != null && !"".equals(name))
                rideDTO.setUserName(name);
            else
                rideDTO.setUserName("NO DATA");
        }

        //user object and getting Taxi tpye
        if ((driverObject && driver != null && driver.getId() != null)) {

            //get Driver Details -Taxi Type
            //driver will change Taxi Type
            /*TaxiDetail taxiDetail = findTaxiDetailId(driver.getId());
            if(taxiDetail != null && taxiDetail.getId() > 0) {
                Taxi taxi = taxiModel.getTaxi(taxiDetail.getTaxiId());
                if(taxi != null && taxi.getCarType() != null){
                    rideDTO.setType(taxi.getCarType());
                }
            }*/

            String name = getDriverName(driver);
            if (!"".equals(name)) {
                rideDTO.setDriverName(name);
            } else {
                rideDTO.setDriverName("NO DATA");

            }
        }
        return rideDTO;
    }

    public List<TripCounts>  getRideCount(String id)  {

        List<TripCounts> countList = new ArrayList<>();
        User user = userService.getById(id);
        List<RideDTO> rideDTOList = new ArrayList<>();
        Long taxiDetailId = null;
        RideDTO rideDto = null;
        List<Ride> rides = null;
        // Page<Ride> page = null;

        HashMap<String ,HashMap<String,TripCount>> parent =new HashMap<>();
        HashMap<String,TripCount> totalvalue = new HashMap<String,TripCount>();
        if (user != null) {

            if (user != null && user.getRole().equalsIgnoreCase(Path.ROLE.DRIVER)) {
                rides = rideRepository.findByDriverIdOrderByStartDateDesc(id);
            }
            if (user != null && user.getRole().equalsIgnoreCase(Path.ROLE.USER)) {
                rides = rideRepository.findByUserIdOrderByStartDateDesc(id);
            }
            if (rides != null && rides.size() >0) {

                //HashMap<String,String> dateKey = new HashMap<String,String>();
                for(Ride ride : rides) {

                    if ( ride != null) {
                        String date = DateUtil.stringToDate(ride.getEndDate());
                        RideDTO rideDTO = Converter.entityToDTO(ride, null);

                        if (ride.getDriverId() != null) {
                            rideDTO.setDriverName(getUserorDriverName(ride.getDriverId()));
                        }
                        if (ride.getUserId() != null) {
                            User users = getUserorDriver(ride.getUserId());
                            rideDTO.setUserName(getUserorDriverName(ride.getDriverId()));
                        }
                        rideDTOList.add(rideDTO);

                        //dateKey.put(date,date);
                        if(totalvalue.containsKey(date)){
                            TripCount tripCount = totalvalue.get(date);
                            int count = tripCount.getCount() + 1;
                            float amount =   tripCount.getAmount() + rideDTO.getUserTotalPrice();

                            tripCount.setCount(count);
                            tripCount.setAmount(amount);
                            tripCount.setEndDate(ride.getEndDate());
                            totalvalue.put(date,tripCount);
                        }
                        else {
                            //totalvalue.put(date,)
                            TripCount tripCount = new TripCount();
                            tripCount.setCount(1);
                            tripCount.setAmount(rideDTO.getUserTotalPrice());
                            tripCount.setEndDate(ride.getEndDate());
                            totalvalue.put(date,tripCount);
                        }
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();
                        parent.put(uuidAsString,totalvalue);
                    }
                }
                return conversion(totalvalue);
                //return parent;
            }
            //totalvalue

            return countList;
        } else
            return countList;
    }
    public List<TripCounts> conversion(HashMap<String,TripCount> totalvalue){

        List<TripCounts> countList = new ArrayList<>();
        for (Map.Entry mapElement : totalvalue.entrySet()) {

            String key = (String)mapElement.getKey();

            // Add some bonus marks
            // to all the students and print it

            TripCount countVlaue =  (TripCount)mapElement.getValue();
            TripCounts tripCountss = new TripCounts(countVlaue,key);
            System.out.println(key + " : " + mapElement.getValue());
            countList.add(tripCountss);

        }
        return countList;
    }


    public HashMap<String,TripCount> getAllDriversBySupplierCount(String id, Date today)  {
        //User user = userService.getById(User.class, id);
        Optional<Supplier> opsupplier =  supplierRepository.findById(id);
        List<Ride> lride = null;
        List<RideDTO> rideDTO = new ArrayList<>(0);
        Long userId = null;
        Supplier supplier = null;

        HashMap<String,TripCount> totalvalue = new HashMap<String,TripCount>();
        if(opsupplier.isPresent())
            supplier = opsupplier.get();

        if (supplier != null || supplier == null) {

            /*if (today != null) {
                lride = rideService.listByUsersByDate(id, new Date());
            } */
            {
                lride = rideRepository.findBySupplierId(id);
            }

            rideDTO = new ArrayList<>(0);

            for (Ride ride : lride) {
                RideDTO dto = Converter.entityToDTO(ride, null);

                //log.info(" ---PART 1 -AFTER double getPrice --" + dto.getPrice() + " ..getTotalPrice..." + dto.getTotalPrice() + " userTotalPrice" + dto.getUserTotalPrice());
                dto = getName(ride, dto, true);

                if(totalvalue.containsKey(ride.getDriverId())){

                    System.out.println(" already " + ride.getDriverId());
                    int count = 0;
                    TripCount tripCount = totalvalue.get(ride.getDriverId());
                    if(tripCount != null) {
                        if (tripCount.getCount() > 0)
                            count = tripCount.getCount() + 1;

                        float amount = tripCount.getAmount() + ride.getUserTotalPrice();
                        tripCount.setCount(count);
                        tripCount.setAmount(amount);
                        tripCount.setName(dto.getDriverName());
                        tripCount.setEndDate(ride.getEndDate());
                        totalvalue.put(ride.getDriverId(), tripCount);
                    }
                }
                else {
                    System.out.println(" new " + ride.getId());
                    TripCount tripCount = new TripCount();
                    tripCount.setCount(1);
                    tripCount.setAmount(ride.getUserTotalPrice());
                    tripCount.setName(dto.getDriverName());
                    tripCount.setStartDate(ride.getStartDate());
                    totalvalue.put(ride.getDriverId(),tripCount);
                }
            }
            return totalvalue;
        } else
            return totalvalue;
    }




}
