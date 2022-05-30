package com.luminn.firebase.controller;

import com.luminn.firebase.model.*;
import com.luminn.view.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luminn.firebase.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	//https://stackoverflow.com/questions/60131380/can-a-message-be-sent-to-a-batch-of-tokens-to-fcm-clients-using-the-firebase-adm
	@Autowired
	private NotificationService notificationService;


    @Autowired
    NotificationModel notificationModel;

	@PostMapping("/v1/todevice")
	public ResponseEntity<StatusResponse> sendToDeviceOne(@RequestBody PushNotificationRequests request) {

		//String response = this.notificationService.sendPnsToDevice(request);
		StatusResponse sr = new StatusResponse();
		String response = notificationModel.add(request);

		if (response != null) {
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);
		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("/todevice")
	public ResponseEntity<NotificationResponse> sendToDevice(@RequestBody PushNotificationRequests request) {

	    //String response = this.notificationService.sendPnsToDevice(request);
        String response = notificationModel.add(request);

		if (response != null) {
			return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/tripId/todevice")
	public ResponseEntity<NotificationResponse> sendTripToDevice(@RequestBody PushNotificationRequests request) {
		String response = this.notificationModel.addMultiple(request);
		if (response != null) {
			return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/tripId/all/todevice")
	public ResponseEntity<StatusResponse> sendTripToDeviceAll(@RequestBody PushNotificationRequests request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})
		StatusResponse sr = new StatusResponse();
		String response = notificationModel.add(request);
		if (response != null) {
			//return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);

		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
		//return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/app/v2/tripId/multiple/user/notice/todevice")
	public ResponseEntity<StatusResponse> sendNoticeToDeviceMultiple(@RequestBody PushPhoneNotificationRequest request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})


		StatusResponse sr = new StatusResponse();
		String response = notificationModel.addMultipleNotice(request);
		if (response != null) {
			//return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);

		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
		//return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/tripId/all/user/notice/todevice")
	public ResponseEntity<StatusResponse> sendNoticeToDeviceUserAll(@RequestBody PushPhoneNotificationRequest request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})


		StatusResponse sr = new StatusResponse();
		String response = notificationModel.addNotice(request);
		if (response != null) {
			//return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);

		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
		//return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/tripId/all/user/notice/all/todevice")
	public ResponseEntity<StatusResponse> sendNoticeToDeviceUserAlles(@RequestBody PushPhoneNotificationRequest request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})


		StatusResponse sr = new StatusResponse();
		String response = notificationModel.addNoticeAll(request);
		if (response != null) {
			//return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);

		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
		//return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	/*@PostMapping("/tripId/all/user/todevice")
	public ResponseEntity<StatusResponse> sendTripToDeviceUserAll(@RequestBody PushUserNotificationRequests request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})
		StatusResponse sr = new StatusResponse();
		String response = notificationModel.add(request);
		if (response != null) {
			//return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);

		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
		//return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}*/

	@PostMapping("/tripId/all/user/back/todevice")
	public ResponseEntity<StatusResponse> sendTripToDeviceUserAll2(@RequestBody PushUserNotificationRequests request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})
		StatusResponse sr = new StatusResponse();
		String response = notificationModel.addbackGround(request);
		if (response != null) {
			//return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);

		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
		//return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("v1/tripId/all/user/back/todevice")
	public ResponseEntity<StatusResponse> sendTripToDeviceUserCoupan(@RequestBody UserNotificationRequests request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})
		StatusResponse sr = new StatusResponse();
		String response = notificationModel.addbackGroundCoupan(request);
		if (response != null) {
			//return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
			sr.setStatus(true);
			sr.setMessage("send notification successfully!");
			sr.setData("send notification successfully!");
			return new ResponseEntity<>(sr, HttpStatus.OK);

		}
		else {
			sr.setStatus(false);
			sr.setMessage("while send notification, it's failed!");
			sr.setData("while send notification, it's failed!");
			return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
		}
		//return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/tripId/confirmation/todevice")
	public ResponseEntity<NotificationResponse> sendTripToDeviceAll(@RequestBody ConfirmationNotificationResponse request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})
		String response = notificationModel.addConfirmation(request);
		if (response != null) {
			return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/tripId/review/todevice")
	public ResponseEntity<NotificationResponse> sendTripToDeviceAll(@RequestBody ReviewNotificationResponse request) {
		//CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
		// userId: string, km: 44, body: string, dest: zurich, content: string}})
		String response = notificationModel.tripReview(request);
		if (response != null) {
			return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/totopic")
	public ResponseEntity<NotificationResponse> sendToTopic(@RequestBody PushNotificationRequest request) {
		String response = this.notificationService.sendPnsToTopic(request);
		if (response != null) {
			return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
	}
}
