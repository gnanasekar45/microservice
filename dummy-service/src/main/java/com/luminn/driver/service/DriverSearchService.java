package com.luminn.driver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luminn.driver.model.DriverSearch;
import com.luminn.driver.repository.DriverRepository;

@Service
public class DriverSearchService {
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Value("${driver.limit}")
	private int driverLimit;
	
	public List<DriverSearch> searchByLocation(double lt, double lg, double dis) {
		final double R = 6371; // kilometres
        final double Δφ = (dis / R)*180/Math.PI;
        final double Δλ = 2.0* Math.asin(Math.sin(dis/(2.0*R))/Math.cos(lt*Math.PI/180))*180/Math.PI;
        double mLat1 = lt - Δφ;
        if(mLat1<-90)
            mLat1=-90;
            double mLon1 = lg - Δλ;
            double mLat2 = lt + Δφ;
        if(mLat2>90)
             mLat2=90;
             double mLon2 = lg + Δλ;
		
		List<DriverSearch> drivers = new ArrayList<>();
		System.out.println(mLon2+ " -- "+ mLon1+ "-- "+ mLat2 + " -- "+ mLat1);

		this.driverRepository.findByLocation(mLon1, mLon2, mLat1, mLat2).forEach(driver -> {
			System.out.println(driver.getName());
			final double distance = this.distance(lt, lg, driver.getLatitude(), driver.getLongitude());
            System.out.println(distance + " distance -- " + distance );
			drivers.add(new DriverSearch(driver, distance));
		});
		return drivers;
	}
	
	private double distance(double lat1, double lon1, double lat2, double lon2) {
    	final double R = 6371;
        final double φ1 = lat1 * Math.PI/180;
        final double φ2 = lat2 * Math.PI/180;
        final double Δφ = (lat2-lat1) * Math.PI/180;
        final double Δλ = (lon2-lon1) * Math.PI/180;

        final double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                Math.sin(Δλ/2) * Math.sin(Δλ/2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        final double d = R * c;
        return Math.abs(d);
    }
}
