package com.backup.reports.services;

import com.backup.reports.database.MailManagerSchema;
import com.backup.reports.util.AdminData;
import com.backup.reports.util.ClientAddress;
import com.backup.reports.util.MailLogger;
import com.backup.reports.util.MailMessage;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class MailSender {

   private static Properties props = new Properties();
   private static String password;
   private static String emailAddress;
   private final static Logger logger = Logger.getLogger(MailSender.class);

    public MailSender() {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        //get mail from the database
        setCredentials();

    }

    public synchronized boolean sendMail(String subject, String body, String recipient, boolean hasAttachment, String attachmentPath){
        /*Logging++++++++++++++++++++++++++++++++++++++++*/
        String path = "C:/Users/"+System.getProperty("user.name")+"/Documents/SentBackupInventory/"+attachmentPath;
        MailLogger mailLogger = new MailLogger();
        mailLogger.setRecipient(recipient); mailLogger.setClientMachine(getClientMachineLog(recipient)); mailLogger.setSubject(subject);
        mailLogger.setMessage(getMessageIdLog(subject)); mailLogger.setInventory(attachmentPath);
        try {
            MDC.put("recipient",mailLogger.getRecipient());
            MDC.put("clientMachine",mailLogger.getClientMachine());
            MDC.put("subject",mailLogger.getSubject());
            MDC.put("message",mailLogger.getMessage());
            MDC.put("inventory",mailLogger.getInventory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*end of log creation++++++++++++++++++++++++++++++++*/

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new
                PasswordAuthentication(emailAddress, password);
            }
        });
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(password, false));
            msg.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(recipient));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");
            msg.setSentDate(new Date());
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            if (hasAttachment) {
                messageBodyPart.setContent(body, "text/html");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(path);
                multipart.addBodyPart(attachPart);
                msg.setContent(multipart);
            }
            Transport.send(msg);
            logger.info("Sent");
            return true;
        } catch (MessagingException  | IOException e) {
            e.printStackTrace();
            logger.warn("Failed");
            logger.error("Error executing mail send"+ e.getMessage());
            return false;
        }

    }

    private void setCredentials(){
        MailManagerSchema mailManagerSchema = new MailManagerSchema();
        AdminData adminData = mailManagerSchema.getAdmin();
        password = adminData.getAdminPassword();
        emailAddress = adminData.getUserName();
    }
    private String getClientMachineLog(String recipient){
        List<ClientAddress> clientAddressList = new MailManagerSchema().getAllAddresses();
        for (ClientAddress clientAddress : clientAddressList){
            if (clientAddress.getEmail().equalsIgnoreCase(recipient)){
                return clientAddress.getClientMachine();
            }
        }
        return "Unknown";
    }

    private String getMessageIdLog(String subject){
        List<MailMessage> mailMessageList = new MailManagerSchema().getAllMailMessage();
        for (MailMessage mailMessage : mailMessageList){
            if (mailMessage.getSubject().equalsIgnoreCase(subject)){
                return mailMessage.getMessageId();
            }

        }
        System.out.println(subject);
        return "instant";
    }
}
