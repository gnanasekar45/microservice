package com.luminn.firebase.controller.firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executor;

@Service
public class FireBaseConfig {

	private static DatabaseReference messagesReference;
	private static DatabaseReference driversReference;
	private DatabaseReference dbRefDrivers=null;
	private DatabaseReference dbRefMetaByName=null;

	private CollectionReference colRefDrivers=null;
	private CollectionReference colRefDriversStatus=null;

	private Firestore firestore=null;

	private static final String DB_DRIVERS_NAME = "drivers";
	private static final String DB_METABYNAME_NAME = "metaname";

	private static final String DB_DRIVER_STATUS = "DRIVER_STATUS";
	private static final int MAX_SEARCH_ROW = 100;

	private final static String ENV = "dev";
	private final static String STR_REF = "demo-" + ENV+"-8";

	private final static Map<String, TaxiDriver> drivers = new HashMap<String, TaxiDriver>();
	private final static Map<String, FBMessage> messages = new HashMap<String, FBMessage>();
	private final static Map<String, SendMessage> sendMessage = new HashMap<String, SendMessage>();

	private int flagFB=0;
	private int readCount=0;


    @Value("${firebase.url}")
    private String projectUrl;

	//dbRefDrivers = firebaseDatabase.getReference().child(DB_DRIVERS_NAME);
	//dbRefMetaByName = firebaseDatabase.getReference().child(DB_METABYNAME_NAME);

	public Map<String, TaxiDriver> getDrivers() {
		return drivers;
	}

	public Map<String, FBMessage> getMessages() {
		return messages;
	}

	public Map<String, SendMessage> getSendMessage() {
		return sendMessage;
	}
	
	public void info(String env, String val) {
		FirebaseDatabase.getInstance().getReference(STR_REF).child(env).child(""+System.currentTimeMillis()).setValueAsync(val);
	}

	public DatabaseReference getMessagesReference() {
		if (messagesReference == null) {
			messagesReference = FirebaseDatabase.getInstance().getReference(STR_REF).child("fbmessages");
		}
		return messagesReference;
	}

	public DatabaseReference getDriversReference() {
		if (driversReference == null) {
			driversReference = FirebaseDatabase.getInstance().getReference(STR_REF).child("fbdrivers");
		}
		return driversReference;
	}

	public DatabaseReference getDbRefDrivers() {
		if (dbRefDrivers == null) {
			dbRefDrivers = FirebaseDatabase.getInstance().getReference(STR_REF).child(DB_DRIVERS_NAME);
		}
		return dbRefDrivers;
	}

	public DatabaseReference getDbRefMetaByName() {
		if (dbRefMetaByName == null) {
			dbRefMetaByName = FirebaseDatabase.getInstance().getReference(STR_REF).child(DB_METABYNAME_NAME);
		}
		return dbRefMetaByName;
	}

