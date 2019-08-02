package com.backup.reports.util;

/**
 * Created by ken on 7/23/2019.
 */
public class MailMessage {
    private String message;
    private String messageId;
    private String subject;

    public MailMessage(String message, String messageId, String subject) {
        this.message = message;
        this.messageId = messageId;
        this.subject = subject;
    }

    public MailMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
