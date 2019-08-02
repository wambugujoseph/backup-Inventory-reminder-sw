package com.backup.reports.services;

import com.backup.reports.database.MailManagerSchema;
import com.backup.reports.util.MailMessage;
import com.backup.reports.util.ReminderNotificationMsg;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by ken & joseph on 7/27/2019.
 */
public class MailMessageGen extends TimerTask{

    private String timed;

    public MailMessageGen(String timed) {
        this.timed = timed;
    }

    @Override
    public void run() {
        if (timed.equalsIgnoreCase("MONDAY") && LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY
                && LocalTime.now().isBefore(LocalTime.of(9,0))){
            reminder("MONDAY");
            inventory("MONDAY");
        }else if (timed.equalsIgnoreCase("FRIDAY") && LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY
                && LocalTime.now().isBefore(LocalTime.of(17,15))){
            reminder("FRIDAY");
            inventory("FRIDAY");
        }else if (timed.equalsIgnoreCase("MONTH")) {
            inventory("MONTH");
            reminder("MONTH");
        }
    }

    /**
     * creates reminder for each client and invoke mail sending process
     * @param scheduledTime
     */
    public void reminder(String scheduledTime){
        List<ReminderNotificationMsg> reminderNotificationMsgList=  new MailManagerSchema().getAllReminderNotificationMsg();
        //iterate the list of clients and their mail related details
        for (ReminderNotificationMsg reminderNotificationMsg : reminderNotificationMsgList){

            if (reminderNotificationMsg.getReminder_occurrence().equalsIgnoreCase(scheduledTime)) {
                new MailSender().sendMail(reminderNotificationMsg.getSubject(),
                        getReminderFullMessageBody(reminderNotificationMsg.getContent(),
                                                        reminderNotificationMsg.getEquipment_user(),
                                                        reminderNotificationMsg.getExecution_time()),
                                    reminderNotificationMsg.getRecipient(),
                                    false,
                                        "none");
            }
        }
    }

    /**
     * creates inventory pdf and message for every client
     * @param scheduledTime
     */
    public void inventory(String scheduledTime){
        List<ReminderNotificationMsg> reminderNotificationMsgList=  new MailManagerSchema().getAllReminderNotificationMsg();
        //iterate all the list of client and their mail related details
        for (ReminderNotificationMsg reminderNotificationMsg : reminderNotificationMsgList){
            List<MailMessage> mailMessageList = new MailManagerSchema().getAllMailMessage();
            MailMessage message = new MailMessage();
            //get the inventory message
            for (MailMessage mailMessage : mailMessageList){
                if(mailMessage.getMessageId().equalsIgnoreCase("inventory")){
                    message.setMessageId(mailMessage.getMessageId());
                    message.setSubject(mailMessage.getSubject());
                    message.setMessage(mailMessage.getMessage());
                }
            }
            //check execution time
            if( new MailManagerSchema().getRowNumber(
                            reminderNotificationMsg.getClient_machine(),
                            Date.valueOf(LocalDate.now().minusMonths(1)),Date.valueOf(LocalDate.now().minusDays(1)))>=1) {

                if (reminderNotificationMsg.getInventory_occurrence().equalsIgnoreCase(scheduledTime)) {
                        //create inventory pdf and return path
                    String pdfPath;
                    if (scheduledTime.equalsIgnoreCase("THURSDAY")) {
                        pdfPath = new InventoryMailAttachment().createDepInventoryPDFFromTo(
                                reminderNotificationMsg.getClient_machine(),
                                Date.valueOf(LocalDate.now().minusDays(9)),Date.valueOf(LocalDate.now()));
                    }else{
                        pdfPath = new InventoryMailAttachment().createDepInventoryPDFFromTo(
                                reminderNotificationMsg.getClient_machine(),
                                Date.valueOf(LocalDate.now().minusMonths(1)),Date.valueOf(LocalDate.now().minusDays(1)));
                    }

                    new MailSender().sendMail(message.getSubject(),
                            getInventoryFullMessageBody(message.getMessage(),
                                                        reminderNotificationMsg.getEquipment_user(),
                                                        scheduledTime),
                            reminderNotificationMsg.getRecipient(),
                            true,
                            pdfPath);
                }
            }
        }
    }

    /**
     * create full reminder message body
     * @param lawMessage
     * @param recipientName
     * @param executionTime
     * @return
     */
    private String getReminderFullMessageBody(String lawMessage, String recipientName, String executionTime){
        String lawMessageOne = lawMessage.replace("**TIME", executionTime);
        return lawMessageOne.replace("**NAME",getClientName(recipientName));
    }
    /**
     * create inventory full message body
     * @param message
     * @param recipientName
     * @return
     */
    private String getInventoryFullMessageBody(String message, String recipientName, String timed){
        String lawMessageOne = message.replace("**NAME",getClientName(recipientName));
        if (timed.equalsIgnoreCase("THURSDAY")) {
            String lawMessageTwo = lawMessageOne.replace("**DAY",LocalDate.now().minusDays(4).toString());
            return lawMessageTwo.replace("**CURRENT", LocalDate.now().toString());
        }else if (timed.equalsIgnoreCase("MONTHLY")){
            String lawMessageTwo = lawMessageOne.replace("**DAY", LocalDate.now().minusMonths(1).toString());
            return lawMessageTwo.replace("**CURRENT", LocalDate.now().minusDays(1).toString());
        }
        return null;
    }

    private  String getClientName(String fullName){
        try {
            String title = fullName.substring(0,fullName.indexOf(" "));
            String rem = fullName.replace(title+"","").trim();
            String firstName;
            try{
                firstName= rem.substring(0,rem.indexOf(" "));
            }catch (StringIndexOutOfBoundsException e){
                firstName = rem;
            }
            return title+" "+firstName;
        } catch (StringIndexOutOfBoundsException e) {
            return fullName;
        }
    }
}
