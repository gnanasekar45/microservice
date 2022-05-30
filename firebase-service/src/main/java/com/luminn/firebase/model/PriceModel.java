package com.luminn.firebase.model;


import com.luminn.firebase.dto.CATEGORY;
import com.luminn.firebase.entity.Price;
import com.luminn.firebase.dto.PriceDTO;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.repository.PriceRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.PriceService;
import com.luminn.firebase.util.CARTYPE;
import com.luminn.firebase.util.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Component
public class PriceModel {

    @Autowired
    PriceService priceService;

    //@Autowired
    //CityModel cityModel;

    @Autowired
    PriceRepository priceRepository;
    @Autowired
    private MessageByLocaleService messageSource;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    TaxiDetailModel taxiDetailModel;

    private static final Logger log = Logger.getLogger(PriceModel.class.getName());

    public PriceDTO getPriceByRegion(String region, String taxiType){

        //Region or code
        List<Price> priceList = priceRepository.findAll();
        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        dto.setKm(3.0f);
        for(Price price : priceList){
            log.info("Region ---"+price.getRegion() );
            if(price.getRegion() != null&& price.getRegion().equalsIgnoreCase(region)
              && price.getCarType() != null && price.getCarType().equalsIgnoreCase(taxiType)){
                dto.setWaitingTime(price.getWaitingTime());
                dto.setType(price.getCarType());
                dto.setRegion(price.getRegion());
                dto.setKm(price.getPrice());
                dto.setCancellation(price.getCancellation());
                return dto;
            }
        }
        return dto;
    }
    public Price createDummyPrice(String carType,String region,String country,String domain,String supplierId){

        if(country.equalsIgnoreCase("in"))
          return  createPriceIndia(carType,region,domain,supplierId);
        else if(country.equalsIgnoreCase("ch"))
         return   createPriceSwiss(carType,region,Path.DOMAIN.MYDOMAIN,supplierId);
        return createPriceIndia(carType,region,domain,supplierId);
    }
    public Price createPriceIndia(String carType,String region,String domain,String supplierId){
        Price price = new Price();
        price.setCarType(carType);
        price.setRegion(region);
        price.setBasePrice(100);
        price.setPrice(10);
        price.setPeakPrice(20);
        price.setWaitingTime(5);
        price.setMinimumPrice(10);
        if(!"".equalsIgnoreCase(domain) && !domain.equalsIgnoreCase("string")){
            price.setDomain(domain);
        }
        else
            price.setDomain(Path.DOMAIN.MYDOMAIN);

        price.setCategory("Taxi");
        price.setSupplierId(supplierId);
        price.setZipCode(10000);
        price.setPercentage(10);
        price.setTax(5);
        price.setKm(5);
        price.setWaitingTime(2);
        price.setPrice(3);

        price.setKmTwo(50);
        price.setPriceTwo(20);
        price.setWaitingTimeTwo(10);

        price.setKmThree(100);
        price.setPriceThree(20);
        price.setWaitingTimeThree(10);


         priceRepository.save(price);

      return price;
    }
    public Price createPriceSwiss(String carType,String region,String domain,String supplierId){
        Price price = new Price();
        price.setCarType(carType);
        price.setRegion(region);
        price.setBasePrice(100);
        price.setPrice(5);
        price.setPeakPrice(20);
        price.setWaitingTime(5);
        price.setMinimumPrice(30);
        price.setZipCode(10000);
        price.setTax(1);
        price.setPercentage(10);
        price.setDomain(domain);
        price.setTax(5);
        price.setDomain(domain);
        price.setKm(4);
        price.setCategory("Taxi");

        price.setKmTwo(5);
        price.setPriceTwo(5);
        price.setWaitingTimeTwo(5);
        price.setSupplierId(supplierId);

        priceRepository.save(price);
        return price;
    }


    public PriceDTO getPriceByRegion(String region,float kmPrice){

        //Region or code
        List<Price> priceList = priceRepository.findAll();
        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        dto.setKm(3.0f);
        for(Price price : priceList){
            log.info("Region ---"+price.getRegion() );
            if(price.getRegion() != null&& price.getRegion().equalsIgnoreCase(region)  ){
                if(kmPrice == price.getPrice()) {
                    dto.setWaitingTime(price.getWaitingTime());
                    dto.setType(price.getCarType());
                    dto.setRegion(price.getRegion());
                    dto.setKm(price.getPrice());
                    return dto;
                }
            }
        }
        return dto;
    }

