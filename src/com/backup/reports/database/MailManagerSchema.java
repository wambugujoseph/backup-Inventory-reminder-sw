package com.backup.reports.database;

import com.backup.reports.services.MailSender;
import com.backup.reports.util.*;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken & Joseph on 7/22/2019.
 */
public class MailManagerSchema {
    private PreparedStatement preparedStatement;
    private Connection connection = DbConnection.connection;
    final static Logger logger = Logger.getLogger(MailSender.class);
    public boolean addAdmin(AdminData adminData){
        String adminQuery="INSERT INTO adminTable(userName,adminPassword ) VALUES(?,?)";
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=connection.prepareStatement(adminQuery);
                preparedStatement.setString(1,adminData.getUserName());
                preparedStatement.setString(2,adminData.getAdminPassword());
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Database connection error "+ e.getMessage());
            } finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean updateAdmin( AdminData adminData,int id){
        String updateAdmin="UPDATE adminTable SET  userName=?,adminPassword=? WHERE id=?";
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=connection.prepareStatement(updateAdmin);
                preparedStatement.setString(1,adminData.getUserName());
                preparedStatement.setString(2,adminData.getAdminPassword());
                preparedStatement.setInt(3,id);
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public AdminData getAdmin() {
        String updateAdmin = "SELECT * FROM admintable";
        if (new DbConnection().isDatabaseConnected()) {
            try {
                PreparedStatement preparedStatement = DbConnection.connection.prepareStatement(updateAdmin);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()){
                    return new AdminData(rs.getString("userName"),rs.getString("adminPassword"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public  boolean saveEmail(ClientAddress clientAddress) {
        String emailQuery = "INSERT INTO client_emails(active,equipment_User,email_address,client_Machine,execution_Time) values(?,?,?, ?,?)";
        if (new DbConnection().isDatabaseConnected()){
            try{
                try {
                    preparedStatement = connection.prepareStatement(emailQuery);
                    preparedStatement.setString(1,clientAddress.getActive());
                    preparedStatement.setString(2,clientAddress.getEquipmentUser());
                    preparedStatement.setString(3,clientAddress.getEmail());
                    preparedStatement.setString(4,clientAddress.getClientMachine());
                    preparedStatement.setString(5,clientAddress.getExecutionTime());
                    preparedStatement.execute();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }
    public List<ClientAddress> getAllAddresses() {
        String listEmail= "SELECT * FROM client_emails";
        List<ClientAddress> clientAddressList = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()){
            try {
                PreparedStatement preparedStatement = DbConnection.connection.prepareStatement(listEmail);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    clientAddressList.add(new ClientAddress(rs.getString("active"),rs.getString("equipment_user"),rs.getString("email_address"),
                            rs.getString("client_Machine"),rs.getString("execution_Time")));
                }
                return clientAddressList;

            }catch (SQLException e){e.printStackTrace();}
            finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public boolean editAddress(ClientAddress clientAddress, String email) {
        String editEmailQuery= "UPDATE client_emails SET active=?,client_machine=?, email_address=?, equipment_user=?,execution_time =? WHERE email_address = ?";
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=connection.prepareStatement(editEmailQuery);
                preparedStatement.setString(1,clientAddress.getActive());
                preparedStatement.setString(2,clientAddress.getClientMachine());
                preparedStatement.setString(3,clientAddress.getEmail());
                preparedStatement.setString(4,clientAddress.getEquipmentUser());
                preparedStatement.setString(5,clientAddress.getExecutionTime());
                preparedStatement.setString(6,email);
                preparedStatement.execute();
                return true;
            }catch (SQLException e){
                e.printStackTrace();
            }finally{
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }

    public  boolean deleteClient(String emailAddress){
        String deleteClient = "DELETE FROM client_emails WHERE email_address =?";
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=connection.prepareStatement(deleteClient);
                preparedStatement.setString(1,emailAddress);
                preparedStatement.execute();
                return true;
            }catch (SQLException e){
                e.printStackTrace();
            } finally{
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }
    public boolean setMailReminder(Reminder reminder){
        String reminderQuery = "INSERT INTO reminder_notification(status,reminder_occurrence,inventory_occurrence,recipient ,message ) values(?,?,?,?,?)";
        if (new DbConnection().isDatabaseConnected()){
            try{
                try {
                    preparedStatement = connection.prepareStatement(reminderQuery);
                    preparedStatement.setString(1,reminder.getStatus());
                    preparedStatement.setString(2,reminder.getReminderOccurrence());
                    preparedStatement.setString(3,reminder.getInventoryOccurrence());
                    preparedStatement.setString(4,reminder.getRecipient());
                    preparedStatement.setString(5,reminder.getMessage());
                    preparedStatement.execute();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }
    public List<Reminder> getAllReminder(){
        String selectReminder="SELECT equipment_user, client_machine, status, reminder_occurrence, inventory_occurrence," +
                " recipient, message_id, subject FROM client_emails, mail_message,reminder_notification " +
                "WHERE mail_message.message_id=reminder_notification.message" +
                " AND client_emails.email_address = reminder_notification.recipient";
        List<Reminder> reminders = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=connection.prepareStatement(selectReminder);
                ResultSet rs=preparedStatement.executeQuery();
                while (rs.next()){
                    reminders.add(new Reminder(rs.getString("status"),rs.getString("reminder_occurrence"),rs.getString("inventory_occurrence"),rs.getString("recipient"),
                            rs.getString("subject"),rs.getString("message_id"),rs.getString("equipment_user"),
                            rs.getString("client_machine")));
                }
                return reminders;
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public boolean editReminder(Reminder reminder, String recipient)  {
        String editReminderQuery = "UPDATE \n" +
                "reminder_notification SET status =?,reminder_occurrence=?,inventory_occurrence=?,message =? WHERE recipient=?";
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=DbConnection.connection.prepareStatement(editReminderQuery);
                preparedStatement.setString(1,reminder.getStatus());
                preparedStatement.setString(2,reminder.getReminderOccurrence());
                preparedStatement.setString(3,reminder.getInventoryOccurrence());
                preparedStatement.setString(4,reminder.getMessage());
                preparedStatement.setString(5,recipient);
                preparedStatement.execute();
                return  true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean deleteReminder(String recipient){
        String deleteReminder ="DELETE FROM reminder_notification WHERE recipient =?";
        try {
            if (new DbConnection().isDatabaseConnected()){
               PreparedStatement preparedStatement=DbConnection.connection.prepareStatement(deleteReminder);
                preparedStatement.setString(1,recipient);
                preparedStatement.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
       return false;
    }

    public List<MailLog> getAllMailLog(){
        String reminderQuery = "SELECT * FROM mail_logs";
        List<MailLog> mailLogList = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()){
            try {
                PreparedStatement preparedStatement = DbConnection.connection.prepareStatement(reminderQuery);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()){
                    mailLogList.add(new MailLog(rs.getString("status"),rs.getString("date_time"),rs.getString("log_level")
                            ,rs.getString("recipient"),rs.getString("client_machine"),rs.getString("subject"),
                            rs.getString("message_id"),rs.getString("inventory")));
                }
                    return mailLogList;
                } catch (SQLException e) {
                    e.printStackTrace();

                }finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return  null;
    }

    public List<MailMessage> getAllMailMessage(){
        String query = "SELECT * FROM mail_message";
        List<MailMessage> mailMessages = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()) {
            try {
                PreparedStatement preparedStatement=DbConnection.connection.prepareStatement(query);
                ResultSet rs=preparedStatement.executeQuery();
                while (rs.next()){
                    MailMessage mailMessage = new MailMessage();
                    mailMessage.setMessage(rs.getString("content"));
                    mailMessage.setSubject(rs.getString("subject"));
                    mailMessage.setMessageId(rs.getString("message_id"));
                    mailMessages.add(mailMessage);
                }
                return mailMessages;
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return mailMessages;
    }

    public boolean CreateMAilMessage(MailMessage mailMessage){
        String query = "INSERT INTO mail_message(message_id, Subject, Content)VALUES (?,?,?)";

        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,mailMessage.getMessageId());
            preparedStatement.setString(2,mailMessage.getSubject());
            preparedStatement.setString(3,mailMessage.getMessage());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMailMessage(MailMessage mailMessage, String msgId){

        String query = "UPDATE mail_message SET content=?,subject=?,message_id=?WHERE message_id=?";
        if (new DbConnection().isDatabaseConnected()) {
            try {
                preparedStatement=connection.prepareStatement(query);
                preparedStatement.setString(1,mailMessage.getMessage());
                preparedStatement.setString(2,mailMessage.getSubject());
                preparedStatement.setString(3,mailMessage.getMessageId());
                preparedStatement.setString(4,msgId);
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return  false;
    }

    /**
     * num row count
     * @return
     */
    public int getRowNumber(String serverName, Date fromDate,Date toDate){
        int numberRow = 0;
        try{
            String query = "SELECT * from file_record WHERE server_name=? AND date_modified BETWEEN ? AND ?";
            PreparedStatement preparedStatement=DbConnection.connection.prepareStatement(query);
            preparedStatement.setString(1,serverName);
            preparedStatement.setDate(2,fromDate);
            preparedStatement.setDate(3,toDate);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
//                numberRow = rs.getInt("count(*)");
                numberRow++;
            }
            return numberRow;
        }catch (Exception ex){
            ex.printStackTrace();
//            System.out.println(ex.getMessage());
        }
        return numberRow;
    }

    /**
     * The method creates list of clients and the necessary details required to
     * send reminder or inventory pdf
     * @return list of ReminderNotificationMsg
     */
    public List<ReminderNotificationMsg> getAllReminderNotificationMsg(){
        String query = "SELECT reminder_occurrence,inventory_occurrence,recipient,subject,content,equipment_user," +
                "execution_time,client_machine" +
                "                 FROM reminder_notification,mail_message,client_emails" +
                "                 WHERE  reminder_notification.message=mail_message.message_id" +
                "                 AND reminder_notification.recipient=client_emails.email_address" +
                "                 AND reminder_notification.status ='active'";
        List<ReminderNotificationMsg> reminderNotificationMsgList = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement = connection.prepareStatement(query);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()){

                   reminderNotificationMsgList.add( new ReminderNotificationMsg(rs.getString("reminder_occurrence"),
                            rs.getString("inventory_occurrence"),
                            rs.getString("recipient"),
                            rs.getString("subject"),
                            rs.getString("content"),
                            rs.getString("equipment_user"),
                            rs.getString("execution_time"),
                            rs.getString("client_machine")));

                }
                return reminderNotificationMsgList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * delete mail log
     * @param date
     * @return
     */
    public  boolean deleteMailLog(String date){
        String deleteMailLog = "DELETE FROM mail_logs WHERE date_time =?";
        if (new DbConnection().isDatabaseConnected()){
            try {
                PreparedStatement preparedStatement=DbConnection.connection.prepareStatement(deleteMailLog);
                preparedStatement.setString(1,date);
                preparedStatement.execute();
                return true;
            }catch (SQLException e){
                e.printStackTrace();
            } finally{
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }

    /**
     * delete all mail logs from the list
     * @return
     */

    public boolean deleteAllMailLog(){
        String deleteAll="DELETE FROM mail_logs";
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=connection.prepareStatement(deleteAll);
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!connection.isClosed())
                        connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return  false;
    }
}


