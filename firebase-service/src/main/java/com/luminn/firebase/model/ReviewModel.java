package com.luminn.firebase.model;

import com.luminn.firebase.dto.ReviewDTO;
import com.luminn.firebase.entity.Review;
import com.luminn.firebase.entity.Ride;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.ReviewRepository;
import com.luminn.firebase.repository.RideRepository;
import com.luminn.firebase.service.ReviewService;
import com.luminn.firebase.service.RideService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.STEPS;
import com.luminn.view.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ReviewModel {

    public String reviewId;
    private static final Logger log = LoggerFactory.getLogger(ReviewModel.class);

    @Autowired
    public UserMongoService userservice;

    @Autowired
    RideService rideService;

    @Autowired
    RideModel rideModel;

    @Autowired
    RideRepository rideRepisitoy;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TaxiDetailModel taxiDetailModel;




    public ModelStatus createReview(ReviewDTO reviewDTO) {

        StatusResponse sr = new StatusResponse();
        User user = null;
        User driver = null;
        Ride ride = null;
        log.info("Review DTO user.." + reviewDTO.getUserId() + "rideid--" + reviewDTO.getRideId() + "..post by" +reviewDTO.getPostedBy() );
        Review review = Converter.dtoToentity(reviewDTO,null);

        user = userCheck(review.getUserId());
        driver = userCheck(review.getDriverId());

        if(user == null && driver == null)
            return ModelStatus.USER_IS_NOT_EXISTING;

        if(review.getUserId() == null && review.getUserId() == null) {
            user = validateCheck(review.getUserId());
            if(user == null) {
                return ModelStatus.USER_IS_NOT_EXISTING;
            }
        }

        //.3000 is expection for van, user can write comments
       // if(Validation.isEmpty(review.getRideId()) &&
        //        rideCheck(review.getRideId()))) ) {
        if(review.getRideId() != null){

            ride = rideCheck(review.getRideId());
            if(ride == null) {
                if(review.getRideId() != null)
                    return ModelStatus.RIDE_IS_NOT_EXISTING;
            }
        }
        log.info("review DTO review.." + review.getUserId() + "rideid--" + review.getRideId() + "..post by" +review.getPostedBy() );

        // -3000 expection van
        /*if(Validation.isEmpty(review.getDriverId()) && userCheck(review.getDriverId()) || (ride != null || review.getRideId() != null)) {
            driver = validateCheck(review.getDriverId());

            if(driver == null) {
                //return ModelStatus.DRIVER_IS_NOT_EXISTING;
                Ride rides = rideModel.getRide(review.getRideId());
                log.info("review DTO inside.." + rides.getDriverId());

                if(rides != null && rides.getDriverId() !=null && rides.getDriverId() != null )
                    review.setDriverId(rides.getDriverId());
                //CK
                //if(rides != null && rides.getTax() !=null && rides.getTaxiDetailId().longValue() > 0)
                // review.setTaxiDetailId(review.getTaxiDetailId());
            }
        }*/

        //RIDE_IS_NOT_EXISTING
        if(review.getTaxiDetailId() != null && !Validation.isEmpty(review.getTaxiDetailId()) && !driver.getRole().equalsIgnoreCase(Path.ROLE.DRIVER)) {
            TaxiDetail taxiDetail = taxiDetailModel.getId(review.getTaxiDetailId());
            if(taxiDetail == null)
                return ModelStatus.NO_TAXIDETAILS_ID;
            else
                review.setTaxiDetailId(taxiDetail.getTaxiId());
        }

        if( ( Validation.isEmpty(review.getTaxiDetailId()))) {


            if(user != null || driver != null) {

                if(review.getPostedBy() != null &&  review.getPostedBy().equalsIgnoreCase(Path.ROLE.DRIVER)){
                    review.setPostedBy(Path.ROLE.DRIVER);
                    tractUser(STEPS.RIDE_LOGIN,driver);
                }
                else if(review.getPostedBy() != null && review.getPostedBy().equalsIgnoreCase(Path.ROLE.USER)){
                    review.setPostedBy(Path.ROLE.USER);
                    tractUser(STEPS.RIDE_LOGIN,user);
                }
                //DEFAULT VALUE
                if("".equalsIgnoreCase(review.getPostedBy()) || review.getPostedBy() == null){
                    review.setPostedBy(Path.ROLE.USER);
                }
                String fullName = null;

                if(user.getFirstName() != null)
                     fullName = user.getFirstName();
                review.setUserFullName(fullName);

                reviewId  =  reviewRepository.save(review).getId();

                //log
                if(reviewId != null )
                    if((reviewDTO.getPostedBy() != null) && (reviewDTO.getPostedBy().equalsIgnoreCase("DRIVER")) )
                        tractUser(STEPS.LOGIN,driver);
                    else if((reviewDTO.getPostedBy() != null) && reviewDTO.getPostedBy().equalsIgnoreCase("USER") )
                        tractUser(STEPS.LOGIN,user);
                return ModelStatus.CREATED;
            }
            else {
                return ModelStatus.USER_IS_NOT_EXISTING;
            }
        }

        return ModelStatus.NOTEXISTS;

    }

    private void tractUser(String status, User user){
        //userModel.loginStatus(status,user);

    }

    public User userCheck(String userId)  {
        if(Validation.isEmpty(userId)) {
            User suer = userservice.getById(userId);
            return suer;

        }
        return null;
    }

    public User validateCheck(String userId)  {
        if(Validation.isEmpty(userId)) {
            User user = userservice.getById(userId);
            return user;
        }
        return null;
    }

    public Ride rideCheck(String rideId)  {
        if(Validation.isEmpty(rideId)) {
            //Long id = userservice.getById(User.class, userId).getId();
            Optional<Ride> ride = rideRepisitoy.findById(rideId);
            if(ride.isPresent())
                return ride.get();
            else
                return null;

        }
        return null;
    }

    public List<Review> getReviewByRideByPost(String reviewId){

        //return reviewSerivce.getReviewByRideByPost(reviewId);
      return  reviewRepository.findByPostedBy(reviewId);
    }

    public List<ReviewDTO> getByTaxiDetailIds(String taxiDetailId) {


        List<Review> reviews = reviewRepository.findByTaxiDetailId(taxiDetailId);
        System.out.println(" reviews after" + reviews);
        List<ReviewDTO> listReviewDTO = new ArrayList<ReviewDTO>();

        if(reviews != null) {
            System.out.println(" Reviews Size" + reviews.size());
            for (Review review : reviews) {

                ReviewDTO reviewDTO = Converter.entityToDto(review);

                User user = validateCheck(review.getUserId());
                if(user != null ){
                    if(user.getFirstName() != null){
                        reviewDTO.setUserFullName(user.getFirstName());
                    }
                    //if(user.getImageInfo() != null)
                     //   reviewDTO.setImageInfo(user.getImageInfo());
                }
                listReviewDTO.add(reviewDTO);
            }
            return listReviewDTO;
        }
        else
            return new ArrayList<ReviewDTO>();

    }

}