    public ModelStatus createPrice(PriceDTO priceDTO){

        Price p =new Price();
        p.setCarType(priceDTO.getType());
        p.setPrice(priceDTO.getPrice());
        p.setWaitingTime(priceDTO.getWaitingTime());
        p.setRegion(priceDTO.getRegion());
        p.setZipCode(priceDTO.getZipCode());
        p.setBasePrice(priceDTO.getBasePrice());
        p.setPeakPrice(priceDTO.getPeakPrice());
        p.setMinimumPrice(priceDTO.getMinimumPrice());
        p.setKm(priceDTO.getKm());
        p.setTravelTime(priceDTO.getTravelTime());
        p.setTax(priceDTO.getTax());
        p.setPercentage(priceDTO.getPercentage());

        String domain = priceDTO.getDomain();
        p.setPriceTwo(priceDTO.getPriceTwo());
        p.setWaitingTimeTwo(priceDTO.getWaitingTimeTwo());
        p.setKmTwo(priceDTO.getKmTwo());

        p.setPriceThree(priceDTO.getPriceThree());
        p.setWaitingTimeThree(priceDTO.getWaitingThree());
        p.setKmThree(priceDTO.getKmThree());


        p.setPriceFour(priceDTO.getPriceFour());
        p.setWaitingTimeFour(priceDTO.getWaitingTimeFour());
        p.setKmFour(priceDTO.getKmFour());


        p.setPriceFive(priceDTO.getPriceFive());
        p.setWaitingTimeFive(priceDTO.getWaitingTimeFive());
        p.setKmFive(priceDTO.getKmFive());


        p.setPriceSix(priceDTO.getPriceSix());
        p.setWaitingTimeSix(priceDTO.getWaitingTimeSix());
        p.setKmSix(priceDTO.getKmSix());
        p.setBaseAddPrice(priceDTO.getBaseAddPrice());
        p.setBaseTime(priceDTO.getBaseTime());


        p.setCategory(getCategoryType(priceDTO.getCategory()).name());

        p.setSupplierId(priceDTO.getSupplierId());

		if("".equalsIgnoreCase(domain) || domain.equalsIgnoreCase("string"))
			p.setDomain(Path.DOMAIN.MYDOMAIN);
		else
			p.setDomain(domain);

        priceRepository.save(p);
        return ModelStatus.CREATED;
    }

    public CATEGORY getCategoryType(String type) {
        if (!"".equalsIgnoreCase(type)  && !type.equalsIgnoreCase("String")) {
            return CATEGORY.valueOf(type.toUpperCase());
        }
        return CATEGORY.TAXI;
    }

    public ModelStatus updatePrice(PriceDTO priceDTO) {

        Price p = null;
        Optional<Price> prices = priceRepository.findById(priceDTO.getId());
        if(prices.isPresent())
            p = prices.get();

        //Price p = priceService.getById(Price.class,priceDTO.getId());
        //Price p =new Price();
        p.setPrice(priceDTO.getPrice());
        p.setPercentage(priceDTO.getPercentage());
        p.setTax(priceDTO.getTax());
        p.setPeakPrice(priceDTO.getPeakPrice());
        p.setBasePrice(priceDTO.getBasePrice());
        p.setKm(priceDTO.getKm());
        p.setMinimumPrice(priceDTO.getMinimumPrice());
        p.setKmTwo(priceDTO.getKmTwo());
        p.setPriceTwo(priceDTO.getPriceTwo());
        p.setCategory(priceDTO.getCategory());
        p.setSupplierId(priceDTO.getSupplierId());

		if("".equalsIgnoreCase(priceDTO.getDomain()) || priceDTO.getDomain().equalsIgnoreCase("string"))
			p.setDomain(Path.DOMAIN.MYDOMAIN);
		else
			p.setDomain(priceDTO.getDomain());
        priceRepository.save(p);
        return ModelStatus.UPDATED;

    }

    public ModelStatus deletePrice(PriceDTO priceDTO)  {

        Optional<Price> price = priceRepository.findById(priceDTO.getId());
        Price p = null;

        if(price.isPresent())
            p = price.get();
        //Price p =new Price();
        priceRepository.delete(p);

        return ModelStatus.DELETED;

    }

