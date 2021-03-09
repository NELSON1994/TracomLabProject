package com.tracom.atlas.service;

import com.tracom.atlas.wrapper.IsInitiatorResonseEntity;
import com.tracom.atlas.wrapper.IsInitiatorWrapper;
import com.tracom.atlas.wrapper.LogExtras;
import com.tracom.atlas.wrapper.LogWrapper;
import ke.axle.chassis.utils.LoggerService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoggerServiceTemplate implements LoggerService,LoggerServiceExtension  {
    @Autowired
    LogExtras extras;
    @Value("${baseUrlLoggerService}")
    private String url;

    @Override
    public void log(String description, String entity, Object entityId, String activity, String activityStatus, String notes) {
        RestTemplate temp = new RestTemplate();

        var entityHolder = (entityId == null) ? null : entityId.toString();

        //       create log object for posting
        LogWrapper log = new LogWrapper();
        log.setDescription(description);
        log.setEntityName(entity);
        log.setEntityId(entityHolder);
        log.setActivityType(activity);
        log.setStatus(activityStatus);
        log.setNotes(notes);

        log.setSource(extras.getSource());
        log.setIpAddress(extras.getIpAddress());
        log.setClientId(extras.getClientId());
        log.set_userId(extras.getUserId());

//        post log to logger service
        System.out.println("SENDING LOGGING REQUEST ..." + log.toString());
        ResponseEntity res = null;
        try {
            res = temp.postForEntity(url + "ufs-logger-service/api/v1/logger/log", log, LogWrapper.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Logger service is unreachable ...");
        }
        System.out.println("REQUEST  RESPONSE =====>>>> " + res.getStatusCode());

    }

    //    used when user id is already provided by chassis
    @Override
    public void log(String description, String entity, Object entityId, Long userId, String activity, String activityStatus, String notes) {
        RestTemplate temp = new RestTemplate();
        //       create log object for posting

        var entityHolder = (entityId == null) ? null : entityId.toString();
        LogWrapper log = new LogWrapper();
        log.setDescription(description);
        log.setEntityName(entity);
        log.setEntityId(entityHolder);
        log.setActivityType(activity);
        log.setStatus(activityStatus);
        log.setNotes(notes);

        log.setSource(extras.getSource());
        log.setIpAddress(extras.getIpAddress());
        log.setClientId(extras.getClientId());
        log.set_userId(userId);

//        post log to logger service
        System.out.println("SENDING LOGGING REQUEST ..." + log.toString());
        ResponseEntity res = null;
        try {
            System.out.println("User Id========>"+log.get_userId());
            System.out.println("Activity========>"+log.getActivityType());
            res = temp.postForEntity(url + "ufs-logger-service/api/v1/logger/log", log, LogWrapper.class);

        } catch (HttpClientErrorException e) {
            System.out.println("Logger sservice is unreachable ...");
        }
        System.out.println("REQUEST  RESPONSE =====>>>> " + res.getStatusCode());
    }

    @Override
    public boolean isInitiator(String Entity, Object entityId, String activity) {
        RestTemplate temp = new RestTemplate();
        List<IsInitiatorWrapper> payload = new ArrayList<>();
//       create isinitaitor object for posting
        IsInitiatorWrapper ismaker = new IsInitiatorWrapper();
        ismaker.setUserId(new BigDecimal(extras.getUserId()));
        ismaker.setEntity(Entity);
        ismaker.setEntityId(entityId.toString());
        ismaker.setActivity(activity);
        payload.add(ismaker);

//        post payload to logger service
        System.out.println(" SENDING IS INITIATOR REQUEST ... " + payload.toString());

        IsInitiatorResonseEntity res = temp.postForObject(url + "ufs-logger-service/api/v1/logger/log/is-initiator", payload, IsInitiatorResonseEntity.class);
        boolean result = (res.getData().getAllowewd().size() > 0) ? false : true;
        System.out.println("ALLOWED IDS" + res.getData().getAllowewd());
        System.out.println("DIS-ALLOWED IDS" + res.getData().getNotAllowed());

        return result;
    }

}
