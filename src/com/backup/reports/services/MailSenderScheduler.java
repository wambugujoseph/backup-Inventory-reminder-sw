package com.backup.reports.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

/**
 * Created by ken and Joseph on 7/27/2019.
 */
public class MailSenderScheduler {

    public static Timer mailTimer = new Timer(true);

    public void mailSendSchedule(){
        LocalDate localDate =LocalDate.now();
        LocalTime morningTime = LocalTime.of(7,45);
        LocalTime eveningTime = LocalTime.of(16,45);
        Instant morningInstance = morningTime.atDate(LocalDate.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth()))
                .atZone(ZoneId.systemDefault()).toInstant();
        Instant eveningInstance =eveningTime.atDate(LocalDate.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth()))
                .atZone(ZoneId.systemDefault()).toInstant();
        Instant eveningMonthInstance =eveningTime.atDate(LocalDate.of(localDate.getYear(),localDate.getMonth(),1))
                .atZone(ZoneId.systemDefault()).toInstant();

        Date monthDay = Date.from(eveningMonthInstance);
        Date monday = Date.from(morningInstance);
        Date friday = Date.from(eveningInstance);

        mailTimer.schedule(new MailMessageGen("MONTH"),monthDay);
        mailTimer.schedule(new MailMessageGen("MONDAY"),monday);
        mailTimer.schedule(new MailMessageGen("FRIDAY"),friday);

    }

}
