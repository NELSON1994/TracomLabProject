package com.tracom.atlas.service;

import com.tracom.atlas.wrapper.EmailBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author eli.muraya
 */
@Service
public class NotifyServiceTemplate implements NotifyService {

    @Autowired
    CommunicationService commService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void sendEmail(String emailAddress, String title, String message) {
        System.out.println("SENDING EMAIL=====================================>>>>>>");
        EmailBody email = new EmailBody();
        email.setSubject(title);
        email.setMessage(message);
        email.setSendTo(emailAddress);

        log.info("Sending email notification to remote client (Email Address: {}, Title: {}, Message: {})", emailAddress, title, message);
        // TODO Auto-generated method stub
        commService.sendEmail(email);

    }

    @Override
    public void sendEmailWithAttachment(String emailAddress, String title, String message, String pathToAttachment) {
        System.out.println("SENDING EMAIL=====================================>>>>>>");
        EmailBody email = new EmailBody();
        email.setSubject(title);
        email.setMessage(message);
        email.setSendTo(emailAddress);
        email.setPathToAttachment(pathToAttachment);


        log.info("Sending email notification to remote client (Email Address: {}, Title: {}, Message: {})", emailAddress, title, message);
        // TODO Auto-generated method stub
        commService.sendEmailWithAttachment(email);
    }

}
