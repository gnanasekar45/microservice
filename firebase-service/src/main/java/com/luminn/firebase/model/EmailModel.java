package com.luminn.firebase.model;


import com.luminn.firebase.dto.MobileDTO;
import com.luminn.firebase.dto.UserDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.EmailService;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.Role;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import com.luminn.firebase.dto.ContactDTO;
import com.luminn.firebase.dto.RideDetailDTO;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.view.EmailView;
import io.netty.util.internal.ResourcesUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.print.URIException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

@Component
public class EmailModel {





	@Value("${email_verify}")
	private String emailTemplateResetPassword;


	@Value("${emailDealbid}")
	private String emailDealbid;

	@Value("${bid_offer}")
	private String emailBid;

	@Value("${ccEmail}")
	private String ccEmail;

	@Value("${sendingEmail}")
	private String sendingEmail;

	@Value("${emailKey}")
	private String emailKey;



	@Value("${emailTemplateFolder}")
	private String emailTemplateFolder;
	@Value("${email_verify}")
	private String emailTemplate;
	@Value("${email_verify_driver}")
	private String emailTemplateDriver;

	@Value("${siteUrl}")
	private String siteUrl;
	@Value("${commonEmail}")
	private String commonEmail;

	@Value("${siteFrontEnd}")
	private String siteFrontEnd;
	@Value("${siteBackEnd}")
	private String siteBackEnd;

	@Value("${contactus}")
	private String emailTemplateContact;


	@Value("${rideBill}")
	private String rideBill;

	@Autowired
	private MessageByLocaleService messageservice;

	@Autowired
	StoreModel storeModel;

	//@Value("classpath:resources/email_templates/*")
	//private Resource[] resources;

	@Autowired
	FreeMarkerConfigurer freemarkerConfig;

	@Autowired
	private ResourceLoader resourceLoader;


	@Autowired
    UserRepository userRepository;

	@Qualifier("mailJet")
	@Autowired
	MailjetClient getMailjetClient;

	private static final Logger log = LoggerFactory.getLogger(EmailModel.class);

	public void sendCCEmail(String sendFromEmail, String sendToEmail,String sendCCToEmail, String subject, String messageBody) {
		Properties props = new Properties();

		/*Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sendFromEmail));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
			msg.addRecipient(Message.RecipientType.CC, new InternetAddress(sendCCToEmail));
			msg.setSubject(subject);
			msg.setText(messageBody);
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}*/
	}
	private String getServer(String key){
		if(storeModel.findByKey(key) != null)
			return storeModel.findByKey(key);
		return "http://1-dot-taxi2deal.appspot.com";
	}



	public void sendHtmlEmailCC( Collection<EmailView> addresses,
							  String subject, String htmlBody) {

		/*Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Address[] addressArray = null;

		if (addresses != null && !addresses.isEmpty()) {
			 addressArray = new Address[addresses.size()];
			int index = 0;
			for (EmailView address : addresses) {
				addressArray[index++] = new InternetAddress(address.getDriverEmail());
			}

		}

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sendingEmail));

			msg.addRecipients(Message.RecipientType.CC,addressArray );
			msg.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}*/
	}

	public void sendEmailToDrivers(Collection<EmailView> listEmailView,Long dealId,String title) throws IOException {

		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(emailTemplateFolder));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template temp = cfg.getTemplate(emailDealbid);

		String notification  = messageservice.getMessage("bid_email_notification_gn");
		String name_gn  = messageservice.getMessage("name_gn");
		String bid_invitation_title  = messageservice.getMessage("bid_invitation_title",title);
		String bid_inviation_dealId  = messageservice.getMessage("bid_inviation_dealId",dealId);

		//log.info("notification = " +notification);

		Map root = new HashMap();
		Map latest = new HashMap();
		latest.put("name", name_gn);
		latest.put("dealId", bid_inviation_dealId);
		latest.put("title", bid_invitation_title);
		latest.put("notification", notification);


		root.put("user", latest);

