/**
 * 
 */
package com.luminn.firebase.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Ankur
 *
 */
public enum CARTYPE {
	Auto,
	Taxi,
	Taxi4,
	Taxi6,
	Van,
	Transport,
	Bike,
	Delivery,
	Car,
	Cycle,
	Tourist,
	Traveller,
	Pickup,
	Ambulance,
	Bus;

	private static Map<String, CARTYPE> namesMap = new HashMap<String, CARTYPE>(3);

	static {
		//namesMap.put("taxi", TAXI);
		namesMap.put("auto", Auto);
		namesMap.put("taxi", Taxi);
		namesMap.put("taxi4", Taxi4);
		namesMap.put("taxi6", Taxi6);
		namesMap.put("van", Van);
		namesMap.put("bus", Bus);
		namesMap.put("car", Car);
		namesMap.put("bike", Bike);
		namesMap.put("cycle", Cycle);
		namesMap.put("transport", Transport);
		namesMap.put("delivery", Delivery);
		namesMap.put("tourist", Tourist);
		namesMap.put("traveller", Traveller);
		namesMap.put("pickup", Pickup);
		namesMap.put("ambulance", Ambulance);
	}

	@JsonCreator
	public static CARTYPE forValue(String value) {
		return namesMap.get(StringUtils.lowerCase(value));
	}

	@JsonValue
	public String  toValue() {

		for (Map.Entry<String, CARTYPE> entry : namesMap.entrySet()) {
			if (entry.getValue() == this)
				return entry.getKey();
		}

		return null; // or fail

	}
	public static boolean check(String test) {

		for (CARTYPE c : namesMap.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}

		return false;
	}

}
