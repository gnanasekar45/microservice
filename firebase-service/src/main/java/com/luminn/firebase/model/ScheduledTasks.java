package com.luminn.firebase.model;

import com.luminn.firebase.util.DateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;


@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //@Scheduled(fixedRate = 5000000)
    //5 am in morning
    @Scheduled(cron = "0 0 5 * * ?")
    public void reportCurrentTime()  {

        //FindIterable<Document> docs = mongoTemplate.getCollection("contactMail").find()
        //log.info("The time is now {}", dateFormat.format(new Date()));
        //https://webdevdesigner.com/q/java-mongodb-query-by-date-100109/

        SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //formattedDate.setTimeZone(TimeZone.getTimeZone("UTC"));

        //System.out.println(formattedDate.format(date));

        String date = DateUtil.getCurrentDate(1).toString();
        String data1 =DateUtil.getCurrentDate(2).toString();

        log.info(" date " +date);
        /*Date startDate = null;
        Date endDate = null;
            try {
                 startDate = formattedDate.parse(date);
                 endDate = formattedDate.parse(data1);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/



        //BasicDBObject query = new BasicDBObject("startDate",
         //       new BasicDBObject("$gte",startDate).append("$lt",startDate ));
        //log.info(" startDate " + startDate);
        BasicDBObject query = new BasicDBObject("startDate",
               new BasicDBObject("$gte",DateUtil.getCurrentDate(1)));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("contactMail").find(query);

        log.info(" runnung " + findIterable.first());

       /* BasicDBObject queryObj = new BasicDBObject();
        //queryObj.put("driverId", new ObjectId(driverId)); $lte "$gte"
        queryObj.put("startDate", new Date());
        final BasicDBObject query = new BasicDBObject("startDate", new BasicDBObject("$gte", new Date()));

        FindIterable<Document> findIterable = mongoTemplate.getCollection("contactMail").find(queryObj);*/
    }
}