    public double getBestPrices(String carType,double currentPrice){

        List<Price> priceList = priceRepository.findAll();

        for(Price price : priceList){

            if(price.getCarType().equals(carType)){
                if(currentPrice >= price.getPrice())
                    return price.getPrice();
                else
                    return currentPrice;
            }

        }
        return 2.0;
    }

    public PriceDTO getPriceByRegionZipCode(String code){

        /*com.lumiin.mytalk.pojo.City city = cityModel.findByZipCode(code);

        if(city != null)
            log.info("city ---"+city.getCode() + "----" + city.getName() );

        //Region or code
        List<Price> priceList = priceService.list(Price.class);
        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        //default
        dto.setKm(3.0f);
        if(city != null)
            for(Price price : priceList){
                log.info("Region ---"+price.getRegion() );
                if(price.getRegion() != null&& price.getRegion().equalsIgnoreCase(city.getName())){
                    dto.setWaitingTime(price.getWaitingTime());
                    dto.setType(price.getCarType());
                    dto.setRegion(price.getRegion());
                    dto.setKm(price.getPrice());
                    return dto;
                }
            }
        return dto;*/
        return new PriceDTO();
    }

    public List<PriceDTO> getPriceDomain(String domain){

        List<PriceDTO> allPricesofTaxies = new ArrayList<>();
        List<Price> priceList = priceRepository.findAll();
        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        //default
        dto.setKm(3.0f);
        //if(city != null)
            for(Price price : priceList){

                if(!"".equalsIgnoreCase(domain) && price.getDomain()  != null &&
                        price.getDomain().equalsIgnoreCase(domain)){
                    log.info("Domain ---"+price.getDomain() );
                    dto.setWaitingTime(price.getWaitingTime());
                    dto.setType(price.getCarType());
                    dto.setRegion(price.getRegion());


                    dto.setPeakPrice(price.getPeakPrice());

                    dto.setCancellation(price.getCancellation());

                    //WAITING TIME
                    dto.setKm(price.getPrice());
                    dto.setPrice(price.getPrice());
                    dto.setWaitingTime(price.getWaitingTime());

                    //TWO
                    dto.setKmTwo(price.getKmTwo());
                    dto.setPriceTwo(price.getPriceTwo());
                    dto.setWaitingTimeTwo(price.getWaitingTimeTwo());

                    //THREE
                    dto.setKmThree(price.getKmThree());
                    dto.setPriceThree(price.getPriceThree());
                    dto.setWaitingThree(price.getWaitingTimeThree());



                    allPricesofTaxies.add(dto);
                }
            }
        return allPricesofTaxies;
    }


    public List<PriceDTO> getSupplierId(String supplierId,String type,String category){

        List<PriceDTO> allPricesofTaxies = new ArrayList<>();
        List<Price> priceList = priceRepository.findBySupplierId(supplierId);
        //log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        //default
        dto.setKm(3.0f);
        //if(city != null)
        log.info("type --->"+priceList.size() + "type " + type + "category " + category );

        for(Price price : priceList){


            if(!"".equalsIgnoreCase(type) && !"".equalsIgnoreCase(category)&&
            price.getCategory().equalsIgnoreCase(category) && price.getCarType().equalsIgnoreCase(type) ){

                dto.setWaitingTime(price.getWaitingTime());
                dto.setType(price.getCarType());
                dto.setRegion(price.getRegion());


                dto.setPeakPrice(price.getPeakPrice());

                dto.setCancellation(price.getCancellation());

                //WAITING TIME
                dto.setKm(price.getPrice());
                dto.setPrice(price.getPrice());
                dto.setWaitingTime(price.getWaitingTime());

                //TWO
                dto.setKmTwo(price.getKmTwo());
                dto.setPriceTwo(price.getPriceTwo());
                dto.setWaitingTimeTwo(price.getWaitingTimeTwo());

                //THREE
                dto.setKmThree(price.getKmThree());
                dto.setPriceThree(price.getPriceThree());
                dto.setWaitingThree(price.getWaitingTimeThree());
                allPricesofTaxies.add(dto);
            }
        }
        return allPricesofTaxies;
    }

