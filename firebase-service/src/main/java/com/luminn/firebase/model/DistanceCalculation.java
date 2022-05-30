package com.luminn.firebase.model;


import com.luminn.firebase.util.BaseDistance;
import org.springframework.stereotype.Component;

@Component
public class DistanceCalculation {

    public BaseDistance getBasePrice(float distance, float baseDistance, float basePrice, float changeBasePrice){
        BaseDistance bd = new BaseDistance();
        //Kerela
        //float basePrice = priceMap.getBasePrice();
        //float changeBasePrice = priceMap.getBaseAddPrice();
       // float baseDistance = priceMap.getKm();
        float totalDistance ;
        float base;
        System.out.println("distance 1" + distance + "baseDistance 1 " + baseDistance  + "basePrice 1"  + basePrice );

        if(baseDistance > 0 && basePrice > 0)
        if(distance > 0 && distance > baseDistance && changeBasePrice > 1 ){
            //reduce the price
            totalDistance = distance - baseDistance;
            bd.setDistance(totalDistance);
             bd.setBase(changeBasePrice);
            System.out.println("baseDistance is greater changedBase Price " + totalDistance + "changeBasePrice "  + changeBasePrice  );
            return bd;
        }
        //General
        else if(distance > 0 && distance >= baseDistance && basePrice > 0){
            //reduce the price
             totalDistance = distance - baseDistance;
             bd.setDistance(totalDistance);
             // float base =finalPrice;

             bd.setBase(basePrice);

            System.out.println(" distance greater than distance totalDistance " + totalDistance + "basePrice "  + basePrice  );
            return bd;
        }
        else if(distance <= baseDistance) {
            bd.setBase(basePrice);
            bd.setDistance(0);
            System.out.println(" less than base price "  );
            return bd;
        }
        else {
            bd.setBase(basePrice);
            bd.setDistance(distance);
            System.out.println("general "  );
            return bd;
        }
      return bd;
    }
    public BaseDistance getTimeCalulation(float baseTime,float totalTravelTime,float basePrice,float changeBasePrice) {

        BaseDistance bd = new BaseDistance();
        //Kerela
        //float basePrice = priceMap.getBasePrice();
        //float changeBasePrice = priceMap.getBaseAddPrice();
        // float baseDistance = priceMap.getKm();
        float totalDistance ;
        float base;
        System.out.println("baseTime ->" + baseTime + "totalTravelTime -> " + totalTravelTime  + "basePrice ->"  + basePrice );

        if(totalTravelTime > 0 && baseTime > 0 && basePrice > 0) {
            if (totalTravelTime > 0 && totalTravelTime > baseTime && changeBasePrice > 1) {
                //reduce the price
                totalDistance = totalTravelTime - baseTime;
                bd.setTotalTravelTime(totalDistance);
                bd.setBase(changeBasePrice);
                System.out.println("baseTime is greater changedBase Price " + totalDistance + "changeBasePrice " + changeBasePrice);
                return bd;
            }
            //General
            else if (totalTravelTime > 0 && totalTravelTime >= baseTime && basePrice > 0) {
                //reduce the price
                totalDistance = totalTravelTime - baseTime;
                bd.setTotalTravelTime(totalDistance);
                bd.setBase(basePrice);

                System.out.println(" distance greater than distance totalDistance " + totalDistance + "basePrice " + basePrice);
                return bd;
            } else if (totalTravelTime <= baseTime) {
                bd.setBase(basePrice);
                bd.setTotalTravelTime(0);
                System.out.println(" less than base price ");
                return bd;
            } else {
                bd.setBase(basePrice);
                bd.setTotalTravelTime(totalTravelTime);
                System.out.println("general ");
                return bd;
            }
        }
        //SPECIAL CASE //base time is ZERO (baseTime)
        else if(totalTravelTime > 0 && baseTime > 0 && basePrice > 0){
            bd.setBase(basePrice);
            bd.setTotalTravelTime(totalTravelTime);
            System.out.println("LAST ");
            return bd;
        }
        return bd;


    }
        public PriceTime getKmandPriceCalculation( float distance, PriceMap priceMap){

        //km = 100
        //wait = 100

        PriceTime priceTime = new PriceTime();
        System.out.println("priceMap 2" + priceMap.getKmTwo());
        System.out.println("priceMap 3" + priceMap.getKmThree());

        if(wrongDistanceSetting(priceMap.getKmTwo()) && (distance >= priceMap.getKmTwo() && distance <= priceMap.getKmThree())){
            priceTime.setPrice(priceMap.getPriceTwo());
            if(priceMap.getWaitTimeTwo() > 0) {
                priceTime.setWaitingTime(priceMap.getWaitTimeTwo());
            }
            System.out.println(" 1-> " + distance + " price" + priceTime.getPrice());
        }
        else if(wrongDistanceSetting(priceMap.getKmThree()) && (distance >= priceMap.getKmThree() && distance <= priceMap.getKmFour())){
            priceTime.setPrice(priceMap.getPriceThree());
            if(priceMap.getWaitTimeThree() > 0){
                priceTime.setWaitingTime(priceMap.getWaitTimeThree());
            }
            System.out.println(" 2-> " + distance + " price " + priceTime.getPrice());
        } //have to add day or time Time price here
        else if(wrongDistanceSetting(priceMap.getKmFour()) && distance > priceMap.getKmFour() && distance <= priceMap.getKmFive()){

            priceTime.setPrice(priceMap.getPriceFour());
            if(priceMap.getWaitingTimeFour() > 0){
                priceTime.setWaitingTime(priceMap.getWaitingTimeFour());
            }
            System.out.println(" 4-> " + distance + " price " + priceTime.getPrice());
        } //have to add day or time Time price here
        else if(wrongDistanceSetting(priceMap.getKmFive()) && (distance > priceMap.getKmFive() && distance <= priceMap.getKmSix())){

            priceTime.setPrice(priceMap.getPriceFive());
            if(priceMap.getWaitingTimeFive() > 0){
                priceTime.setWaitingTime(priceMap.getWaitingTimeFive());
            }
            System.out.println(" 5 -> " + distance + " price " + priceTime.getPrice());
        } //have to add day or time Time price here
        else if(wrongDistanceSetting(priceMap.getPriceSix()) && (distance > priceMap.getKmFive()) ){
            //else if(between(distance,priceMap.getKmSix(),100000)){
            priceTime.setPrice(priceMap.getPriceSix());
            if(priceMap.getWaitingTimeSix() > 0){
                priceTime.setWaitingTime(priceMap.getWaitingTimeSix());
            }
            System.out.println(" 6-> " + distance + " price " + priceTime.getPrice());
        } //have to add day or time Time price here

        //Ignore it at the moment.
        //This is excaptioncal case - lazy ness , enter into ony getKmTwo = 30kn and not in getKmThree
        else if(wrongDistanceSetting(priceMap.getKmTwo()) && (distance >= priceMap.getKmTwo() && priceMap.getKmThree() <  5)){
                priceTime.setPrice(priceMap.getPriceTwo());
                if(priceMap.getWaitTimeTwo() > 0) {
                    priceTime.setWaitingTime(priceMap.getWaitTimeTwo());
                }
                System.out.println(" 1-> " + distance + " price" + priceTime.getPrice());
         }
         else {
            priceTime.setPrice(priceMap.getPrice());
            priceTime.setWaitingTime(priceMap.getWaitTime());
            System.out.println(" unlimited ->  " + distance + " price " + priceTime.getPrice());
        }

        priceTime.setTravelTime(priceMap.getTravelTime());
        //if(travelTime > 0){
         //   priceTime.setTravelTime(travelTime * priceMap.getTravelTime());
       // }
        System.out.println(distance + "calucaltion Price -->" +priceTime.getPrice()  + " waiting time "  + priceTime.getWaitingTime() + " travel time" + priceMap.getTravelTime() );
        return priceTime;
    }
    private boolean wrongDistanceSetting(float twoThree){
        if(twoThree > 10){
            return true;
        }
        return false;
    }

}
