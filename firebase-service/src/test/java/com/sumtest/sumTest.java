package com.sumtest;

import com.luminn.firebase.dto.TaxiCompanyDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class sumTest {

    List<TaxiCompanyDTO> allIst = new ArrayList<TaxiCompanyDTO>();
    public static void main(String[] args){
        System.out.println(" .. ");
        sumTest sm = new sumTest();
        sm.load();
        sm.getsum();
        sm.filterTest();

    }

    sumTest(){

    }
    public void load(){


        TaxiCompanyDTO dto = new TaxiCompanyDTO();
        dto.setCompany("TaxiDeal");
        dto.setCarType("Taxi4");
        dto.setCategory("Taxi");
        allIst.add(dto);

        TaxiCompanyDTO dto2 = new TaxiCompanyDTO();
        dto.setCompany("TaxiDeal");
        dto.setCarType("Taxi3");
        dto.setCategory("Taxi");
        allIst.add(dto2);


    }
    public void getsum(){
       // List<>
        /*Integer sum = allIst.stream()
                .map(x -> x.getCarType())
                .reduce(0, Integer::sum);*/

       /* Integer sum = (Integer) allIst.stream()
                .map(x -> x.getCarType())
                .collect(Collectors.summingInt(Integer::intValue));

     long count =  allIst.stream()
                .mapToInt(x -> Integer.parseInt(x.getCarType()))
                .count();*/

    }
    public void filterTest(){
        //List<TaxiCompanyDTO> sorted = allIst.stream().sorted(Comparator.comparing(TaxiCompanyDTO::getCarType).thenComparing(TaxiCompanyDTO::getCategory)).
        List<TaxiCompanyDTO> sorted = allIst.stream().sorted(Comparator.comparing(TaxiCompanyDTO::getCarType)).
                collect(Collectors.toList());
        sorted.forEach(System.out::println);
    }

    public void filter2(){

        //someDTO.getImmutableList().stream().collect(toCollection(ArrayList::new));
        /*MutableList<Integer> petAges = this.allIst
                .stream()
                .flatMap(TaxiCompanyDTO -> TaxiCompanyDTO.getCarType())
                .map(taxiCompanyDTO -> taxiCompanyDTO.)
                .collect(Collectors.toCollection(FastList::new));*/
    }

}
