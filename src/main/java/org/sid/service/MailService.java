package org.sid.service;

import org.sid.exceptions.SpringRedditException;
import org.sid.model.NotificationEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailContentBuilder mailContentBuilder;
    
    @Async
    public void sendEmail(NotificationEmail notificationEmail) {
	MimeMessagePreparator messagePreparator = mimeMessage -> {
	    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	    messageHelper.setFrom("springRedditRebot@gmail.com");
	    messageHelper.setTo(notificationEmail.getRecipient());
	    messageHelper.setSubject(notificationEmail.getSubject());
	    messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
	};
	try {
	    mailSender.send(messagePreparator);
	    log.info("Activation email sent!!");
	}catch(MailException e) {
	    throw new SpringRedditException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
	}
    }
}
