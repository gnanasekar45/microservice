package com.luminn.firebase.controller.firebase;

//import com.google.api.client.util.Data;
import com.google.firebase.database.*;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.luminn.firebase.entity.User;
//import com.luminn.mytalk.dto.UserDTO;
//import com.luminn.mytalk.model.ModelStatus;
//import com.luminn.mytalk.model.SupplierModel;
//import com.luminn.mytalk.model.UserModel;
//import com.luminn.mytalk.pojo.User;
//import com.luminn.mytalk.service.UserService;
import com.luminn.firebase.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/fcm2")
public class DemoRestController {

	private Query queryMessage, queryDrivers;

	//@Autowired
    //SupplierModel supplierModel;

	//@Autowired
    //UserModel userModel;

	//@Autowired
    //UserService userService;

	//@Autowired
	//UserMongoService userMongoService;

	@Autowired
	UserMongoService taxiService;

	//old code, it will be removed
	private final static Map<String, TaxiDriver> drivers = new HashMap<String, TaxiDriver>();
	private final static Map<String, FBMessage> fbMessages = new HashMap<String, FBMessage>();
	private final static Map<String, SendMessage> sendMessages = new HashMap<String, SendMessage>();
	private DatabaseReference messagesReference;

	public DatabaseReference getMessagesReference() {

		if (messagesReference == null) {
			//messagesReference = FirebaseDatabase.getInstance().getReference("server/saving-data/fireblog")
			//		.child("fbmessages");

			messagesReference = fireBaseConfigr.getMessagesReference();
		}
		return messagesReference;
	}
	//After testing deleteting this
	//////////////////////////
	//https://stackoverflow.com/questions/45691269/google-app-engine-standard-firebase-database
	@PostConstruct
	public void init() {
		try {
			fireBaseConfigr.info("Service", "1");

			queryMessage = fireBaseConfigr.getMessagesReference().orderByChild("status")
					.equalTo(DemoConstants.STATUS_MSG_READY_TO_SEND);

			queryDrivers = fireBaseConfigr.getDriversReference().orderByChild("name");

			queryMessage.addChildEventListener(new ChildEventListener() {

				@Override
				public void onChildRemoved(DataSnapshot snapshot) {
					FBMessage message = snapshot.getValue(FBMessage.class);
					 
				}

				@Override
				public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
					FBMessage message = snapshot.getValue(FBMessage.class);

				}

				@Override
				public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
					FBMessage message = snapshot.getValue(FBMessage.class);

				}

				@Override
				public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
					FBMessage message = snapshot.getValue(FBMessage.class);
					if (!fireBaseConfigr.getSendMessage().containsKey(message.getId())) {
						SendMessage sendMessage = new SendMessage(message, fireBaseConfigr);
						fireBaseConfigr.getSendMessage().put(message.getId(), sendMessage);
						taskExecutor.execute(sendMessage);
					}

				}

				@Override
				public void onCancelled(DatabaseError error) {
					System.out.println(error);
				}
			});
			queryDrivers.addChildEventListener(new ChildEventListener() {

				@Override
				public void onChildRemoved(DataSnapshot snapshot) {
					TaxiDriver message = snapshot.getValue(TaxiDriver.class);
					fireBaseConfigr.getDrivers().remove(message.getFbToken());

				}

				@Override
				public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
					TaxiDriver message = snapshot.getValue(TaxiDriver.class);
					fireBaseConfigr.getDrivers().put(message.getFbToken(), message);
				}

				@Override
				public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
					TaxiDriver message = snapshot.getValue(TaxiDriver.class);
					fireBaseConfigr.getDrivers().put(message.getFbToken(), message);

				}

				@Override
				public void onCancelled(DatabaseError error) {
					System.out.println(error);

				}
			});

			// new Thread(new DemoTaskScheduler()).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String TOKEN = "";

	@Autowired
	private FireBaseConfig fireBaseConfigr;

	@Autowired
	private TaskExecutor taskExecutor;

	@RequestMapping(value ="/sendmessage", method = RequestMethod.POST)
	public String listProducts() {

		return listProducts("Title");
	}

	@RequestMapping(value ="/driver", method = RequestMethod.POST)
    TaxiDriver driver(@RequestBody TaxiDriver driver) {

		System.out.println("Before driver pass 1" + driver.toString());
		//send to real DB
		// status = supplierModel.createUserDriver(dto, true,true);

		long userId = 0l;
		String ids1 = null;
		User user = new User();
		 //Taxi taxi = new Taxi();

		//User user = userMonService.getFcmToken(driver.getFbToken());
		 user =	taxiService.getToken(driver.getFbToken());

		//Taxi user = taxiService.
		if(user.getToken() == null || "".equalsIgnoreCase(user.getToken()) || user == null) {
			ids1 = dummyDriver(driver);
			//user = userModel.getById(userId);
			//user.setFcmToken(driver.getFbToken());

			System.out.println(" Id  " + ids1);
		}
		else if(ids1 != null && user.getFcmToken().equalsIgnoreCase(driver.getFbToken())){
			user.setFirstName(driver.getName());
			taxiService.updateToken(user.getName(),user.getToken());

			System.out.println(" user.getFcmToken() " + user.getFcmToken());
		}
		else if(ids1 != null) {
			user.setFirstName(driver.getName());
			taxiService.updateDriver(user);
			System.out.println(" driver.getName() " + driver.getName());
		}

		System.out.print("fireBaseConfigr/getPath -->" + fireBaseConfigr.getDriversReference().child(driver.getFbToken()).getPath());
		fireBaseConfigr.getDriversReference().child(driver.getFbToken()).setValueAsync(driver);
		return driver;
	}

	public String dummyDriver(TaxiDriver driver){

		String sf = String.valueOf(getRandomNumber());
		System.out.print(" Before sf -->" + sf);
		long id = 0;
		String ids = null;



		User user = new User();
		user.setName(driver.getName());
		user.setToken(driver.getFbToken());
		user.setStatus("Test");
		user.setLatitude(1f);
		user.setLongitude(2f);




		try {

			taxiService.addUserOne(user);

			ids =	taxiService.findNameId(user.getName());
			/*id = supplierModel.createUserDriverFCM(userDTO, true, true);
			if(id == 0) {
				id = supplierModel.getUserKeyId();
				System.out.print(" id -->" + id);
			}*/
			System.out.print(" id after -->" + id);
		}
		catch(Exception e){
			System.out.println(" ERROR " + e.getMessage());
			e.printStackTrace();
		}
		return ids;
	}

	public  int getRandomNumber(){
		// create instance of Random class
		Random rand = new Random();

		// Generate random integers in range 0 to 999
		int rand_int1 = rand.nextInt(1000);
		return rand_int1;
	}

	//@GetMapping("/drivers")
	@RequestMapping(value ="/drivers", method = RequestMethod.POST)
	Map<String, TaxiDriver> drivers() {
		return fireBaseConfigr.getDrivers();
	}

	//delete it later
	//@PostMapping("/old/driver")
	@RequestMapping(value ="/old/driver", method = RequestMethod.POST)
    TaxiDriver olddriver(@RequestBody TaxiDriver driver) {
		drivers.put(driver.getFbToken(), driver);
		return driver;
	}

	//@GetMapping("/messages")
	@RequestMapping(value ="/messages", method = RequestMethod.GET)
	Map<String, FBMessage> messages() {
		return fireBaseConfigr.getMessages();
	}

	//@GetMapping("/sendmessages")hhh
	@RequestMapping(value ="/sendmessages", method = RequestMethod.GET)
	Map<String, SendMessage> sendMessages() {
		return fireBaseConfigr.getSendMessage();
	}

	//@RequestMapping("/sendmessage/{title}")
	//https://stackoverflow.com/questions/47621936/how-can-i-get-the-child-values-from-firebase-database
	@RequestMapping(value ="/sendmessage/{title}", method = RequestMethod.GET)
	public String listProducts(@PathVariable String title) {
		FBMessage message = new FBMessage(title);
		SendMessage sendMessage = new SendMessage(message, fireBaseConfigr);
		DatabaseReference rootRef = fireBaseConfigr.getMessagesReference();
		DatabaseReference usersRef = rootRef.child(message.getId());

		System.out.println( "message.getId() ... " + message.getId());
		System.out.println( "getKey ... " + usersRef.getKey());

		fireBaseConfigr.getMessagesReference().child(message.getId()).setValueAsync(message);
		fireBaseConfigr.getSendMessage().put(message.getId(), sendMessage);
		System.out.println( " message.getId() --> " + message.getId() + "sendMessage -->" + sendMessage.toString() );
		taskExecutor.execute(sendMessage);
		return "Message Sending in Processing.";
	}

	@RequestMapping(value ="/sendmessage/store/{id}/{title}", method = RequestMethod.GET)
	public void listProductsIdStore(@PathVariable long id, @PathVariable String title) {

		String fcmToken;
		if(id > 0) {

			//CKS
			//User user = userModel.getById(id);
			User user = new User();
			//CK
			user.setId(null);
			fcmToken = user.getFcmToken();

			/*Map<String, String> newMap = new HashMap<String, String>();
			newMap.put("id", "1");
			newMap.put("message", " TEST SSS " + System.currentTimeMillis());
			newMap.put("title", " TEST SSS " + System.currentTimeMillis());
			newMap.put("action", "ACTION_GOT_NOTIFICATION");
			AndroidConfig androidConfig = AndroidConfig.builder().putAllData(newMap).setPriority(Priority.HIGH).build();
			Message message = Message.builder().setAndroidConfig(androidConfig).setToken(fcmToken).build();

			String response;
			try {
				response = FirebaseMessaging.getInstance().send(message);
				System.out.println("response" + response);

			} catch (FirebaseMessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			FBMessage message = new FBMessage(title);
			SendMessage sendMessage = new SendMessage(message, fireBaseConfigr);
			DatabaseReference rootRef = fireBaseConfigr.getMessagesReference();
			DatabaseReference usersRef = rootRef.child(message.getId());

			System.out.println( "message.getId() ... " + message.getId());
			System.out.println( "getKey ... " + usersRef.getKey());

			fireBaseConfigr.getMessagesReference().child(message.getId()).setValueAsync(message);
			fireBaseConfigr.getSendMessage().put(message.getId(), sendMessage);
			System.out.println( " message.getId() --> " + message.getId() + "sendMessage -->" + sendMessage.toString() );
			taskExecutor.execute(sendMessage);
			//return "Message Sending in Processing.";
		}

	}

	@RequestMapping(value ="/sendmessage/{id}/{title}", method = RequestMethod.GET)
	public void listProductsId(@PathVariable long id, @PathVariable String title) {

		String fcmToken;
		if(id > 0) {


			User user = new User();
			//CK
			user.setId(null);
			//User user = userModel.getById(id);

			fcmToken = user.getFcmToken();
			Map<String, String> newMap = new HashMap<String, String>();
			newMap.put("id", "1");
			newMap.put("message", " TEST SSS " + System.currentTimeMillis());
			newMap.put("title", " TEST SSS " + System.currentTimeMillis());
			newMap.put("action", "ACTION_GOT_NOTIFICATION");
			AndroidConfig androidConfig = AndroidConfig.builder().putAllData(newMap).setPriority(Priority.HIGH).build();
			Message message = Message.builder().setAndroidConfig(androidConfig).setToken(fcmToken).build();

			String response;
			try {
				response = FirebaseMessaging.getInstance().send(message);
				System.out.println("response" + response);

			} catch (FirebaseMessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}



	//@RequestMapping("/v2/sendmessage/{title}")
	@RequestMapping(value ="/v2/sendmessage/{title}", method = RequestMethod.GET)
	public String listProductsv2(@PathVariable String title) {
		FBMessage message = new FBMessage(title);

		//take messages from 1 drivers/passId/
		SendMessage sendMessage = new SendMessage(new ArrayList<TaxiDriver>(drivers.values()), message);
		fbMessages.put(message.getId(), message);
		sendMessages.put(message.getId(), sendMessage);
		getMessagesReference().child(message.getId()).setValueAsync(message);
		taskExecutor.execute(sendMessage);
		return "Message Sending in Processing.";

	}

	//@GetMapping("/notificationtest1")
	@RequestMapping(value ="/notificationtest1", method = RequestMethod.GET)
	public String test1() {
		//Data data = new Data();
		Map<String, String> newMap = new HashMap<String, String>();
		newMap.put("id", "1");
		newMap.put("message", " TEST SSS " + System.currentTimeMillis());
		newMap.put("title", " TEST SSS " + System.currentTimeMillis());
		newMap.put("action", "ACTION_GOT_NOTIFICATION");

		//System.out.println("Android ---------------------------------> Map map " + data.toString());

		for (Map.Entry<String, TaxiDriver> entry : fireBaseConfigr.getDrivers().entrySet()) {
			System.out.println("Storing values-->" + entry.getValue());
			AndroidConfig androidConfig = AndroidConfig.builder().putAllData(newMap).setPriority(Priority.HIGH).build();
			Message message = Message.builder().setAndroidConfig(androidConfig).setToken(entry.getValue().getFbToken())
					.build();
			System.out.println("Message-->" + message);
			String response;
			try {
				response = FirebaseMessaging.getInstance().send(message);
				System.out.println("response" + response);

			} catch (FirebaseMessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "Testing";
	}

	//@GetMapping("/notificationtest2")
	@RequestMapping(value ="/notificationtest2", method = RequestMethod.GET)
	public String test2() {
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(1000 * 15);
					test1();
					Thread.sleep(1000 * 15);
					test1();
					Thread.sleep(1000 * 15);
					test1();

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		return "Testing";
	}

	@RequestMapping(value ="/savetoken/{token}", method = RequestMethod.POST)
	public String saveToken(@PathVariable String token) {
		TOKEN = token;
		return "Token Saved";
	}

	//@PostMapping("/accept")
	@RequestMapping(value ="/accept", method = RequestMethod.POST)
	public String accept(@RequestBody AcceptRequest request) {

		System.out.println("request : --> " + request);
		System.out.println("Send Messages : " + fireBaseConfigr.getSendMessage().size());
		SendMessage sentMsg = fireBaseConfigr.getSendMessage().get(request.getMessageId());

		System.out.println(sentMsg);

		if (sentMsg != null) {
			return sentMsg.setAccept(request.getToken());
		}

		return "Error Driver or Message is not valid";
	}

	@RequestMapping(value ="/accept/{token}/{messageId}",method = RequestMethod.GET)
	public String accept(@PathVariable String token, @PathVariable String messageId) {

		FBMessage msg = fbMessages.get(messageId);
		TaxiDriver driver = drivers.get(token);
		SendMessage sentMsg = sendMessages.get(messageId);
		if (msg != null && driver != null && sentMsg != null) {
			msg.setDriverName(driver.getName());
			msg.setCarName(driver.getCarName());
			msg.setStatus("ACCEPT");
			sentMsg.setAccept(driver.getFbToken());
			getMessagesReference().child(messageId).setValueAsync(msg);
			return "Driver Accept and store in firebase Database";
		}

		return "Error Driver or Message is not valid";
	}

	//@RequestMapping("/showtoken")
	@RequestMapping(value ="/showtoken", method = RequestMethod.GET)
	public String showToken() {
		return TOKEN;
	}

}
