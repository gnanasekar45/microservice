package com.luminn.firebase.model;

import com.luminn.firebase.dto.PhoneBookingDTO;
import com.luminn.firebase.dto.PhoneBookingSMS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class GupShupTemplate {


    public static String bookRideOTP(PhoneBookingDTO requestBody, String userDriverPhoneNumber, String txt){

        String message = "Default";

        //String pin =  generatePIN(requestBody.getPhoneNumber(), SMSType.RIDE);
        try {

            //String txt =  "Your Ride OTP is :" + "@__"+pin+"__@"+ "\n"  + "Valid for 20 mins, TaxiDeals Team.";


            Date mydate = new Date(System.currentTimeMillis());
            String data = "";
            data += "method=sendMessage";

            //data += "&userid= "; // your loginId
            data += "&userid=2000184739";
            data += "&password=" + URLEncoder.encode("ysVsNA", "UTF-8"); // your password
            data += "&msg=" + URLEncoder.encode(txt, "UTF-8");
            data += "&send_to=" + URLEncoder.encode(userDriverPhoneNumber, "UTF-8"); // a valid 10 digit phone no.
            data += "&v=1.1" ;
            data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
            data += "&auth_scheme=PLAIN";
            data += "&mask=lumiin";
            System.out.println("data -->>" + data);
            URL url = new URL("http://enterprise.smsgupshup.com/GatewayAPI/rest?" + data);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer buffer = new StringBuffer(); while ((line = rd.readLine()) != null){
                message = buffer.append(line).append("\n").toString();
            }
            System.out.println(buffer.toString());
            rd.close();
            conn.disconnect();
            return message;
        }
        catch(Exception e){ e.printStackTrace();
        }
        return message;
    }

    public static String bookRideOTP(PhoneBookingSMS requestBody, String userDriverPhoneNumber, String txt){

        String message = "Default";

        //String pin =  generatePIN(requestBody.getPhoneNumber(), SMSType.RIDE);
        try {

            //String txt =  "Your Ride OTP is :" + "@__"+pin+"__@"+ "\n"  + "Valid for 20 mins, TaxiDeals Team.";


            Date mydate = new Date(System.currentTimeMillis());
            String data = "";
            data += "method=sendMessage";

            //data += "&userid= "; // your loginId
            data += "&userid=2000184739";
            data += "&password=" + URLEncoder.encode("ysVsNA", "UTF-8"); // your password
            data += "&msg=" + URLEncoder.encode(txt, "UTF-8");
            data += "&send_to=" + URLEncoder.encode(userDriverPhoneNumber, "UTF-8"); // a valid 10 digit phone no.
            data += "&v=1.1" ;
            data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
            data += "&auth_scheme=PLAIN";
            data += "&mask=lumiin";
            System.out.println("data -->>" + data);
            URL url = new URL("http://enterprise.smsgupshup.com/GatewayAPI/rest?" + data);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer buffer = new StringBuffer(); while ((line = rd.readLine()) != null){
                message = buffer.append(line).append("\n").toString();
            }
            System.out.println(buffer.toString());
            rd.close();
            conn.disconnect();
            return message;
        }
        catch(Exception e){ e.printStackTrace();
        }
        return message;
    }
}
