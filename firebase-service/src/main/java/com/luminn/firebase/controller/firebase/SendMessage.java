package com.luminn.firebase.controller.firebase;

import com.google.api.client.util.Data;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import java.util.*;
import java.util.Map.Entry;

public class SendMessage implements Runnable {

	private FBMessage fbMessage;
	private boolean isRunning;

	private Map<String, TaxiDriver> messageSentExpired = new HashMap<String, TaxiDriver>();
	private Map<String, TaxiDriver> messageSent = new HashMap<String, TaxiDriver>();

	//old code, we will remove later
	private boolean runing = false;
	private List<TaxiDriver> drivers;
	private List<TaxiDriver> processed;

 	private FireBaseConfig fireBaseConfig;

	public SendMessage(FBMessage fbMessage, FireBaseConfig fireBaseConfig) {
		System.out.println("Creating SendMessage Obj");
		this.fbMessage = fbMessage;
		isRunning = true;
		this.fireBaseConfig = fireBaseConfig;
	}

	public SendMessage(List<TaxiDriver> list, FBMessage message) {
		this.drivers = list;
		this.fbMessage = message;
		this.runing = true;
		this.processed = new ArrayList<TaxiDriver>();

	}

	public String setAccept(String fbToken) {
		isRunning = false;
		synchronized (fbToken) {
			try {
				TaxiDriver driver = fireBaseConfig.getDrivers().get(fbToken);

				if (messageSentExpired.containsKey(fbToken))
					return "Request Expired";

				if (driver != null && DemoConstants.STATUS_MSG_SENT.contentEquals(fbMessage.getStatus())) {

					fbMessage.setDriverName(driver.getName());
					fbMessage.setCarName(driver.getCarName());
					fbMessage.setStatus(DemoConstants.STATUS_MSG_ACCEPT);
					fireBaseConfig.getMessagesReference().child(fbMessage.getId()).setValueAsync(fbMessage);

					Map<String, TaxiDriver> drivers = fireBaseConfig.getDrivers();
					if (!drivers.isEmpty()) {
						for (Entry<String, TaxiDriver> entry : drivers.entrySet()) {
							TaxiDriver td = entry.getValue();

							if (!messageSent.containsKey(td.getFbToken())) {
								Message message = Message.builder().putData("message", "Notificaiton is Expired")
										.putData("action", "ACTION_EXPIRED").putData("title", "Notificaiton is Expired")
										.setToken(td.getFbToken()).build();

								String response = FirebaseMessaging.getInstance().send(message);
								System.out.println(response);
							}
						 	 
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "Driver Accept and store in firebase Database";

	}

	@Override
	public void run() {
		try {
			System.out.println("SendMessage Sending Message to Drivers.");

			Iterator<Entry<String, TaxiDriver>> itr = fireBaseConfig.getDrivers().entrySet().iterator();

			if (itr.hasNext()) {
				while (itr.hasNext()) {
					Entry<String, TaxiDriver> entry = itr.next();
					System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
					if (isRunning) {

						if (DemoConstants.STATUS_MSG_ACCEPT.contentEquals(fbMessage.getStatus())) {
							isRunning = false;
							continue;
						}

						System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

						TaxiDriver driver = entry.getValue();

						System.out.println("driverId:" + driver.getFbToken());

						Data data = new Data();
						Map<String, String> newMap = new HashMap<String, String>();
						newMap.put("id", fbMessage.getId());
						newMap.put("message", fbMessage.getMessage());
						newMap.put("title", fbMessage.getMessage());
						newMap.put("action", "ACTION_GOT_NOTIFICATION");

						System.out.println("Android ---------------------------------> Map map " + data.toString());

						AndroidConfig androidConfig = AndroidConfig.builder().putAllData(newMap)
								.setPriority(Priority.HIGH).build();
						Message message = Message.builder().setAndroidConfig(androidConfig)
								.setToken(driver.getFbToken()).build();
						String response = FirebaseMessaging.getInstance().send(message);
						System.out.println(response);

						fbMessage.setStatus(DemoConstants.STATUS_MSG_SENT);
						fireBaseConfig.getMessagesReference().child(fbMessage.getId()).setValueAsync(fbMessage);
						
						messageSent.put(driver.getFbToken(), driver);
						Thread.sleep(1000 * 30);
						messageSentExpired.put(driver.getFbToken(), driver);
					}
				}
			} else {

				isRunning = false;

				if (fbMessage != null && !DemoConstants.STATUS_MSG_ACCEPT.contentEquals(fbMessage.getStatus())) {
					fbMessage.setStatus(DemoConstants.STATUS_MSG_DRIVERNOTFOUND);
					fireBaseConfig.getMessagesReference().child(fbMessage.getId()).setValueAsync(fbMessage);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public String toString() {
		return "SendMessage [fbMessage=" + fbMessage + ", isRunning=" + isRunning + ", messageSentExpired="
				+ messageSentExpired + "]";
	}

}
