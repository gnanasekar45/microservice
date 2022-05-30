package com.luminn.firebase.controller;

import com.luminn.firebase.dto.ContactDTO;

import com.luminn.firebase.model.ContactModel;
import com.luminn.firebase.model.EmailModel;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/contact")
public class ContactusController {



	@Autowired
	EmailModel emailModel;

	@Autowired
	ContactModel contactModel;


	@ResponseBody
	@RequestMapping(value = Path.Url.APP +  "/mail" +Path.OperationUrl.CREATE, method = RequestMethod.POST)
	public ResponseEntity<StatusResponse> contactusMail(@RequestBody ContactDTO contactDTO) throws IOException {

		ArrayList<String> a1 = new ArrayList<String>();
		a1.add(contactDTO.getName());
		a1.add(contactDTO.getEmail());
		a1.add(contactDTO.getPhonenumber());
		a1.add(contactDTO.getMessage());
		a1.add(contactDTO.getSource());
		a1.add(contactDTO.getDestination());
		a1.add(contactDTO.getJob());

		try {
			emailModel.selectAndSendEmail(contactDTO);
		}

		catch (IOException e){
			System.out.println(" " + e.getMessage());
		}
		contactModel.create(contactDTO);
		StatusResponse sr = new StatusResponse();
		sr.setData("Thank You for your valuable Comments :");
		sr.setStatus(true);
		//sr.setInfoId(ErrorNumber.CONTACT_US_MAIL);
		sr.setMessage("SUCCESS");
		return new ResponseEntity<>(sr, HttpStatus.OK);

	}

	@ResponseBody
	@RequestMapping(value = Path.Url.APP +  "/mail" +Path.OperationUrl.GETALL, method = RequestMethod.POST)
	public ResponseEntity<StatusResponse> getAllMail(@RequestBody ContactDTO contactDTO) throws IOException {

		ArrayList<String> a1 = new ArrayList<String>();
		a1.add(contactDTO.getName());
		a1.add(contactDTO.getEmail());
		a1.add(contactDTO.getPhonenumber());
		a1.add(contactDTO.getMessage());

		//emailModel.selectAndSendEmail(contactDTO);

		StatusResponse sr = new StatusResponse();
		sr.setData("Thank You for your valuable Comments :");
		sr.setStatus(true);
		sr.setInfoId(ErrorNumber.CONTACT_US_MAIL);
		sr.setMessage("SUCCESS");
		sr.setData(contactModel.get());
		return new ResponseEntity<>(sr, HttpStatus.OK);

	}

	@ResponseBody
	@RequestMapping(value = Path.Url.APP +  "/mail/{supplierId}", method = RequestMethod.GET)
	public ResponseEntity<StatusResponse> getAllTaxi(@PathVariable("supplierId") String supplierId) throws IOException {


		//emailModel.selectAndSendEmail(contactDTO);

		StatusResponse sr = new StatusResponse();
		sr.setData("Thank You for your valuable Comments :");
		sr.setStatus(true);
		sr.setInfoId(ErrorNumber.CONTACT_US_MAIL);
		sr.setMessage("SUCCESS");
		sr.setData(contactModel.getMailBySupplier(supplierId));
		return new ResponseEntity<>(sr, HttpStatus.OK);

	}

	@ResponseBody
	@RequestMapping(value = Path.Url.APP +  "/phonenumber/{phonenumber}", method = RequestMethod.GET)
	public ResponseEntity<StatusResponse> getPhoneAllTaxi(@PathVariable("phonenumber") String phonenumber) throws IOException {


		//emailModel.selectAndSendEmail(contactDTO);

		StatusResponse sr = new StatusResponse();
		sr.setData("Thank You for your valuable Comments :");
		sr.setStatus(true);
		sr.setInfoId(ErrorNumber.CONTACT_US_MAIL);
		sr.setMessage("SUCCESS");
		sr.setData(contactModel.getMailByPhoneNumber(phonenumber));
		return new ResponseEntity<>(sr, HttpStatus.OK);

	}

	@ResponseBody
	@RequestMapping(value = Path.Url.APP +  "/email/{mail}", method = RequestMethod.GET)
	public ResponseEntity<StatusResponse> getmailAllTaxi(@PathVariable("mail") String mail) throws IOException {


		//emailModel.selectAndSendEmail(contactDTO);

		StatusResponse sr = new StatusResponse();
		sr.setData("Thank You for your valuable Comments :");
		sr.setStatus(true);
		sr.setInfoId(ErrorNumber.CONTACT_US_MAIL);
		sr.setMessage("SUCCESS");
		sr.setData(contactModel.getMailByMail(mail));
		return new ResponseEntity<>(sr, HttpStatus.OK);

	}



	
}