    public List<PriceDTO> getSupplierIdTaxiId(String taxiId){
        TaxiDetail td =  taxiDetailModel.getTaxiId(taxiId);
        log.info("supplierId ---"+td);

        List<PriceDTO> priceList = getSupplierId(td.getSupplierId());


        if(priceList != null && priceList.size() > 0) {
            return priceList;
        }
        else if(priceList != null && priceList.size() == 0 ){
            List<PriceDTO> priceListDomain = getPriceDomain(Path.DOMAIN.MYDOMAIN);
            //priceList  = new ArrayList<>();
            return priceListDomain;
        }
        else if(priceList == null  ){
            List<PriceDTO> priceListDomain = getPriceDomain(Path.DOMAIN.MYDOMAIN);
            //priceList  = new ArrayList<>();
            return priceListDomain;
        }
        return null;
    }

    public List<PriceDTO> getSupplierId(String supplierId){

        List<PriceDTO> allPricesofTaxies = new ArrayList<>();
        List<Price> priceList = null;
        if(supplierId != null)
            priceList = priceRepository.findBySupplierId(supplierId);
        else
            return allPricesofTaxies;

        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        //default
        dto.setKm(3.0f);
        //if(city != null)
        for(Price price : priceList){
            {
                dto.setWaitingTime(price.getWaitingTime());
                dto.setType(price.getCarType());
                dto.setRegion(price.getRegion());
                dto.setPeakPrice(price.getPeakPrice());
                dto.setCancellation(price.getCancellation());

                //WAITING TIME
                dto.setKm(price.getPrice());
                dto.setPrice(price.getPrice());
                dto.setWaitingTime(price.getWaitingTime());

                //TWO
                dto.setKmTwo(price.getKmTwo());
                dto.setPriceTwo(price.getPriceTwo());
                dto.setWaitingTimeTwo(price.getWaitingTimeTwo());

                //THREE
                dto.setKmThree(price.getKmThree());
                dto.setPriceThree(price.getPriceThree());
                dto.setWaitingThree(price.getWaitingTimeThree());
                allPricesofTaxies.add(dto);
            }
        }
        return allPricesofTaxies;
    }
    public PriceDTO getPriceByRegionZipCode(String code,String type){

        /*City city = cityModel.findByZipCode(code);

        log.info("city ---"+city );
        //Region or code
        List<Price> priceList = priceService.list(Price.class);
        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        //default
        dto.setKm(3.0f);
        if(city != null)
            for(Price price : priceList){
                log.info("Region ---"+price.getRegion() );
                if(!"".equalsIgnoreCase(type)&& type != null){
                    type.equalsIgnoreCase(price.getCarType());

                    if(price.getRegion() != null&& price.getRegion().equalsIgnoreCase(city.getName())) {
                        dto.setWaitingTime(price.getWaitingTime());
                        dto.setType(price.getCarType());
                        dto.setRegion(price.getRegion());
                        dto.setKm(price.getPrice());
                        return dto;
                    }
                }
            }
        return dto;*/
        return null;
    }
    public PriceDTO getPriceByzipCode(int code){

        List<Price> priceList = priceRepository.findAll();
        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto = new PriceDTO();
        dto.setKm(3.0f);
        for(Price price : priceList){
            log.info("Region ---"+price.getRegion() );
            if(price.getRegion() != null&& price.getZipCode() == code){
                dto.setWaitingTime(price.getWaitingTime());
                dto.setType(price.getCarType());
                dto.setZipCode(price.getZipCode());
                dto.setKm(price.getPrice());
                return dto;
            }

        }
        return dto;
    }


    public List<Price> getDomainCategoryPrice(String domain,String category,String region){
        List<Price>  list = null;
        list =  priceRepository.findByDomainAndCategoryAndRegion(domain,category,region);
        return list;
    }
    public List<Price> getDomainCategoryPrice(String domain,String category){
        List<Price>  list = null;
        list =  priceRepository.findByDomainAndCategory(domain,category);
        return list;
    }
    public List<Price> getDomainPrice(String domain){
      List<Price>  list =  priceRepository.findByDomain(domain);
        return list;
    }