		//German
		//ialy
		//url _ englis
		//log
		/* Merge data-model with template */
		Writer out = new StringWriter();
		try {
			temp.process(root, out);
			// System.out.println(out.toString());
			EmailModel emailService = new EmailModel();
			sendHtmlEmailCC(listEmailView,"toEmailId", out.toString());
			//log.info("Completed successfully ==toEmailId" + toEmailId  + "=sendingEmail=" + sendingEmail + "Price =" + bidDTO.getPrice() + "driverName=" + driverName);

			//emailService.sendEmail("chandra20@gmail.com", "hr.lumiins@gmail.com", "messageSubject", "out.toString()");
		} catch (TemplateException e) {
			log.info("inside exception" + e.getStackTrace());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.info("inside exception" + e.getStackTrace());
			e.printStackTrace();
		}
		out.flush();
	}

	public void sendEmailToDealOwner(String title,String userName,String toEmailId,Long dealId,double price ) throws IOException {

		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(emailTemplateFolder));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template temp = cfg.getTemplate(emailDealbid);

		String notification  = messageservice.getMessage("bid_email_notification",userName,price);
		log.info("notification = " +notification);

		Map root = new HashMap();
		Map latest = new HashMap();
		latest.put("name", userName);
		latest.put("notification", notification);
		latest.put("title", title);
		latest.put("dealId", dealId);

		root.put("user", latest);

		//German
		//ialy
		//url _ englis
		//log
		/* Merge data-model with template */
		Writer out = new StringWriter();
		try {
			temp.process(root, out);
			// System.out.println(out.toString());
			sendGrid(sendingEmail,toEmailId, title, out.toString());
			log.info("Completed successfully ==toEmailId=" + toEmailId  + "=sendingEmail =" + sendingEmail + "DealId ="  +  dealId + "Price =" + price + "driverName =" + userName);

			//emailService.sendEmail("chandra20@gmail.com", "hr.lumiins@gmail.com", "messageSubject", "out.toString()");
		} catch (TemplateException e) {
			log.info("inside exception" + e.getStackTrace());
			e.printStackTrace();
		}
		out.flush();
	}



	//CHECK LATER
	private String getURLRegsitration(String id, String langCode, String key) {
		//CHECK LATER
		String url = null;
		/*if(storeModel)
		  url =  storeModel.findByKey("siteFrontEnd");
		if(url!= null && !"".equalsIgnoreCase(url))
			return  url + "/public/verifycall.html?lang=" + langCode + "&id=" + id + "&restKey=" + key;
		else
			return siteUrl + "/public/verifycall.html?lang=" + langCode + "&id=" + id + "&restKey=" + key;*/
		return "test";
	}

	private String getURLForgot(String relativePath){
		//String url =  storeModel.findByKey("siteFrontEnd");
		String url =  "https://taxideals.in";
		if(url!= null && !"".equalsIgnoreCase(url))
			return  url + relativePath;
		else
			return siteUrl + relativePath;

	}


	public void selectAndSendEmail(String name,String toEmailId, String messageSubject, String templateName,String role,String language,String passwordResetKey,String userKeys) throws IOException {

		String verifyUrl = null;
		Template temp2 = null;
		//Configuration cfg = new Configuration();
		//cfg.setDirectoryForTemplateLoading(new File(emailTemplateFolder));
		//cfg.setObjectWrapper(new DefaultObjectWrapper());

		Configuration cfg =	freemarkerConfig.getConfiguration();
		cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		System.out.println(" templateName --->>>" + templateName);
		Template temp = cfg.getTemplate(templateName);
        //if(cfg != null)
		// temp = cfg.getTemplate(templateName);



		if("".equals(verifyUrl) || verifyUrl == null) {
			verifyUrl = getURLRegsitration(userKeys, language, passwordResetKey);
		}

		String paste  = messageservice.getMessage("copy_paste");
		String registration  = messageservice.getMessage("registration",toEmailId);
		String link  = messageservice.getMessage("link");

		Map root = new HashMap();
		Map latest = new HashMap();

		latest.put("registrationdetails", messageservice.getMessage("registrationdetails"));
		latest.put("dear", messageservice.getMessage("dear"));
		latest.put("name", name);
		latest.put("email", toEmailId);
		latest.put("verifyUrl", verifyUrl);
		latest.put("paste", paste);
		latest.put("registration", registration);
		latest.put("link", link);
		latest.put("thankyou",  messageservice.getMessage("thankyou"));


		if(role.equals(Path.ROLE.DRIVER)){
			latest.put("documents",messageservice.getMessage("documents") );
			//latest.put("link", link);
		}
		if(role.equals(Path.ROLE.DELIVERY)){
			latest.put("documents",messageservice.getMessage("documents") );
			//latest.put("link", link);
		}
		else{
			latest.put("documents",messageservice.getMessage("documents") );
		}

		root.put("user", latest);

		log.info("=registration" + registration);


		//German
		//ialy
		//url _ englis
		//log
		/* Merge data-model with template */
		Writer out = new StringWriter();
		try {
			temp.process(root, out);
			sendGrid(sendingEmail,toEmailId, messageSubject, out.toString());
			log.info("Completed successfully");
			//emailService.sendEmail("chandra20@gmail.com", "hr.lumiins@gmail.com", "messageSubject", "out.toString()");
		} catch (TemplateException e) {
			log.info("inside exception" + e.getStackTrace());
			e.printStackTrace();
		}
		log.info("name="+name + "toEmailId=" + toEmailId + "registration=" + registration + "verifyUrl=" + verifyUrl);
		out.flush();
	}

	public void selectAndSendEmailwithForgot(String name,String toEmailId, String messageSubject, String templateName,String verifyEmail,String passwordResetKey) throws IOException{

		String relativePath = "/public/changePassword.html?email="+verifyEmail+"&resetKey="+passwordResetKey;
		selectAndSendEmail(name,toEmailId,getURLForgot(relativePath),messageSubject,templateName);

	}
	public void selectAndSendEmail(String name,String toEmailId, String verifyUrl, String messageSubject, String templateName) throws IOException{


		Configuration cfg =	freemarkerConfig.getConfiguration();
		cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template temp = cfg.getTemplate(templateName);

		/* Create a data-model */
		Map root = new HashMap();
		Map latest = new HashMap();
		latest.put("name", name);
		latest.put("email", toEmailId);
		latest.put("verifyUrl", verifyUrl);

		System.out.println(" == verifyUrl ==" + verifyUrl);

		String registrationdetails = messageservice.getMessage("registrationdetails");
		String registration = messageservice.getMessage("registration");
		String dear = messageservice.getMessage("dear");
		String link = messageservice.getMessage("link");
		String copy_paste = messageservice.getMessage("copy_paste");
		String thankyou = messageservice.getMessage("thankyou");


		latest.put("registrationdetails",registrationdetails);
		latest.put("registrationdetails",registrationdetails);
		latest.put("registration",registration);
		latest.put("dear",dear);
		latest.put("name",dear);
		latest.put("link",link);
		latest.put("paste",copy_paste);
		//latest.put("verifyUrl","verifyUrl");
		latest.put("thankyou",thankyou);

		root.put("user", latest);

		/* Merge data-model with template */
		Writer out = new StringWriter();
		try {
			temp.process(root, out);
			// System.out.println(out.toString());
			sendGrid(sendingEmail,toEmailId, messageSubject, out.toString());
			//emailService.sendEmail("chandra20@gmail.com", "hr.lumiins@gmail.com", "messageSubject", "out.toString()");
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		out.flush();
	}

	public File readFolderFromJar()  throws Exception {
		Resource rs = resourceLoader.getResource("classpath:resources/email_templates");
		return rs.getFile();
	}

     //https://stackoverflow.com/questions/31117107/how-to-read-freemarker-template-files-from-src-main-resources-folder

    public  Configuration getTemplateName(){
        Configuration cfg =	freemarkerConfig.getConfiguration();
        cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        return cfg;
    }

	@Autowired
	JavaMailSender javaMailSender;

	public String selectAndSendEmail(ContactDTO contactDto) throws IOException{

    	Configuration cfg =	freemarkerConfig.getConfiguration();
			//Configuration cfg = new Configuration();
            //cfg.setDirectoryForTemplateLoading(ResourcesUtil.getFile(this.getClass().getResource("/contract.json"));
            //cfg.setDirectoryForTemplateLoading(ResourcesUtil.getFile("classpath:resources/email_templates",""));

        cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	    Template temp = cfg.getTemplate(emailTemplateContact);

		ArrayList<String> a1 = new ArrayList<String>();
		a1.add(contactDto.getName());
		a1.add(contactDto.getEmail());
		a1.add(contactDto.getPhonenumber());
		a1.add(contactDto.getMessage());
		a1.add(contactDto.getSource());
		a1.add(contactDto.getDestination());
		a1.add(contactDto.getStartDate().toString());
		a1.add(contactDto.getJob());


		/*Create a data-model*/
		Map root = new HashMap();
		Map latest = new HashMap();

		latest.put("name", a1.get(0));
		latest.put("email", a1.get(1));
		latest.put("phonenumber", a1.get(2));
		latest.put("message", a1.get(3));
		latest.put("source", a1.get(4));
		latest.put("destination", a1.get(5));
		latest.put("date", a1.get(6));
		latest.put("job", a1.get(7));

		root.put("user", latest);

		/* Merge data-model with template */
		//must store in DB
		//toEmail

		log.info(" before source and destination -->" + contactDto.getSource() + " destination" + contactDto.getDestination());

		Writer out = new StringWriter();



		try {
			temp.process(root, out);
			//System.out.println(out.toString());
			//EmailService emailService = new EmailService();
			SendGrid.Response response=	sendGridContact(contactDto.getSenderMail(),contactDto.getEmail(), contactDto.getSubject(), out.toString());
			//JavaMailContact(contactDto.getSenderMail(),contactDto.getEmail(), contactDto.getSubject(), out.toString());
			emailJet(contactDto.getSenderMail(),contactDto.getEmail(), contactDto.getSubject(), out.toString());

		} catch (TemplateException e) {
			e.printStackTrace();
		}

		//out.flush();
		return "Success";
	}

	public void send(String from, String to, String subject, String body) throws MessagingException {
		final JavaMailSenderImpl javaMailSender = getJavaMailSender();
		Properties prop = javaMailSender.getJavaMailProperties();
		Session session = Session.getInstance(prop, null);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(javaMailSender.getUsername()));
		msg.setSubject(subject);
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

		MimeMultipart multipart = new MimeMultipart("related");
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(body, "text/html");
		multipart.addBodyPart(messageBodyPart);
		msg.setContent(multipart);
		msg.setSentDate(new Date());
		javaMailSender.send(msg);
	}


	private JavaMailSenderImpl getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("taxideals.ch@gmail.com");
		mailSender.setPassword("KOTLIN*developers@12345");
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.timeout", 60000);
		props.put("mail.smtp.connectiontimeout", 60000);
		props.put("mail.smtp.writetimeout", 60000);
		return mailSender;
	}

	public void selectAndSendEmail(RideDetailDTO rideDetailDTO) throws IOException{


		Configuration cfg =	freemarkerConfig.getConfiguration();
		//Configuration cfg = new Configuration();
		//cfg.setDirectoryForTemplateLoading(ResourcesUtil.getFile(this.getClass().getResource("/contract.json"));
		//cfg.setDirectoryForTemplateLoading(ResourcesUtil.getFile("classpath:resources/email_templates",""));

		cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template temp = cfg.getTemplate(rideBill);

		ArrayList<String> a1 = new ArrayList<String>();


		if(rideDetailDTO.getUserName() != null)
			a1.add(rideDetailDTO.getUserName());
		else
			a1.add("User");
		a1.add(rideDetailDTO.getUserEmailId());
		a1.add(String.valueOf(rideDetailDTO.getUserTotalPrice()));

		if(rideDetailDTO.getSource() != null)
			a1.add(rideDetailDTO.getSource());
		else
			a1.add(" source");
		a1.add(rideDetailDTO.getDestination());



		/*Create a data-model*/
		Map root = new HashMap();
		Map latest = new HashMap();

		latest.put("name", a1.get(0));
		latest.put("email", a1.get(1));
		latest.put("totalAmount", a1.get(2));
		latest.put("source", a1.get(3));
		latest.put("destination", a1.get(4));

		root.put("user", latest);

		/* Merge data-model with template */
		//must store in DB
		//toEmail

		Writer out = new StringWriter();
		try {
			temp.process(root, out);
			//System.out.println(out.toString());
			EmailService emailService = new EmailService();

			sendGridContact(rideDetailDTO.getUserEmailId(),rideDetailDTO.getUserEmailId(),"Invoice", out.toString());
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		System.out.println(" ---");
		out.flush();
	}
	 private void sendGrid(String sendFromEmail, String sendToEmail,String subject, String htmlBody){

		 SendGrid sendgrid = new SendGrid(emailKey);
		 SendGrid.Email email = new SendGrid.Email();
		 email.addTo(sendToEmail);
		 email.addCc(ccEmail);
		 email.setFrom(sendingEmail);
		 email.setBcc(new String[]{"taxideals.ch@gmail.com"});
		 email.setSubject(subject);
		 email.setHtml(htmlBody);


		 try {
			 SendGrid.Response response = sendgrid.send(email);
			 if (response.getCode() != 200) {
				 log.info("An error occured: %s"+response.getMessage());
				 return;
			 }

			 log.info("Email sent.");
		 } catch (SendGridException e) {
			 log.info("ndGrid error" + e );
			 //throw new ServletException("SendGrid error", e);
		 }
	 }


	private void emailJet(String sendFromEmail, String sendToEmail,String subject, String htmlBody)  {


		MailjetRequest request;
		MailjetResponse response;

		try {
			//client = new MailjetClient(emailJetApiKey,
			//		emailJetSecretKey, new ClientOptions("v3.1"));
			request = new MailjetRequest(Emailv31.resource)
					.property(Emailv31.MESSAGES, new JSONArray()
							.put(new JSONObject()
									.put(Emailv31.Message.FROM, new JSONObject()
											.put("Email", "admin@taxideals.ch")
											//.put("Email", "admin@taxideals.ch")
											.put("Name", "Taxideals"))
									.put(Emailv31.Message.TO, new JSONArray()
											.put(new JSONObject()
													.put("Email", sendToEmail)
													.put("Name", "Taxideals")))
									.put(Emailv31.Message.SUBJECT, subject)
									.put(Emailv31.Message.TEXTPART, htmlBody)
									.put(Emailv31.Message.HTMLPART, htmlBody)
									.put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
			response = getMailjetClient.post(request);
		}
		catch(MailjetSocketTimeoutException mailjetSocketTimeoutException){
			log.error(mailjetSocketTimeoutException.getMessage());
		}
		catch(MailjetException mailjetException){
			log.error(mailjetException.getMessage());
		}
	}

	private SendGrid.Response sendGridContact(String userwhooRequested,String driverMailId,String subject, String htmlBody){

		SendGrid sendgrid = new SendGrid(emailKey);
		SendGrid.Email email = new SendGrid.Email();
		SendGrid.Response response = null;
		//
		email.addTo(userwhooRequested);
		email.setFrom(sendingEmail);
		email.setBcc(new String[]{"admin@taxideals.ch",driverMailId});
		email.setSubject(subject);
		email.setHtml(htmlBody);


		try {
			response = sendgrid.send(email);
			if (response.getCode() != 200) {
				log.info("An error occured: %s"+response.getMessage());
				return response;
			}
			log.info("Email sent.");
		} catch (SendGridException e) {
			log.info("ndGrid error" + e );
			//throw new ServletException("SendGrid error", e);
			return response;
		}
		return response;
	}


    public ModelStatus getEmailResponse(String userKeys, MobileDTO dto, String userTemplate, String verifyUrl, ModelStatus taxiStatus) throws Exception {

	    User user = null;
        if (userKeys != null) {

            Optional<User> userOPt = userRepository.findById(userKeys);
            // verifyUrl = getURLRegsitration(userKeys, messageservice.getMessage("code"), user.getPasswordResetKey());

            if(userOPt.isPresent())
                user = userOPt.get();

            if (dto.getRole().equalsIgnoreCase(Role.ROLE_USER) || dto.getRole().equalsIgnoreCase(Role.ROLE_SUPPLIER)
                    || dto.getRole().equalsIgnoreCase(Role.ROLE_USER_IT))
                userTemplate = emailTemplate;
            if (dto.getRole().equalsIgnoreCase(Role.ROLE_DRIVER) ||
                    dto.getRole().equalsIgnoreCase(Role.ROLE_DELIVERY) ||
                    dto.getRole().equalsIgnoreCase(Role.ROLE_ADMIN))
                userTemplate = emailTemplateDriver;
            if ("".equals(dto.getFirstName())) {
                String email = dto.getEmail();
                String state = email.substring(email.indexOf("@"));
                dto.setFirstName(state);
            }
            if (userTemplate != null) {
                selectAndSendEmail(dto.getFirstName(), dto.getEmail(), messageservice.getMessage("verify"), userTemplate, dto.getRole(),messageservice.getMessage("code"),
                        user.getPasswordResetKey(),userKeys);
                return checkRole(dto.getRole());
            }
            else {
                log.info("NO ROLE IS DEFINED");
                checkRole(dto.getRole());
            }
        }
        return checkRole(dto.getRole());
    }

    private ModelStatus checkRole(String dto){
        if (dto.equalsIgnoreCase(Path.ROLE.DRIVER)) {
            return ModelStatus.DRIVER_CREATED;
        }  if (dto.equalsIgnoreCase(Path.ROLE.SUPPLIER)) {
            return ModelStatus.SUPPLIER_CREATED;
        }   if (dto.equalsIgnoreCase(Path.ROLE.USER)) {
            return ModelStatus.USER_CREATED;
        }
        if (dto.equalsIgnoreCase(Path.ROLE.USER_IT)) {
            return ModelStatus.USER_CREATED;
        }
        return ModelStatus.USER_CREATED;
    }
}