	//chandra did 30th September 2020
	@PostConstruct
	public void init() {
		FileInputStream serviceAccount;
		try {



            InputStream inputStream = new ClassPathResource("accountKey.json").getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(projectUrl).build();


			FirebaseApp.initializeApp(options);

			firestore = FirestoreClient.getFirestore();
			colRefDrivers = firestore.collection(DB_DRIVERS_NAME);
			colRefDriversStatus = firestore.collection(DB_DRIVER_STATUS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  /////////////////////////////



	public double distance(double lat1, double lon1, double lat2, double lon2) {
		final double R = 6371; // kilometres
		final double φ1 = lat1 * Math.PI/180; // φ, λ in radians
		final double φ2 = lat2 * Math.PI/180;
		final double Δφ = (lat2-lat1) * Math.PI/180;
		final double Δλ = (lon2-lon1) * Math.PI/180;

		final double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
				Math.cos(φ1) * Math.cos(φ2) *
						Math.sin(Δλ/2) * Math.sin(Δλ/2);
		final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		final double d = R * c; // in kilometres
		return Math.abs(d);
	}

	public void removeAllDB(){
		System.out.println("Remove "+DB_DRIVERS_NAME);
		getDbRefMetaByName().removeValueAsync().addListener(new Runnable() {
			@Override
			public void run() {}
		}, new Executor() {
			@Override
			public void execute(Runnable command) {
				System.out.println("Remove "+DB_METABYNAME_NAME);
				getDbRefDrivers().removeValueAsync();
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			//Logger.getLogger(FirebaseUtil.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println(" Error " + ex );
		}
	}

	private void readFromDBRefMetaName(Vector<String> vec, DataSnapshot ds){
		for(DataSnapshot dsChild: ds.getChildren()){
			if(!dsChild.hasChildren())vec.addElement(dsChild.getValue(String.class));
		}
		for(DataSnapshot dsChild: ds.getChildren()){
			if(dsChild.hasChildren())readFromDBRefMetaName(vec, dsChild);
		}
	}


	private void waitting(long milli) {
		long now=System.currentTimeMillis();
		while(System.currentTimeMillis()- now < milli && flagFB!=0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	///////////////////////////////////////////////////////////////////////////////////////////

	public boolean  updateDriver(String key, String name, double latitude, double longitude, int status){
		try{
			DocumentReference dr = colRefDrivers.document(key);
			Map<String, Object> data = new HashMap<>();
			data.put("name", name);
			data.put("latitude", latitude);
			data.put("longitude", longitude);
			data.put("status", status);
			dr.set(data);
			return true;
		}catch(Exception ex){}
		return false;
	}

	public boolean removeDriver(String key){
		colRefDrivers.document(key).delete();
		return true;
	}
	public boolean addDriver2(String name, double latitude, double longitude, int status){
		Map<String, Object> data = new HashMap<>();
		data.put("name", name);
		data.put("latitude", latitude);
		data.put("longitude", longitude);
		data.put("status", status);
		colRefDrivers.add(data);
		return true;
	}

	public boolean addDriverReal(String name, double latitude, double longitude, int status){
		Map<String, Object> data = new HashMap<>();
		data.put("DRIVER_NAME", name);
		data.put("LATITUDE", latitude);
		data.put("LONGITUDE", longitude);
		colRefDriversStatus.add(data);
		return true;
	}



	public ArrayList<DriverSearchs> searchByStatus(String supplierId){
		ArrayList<DriverSearchs> res = new ArrayList<DriverSearchs>();
		Query query = colRefDriversStatus.limit(10).whereGreaterThanOrEqualTo("SUPPLIER", supplierId);

		try {
			for (DocumentSnapshot document : query.get().get().getDocuments()) {
				System.out.println(" id --->" + document.getId());
				DriverSearchs drvSch= getDriverStatus(document.getId(),document.getData());
				res.add(drvSch);
				System.out.println(" SUPPLIER  --->" + document.get("SUPPLIER"));
			}
		} catch (Exception ex) {}

		System.out.println(" Query 2--->" + query.toString());

		return res;
	}
	private DriverSearchs getDriverRealSearch(String key, Map<String, Object> map){

		String latlong = map.get("LOCATION").toString();
		String[] latlongs = null;

		if(latlong.length() > 0 )
		 latlongs = latlong.split(",");
		try {
			DriverSearchs drvSch = new DriverSearchs(
					key,
					(String)(map.get("DRIVER_NAME")),
					Double.parseDouble(latlongs[0]),
					Double.parseDouble(latlongs[1]),
					(map.get("CAR_TYPE").toString())
			);
			return drvSch;
		} catch (Exception ex) {}
		return null;
	}


	private DriverSearchs getDriverearch(String key, Map<String, Object> map){
		try {
			DriverSearchs drvSch = new DriverSearchs(
					key,
					(String)(map.get("name")),
					Double.parseDouble(map.get("latitude").toString()),
					Double.parseDouble(map.get("longitude").toString()),
					Integer.parseInt(map.get("status").toString())
			);
			return drvSch;
		} catch (Exception ex) {}
		return null;
	}

	private DriverSearchs getDriverStatus(String key, Map<String, Object> map){
		try {
			DriverSearchs drvSch = new DriverSearchs((Boolean)(map.get(key)),(map.get("DRIVER_NAME").toString()),
					(map.get("CAR_TYPE")).toString());
			return drvSch;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public double distance2(double lat1, double lon1, double lat2, double lon2) {
		final double R = 6371; // kilometres
		final double φ1 = lat1 * Math.PI/180; // φ, λ in radians
		final double φ2 = lat2 * Math.PI/180;
		final double Δφ = (lat2-lat1) * Math.PI/180;
		final double Δλ = (lon2-lon1) * Math.PI/180;

		final double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
				Math.cos(φ1) * Math.cos(φ2) *
						Math.sin(Δλ/2) * Math.sin(Δλ/2);
		final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		final double d = R * c; // in kilometres
		return Math.abs(d);
	}

	public ArrayList<DriverSearchs> searchByName2(String name){
		Query query = colRefDrivers.whereEqualTo("name", name);

		ArrayList<DriverSearchs> res = new ArrayList<DriverSearchs>();
		try {
			for (DocumentSnapshot document : query.get().get().getDocuments()) {
				DriverSearchs drvSch=getDriverearch(document.getId(), document.getData());
				if(drvSch!=null)res.add(drvSch);
			}
		} catch (Exception ex) {}
		return res;
	}

}