    public Price getPriceDomainRegions(String category,String type,String region,String domain) {

        //find day or night
        List<Price> priceList = null;
        log.info("category-->" + category + "type-->" + type + " region-->" + region + " domain-->" + domain) ;

        if(domain != null && domain.length() > 0 && category != null && category.length() > 0) {

            priceList = getDomainCategoryPrice(domain,category,region);
            if(priceList == null){
                priceList = getDomainCategoryPrice(domain,category);
            }
            log.info(" domain % category here");
        }
        else if(priceList == null && domain != null && domain.length() > 0) {
            priceList = getDomainPrice(domain);
            log.info(" domain here");
        }
        else if(priceList == null && region != null && region.length() > 0 ){
            priceList = priceRepository.findByRegion(region);
            log.info(" no region ");

            //check default region
            if(priceList == null)
                priceList = priceRepository.findByRegion("TamilNadu");
                //create a new region

        }
        else if(priceList == null || priceList.size() == 0) {
            priceList = priceRepository.findAll();
            log.info("no domain ");
        }

        //hashMap.clear();
        Price singlePrice = null;
        for (Price price : priceList) {

            singlePrice = null;



            if(category != null && category.equalsIgnoreCase(price.getCategory()) &&
                    price.getCarType().equalsIgnoreCase(type)){


                if(price.getRegion() == null || "".equalsIgnoreCase(price.getRegion()) ){
                        price.setRegion("TamilNadu");
                }

                if((price.getRegion().trim().equalsIgnoreCase(region))){
                    log.info("---Region ok --->" + price.toString());
                    singlePrice = price;
                    break;
                }


            }
        }
        if(singlePrice == null){
            log.info("---NO PRICE --->" );
        }
        return singlePrice;
    }

    public List<PriceDTO> getPriceAll(){

        List<Price> priceList = priceRepository.findAll();
        log.info("SIZE ---" );
        PriceDTO dto = null;
       // dto.setKm(3.0f);
        List<PriceDTO> list = new ArrayList<>();
        for(Price price : priceList){
            if(price != null && price.getCarType().equalsIgnoreCase(CARTYPE.Taxi4.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Taxi6.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Taxi.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Transport.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Auto.name())){

                dto = new PriceDTO();
                dto.setWaitingTime(price.getWaitingTime());
                dto.setType(price.getCarType());
                //dto.setZipCode(price.getZipCode());
                dto.setKm(price.getKm());

                dto.setPrice(price.getPrice());
                dto.setBasePrice(price.getBasePrice());
                dto.setPeakPrice(price.getPeakPrice());
                dto.setRegion(price.getRegion());
                dto.setDomain(price.getDomain());
                dto.setMinimumPrice(price.getMinimumPrice());
                dto.setKm(price.getKm());

                list.add(dto);
            }

        }
        log.info("DONE ---" );
        return list;
    }
    public PriceDTO getCarTypePrice(String carType,String location){
        List<Price> priceList = priceRepository.findAll();
        //List<Price> priceList = priceService.findPriceByRegion(location)
        return getCarTypePrice(priceList,carType);
    }

    public PriceDTO getCarTypePrice(List<Price> priceList,String carType){

        log.info("SIZE ---"+priceList.size() );
        PriceDTO dto =  new PriceDTO();;
        dto.setKm(3.0f);
        List<PriceDTO> list = new ArrayList<>();

        for(Price price : priceList){
            if(price != null && price.getCarType().equalsIgnoreCase(CARTYPE.Taxi4.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Taxi6.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Taxi.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Transport.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Bike.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Auto.name())
                    || price.getCarType().equalsIgnoreCase(CARTYPE.Ambulance.name())
                    ){

                if(!"".equalsIgnoreCase(carType) && carType.equalsIgnoreCase(price.getCarType())) {

                    dto.setWaitingTime(price.getWaitingTime());
                    dto.setType(price.getCarType());
                    if(price.getBasePrice() > 0)
                        dto.setBasePrice(price.getBasePrice());
                    else
                        dto.setBasePrice(3.0f);
                    if(price.getPeakPrice() > 0)
                        dto.setPeakPrice(price.getPeakPrice());
                    else
                        dto.setPeakPrice(3.5f);
                    if(price.getPrice() > 0)
                        dto.setPrice(price.getPrice());
                    else
                        dto.setPrice(3.0f);
                    //dto.setZipCode(price.getZipCode());
                    dto.setKm(price.getPrice());
                    return dto;
                }
            }
            else {
                    dto.setBasePrice(3.0f);
                    dto.setPeakPrice(3.5f);
                    dto.setPrice(3.0f);
            }

        }
        return dto;
    }

}
