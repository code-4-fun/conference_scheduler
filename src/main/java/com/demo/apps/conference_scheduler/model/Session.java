package com.demo.apps.conference_scheduler.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author devendra.nalawade on 4/2/17
 */
public class Session {

    private static final Logger LOGGER = Logger.getLogger(Session.class);

    private int sessionIntervalInMinutes;
    private int timeAvailable;
    private SessionType type;
    private int additionalBufferTimeInMinutes;
    private boolean isAvailableForSchedule = false;
    private List<Talk> talks = new ArrayList<>();
    private Date nextSessionStartTime;

    public Session(int sessionIntervalInMinutes, SessionType type, int additionalBufferTimeInMinutes,
                   boolean isAvailableForSchedule) {
        this.sessionIntervalInMinutes = sessionIntervalInMinutes;
        this.timeAvailable = sessionIntervalInMinutes;
        this.type = type;
        this.isAvailableForSchedule = isAvailableForSchedule;
        this.additionalBufferTimeInMinutes = additionalBufferTimeInMinutes;

        this.nextSessionStartTime = calculateNextSessionStartTime(new Date(), type);
    }

    public void addTalk(Talk talk) {

        talk.setStartTime(this.nextSessionStartTime);

        final Date endDate = shiftDateByMinutes(this.nextSessionStartTime, talk.getMinutes());
        talk.setEndTime(endDate);

        this.nextSessionStartTime = endDate;

        this.getTalks().add(talk);
        this.timeAvailable = (this.sessionIntervalInMinutes - talk.getMinutes());
    }

    public int getSessionIntervalInMinutes() {
        return sessionIntervalInMinutes;
    }

    public SessionType getType() {
        return type;
    }

    public int getAdditionalBufferTimeInMinutes() {
        return additionalBufferTimeInMinutes;
    }

    public boolean isAvailableForSchedule() {
        return isAvailableForSchedule;
    }

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    public int getTimeAvailable() {
        return timeAvailable;
    }

    private Date calculateNextSessionStartTime(Date conferenceDate, SessionType sessionType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(conferenceDate);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        String timeStr = sessionType.startTime;
        String time[] = timeStr.split(":");

        calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
        calendar.add(Calendar.MINUTE, Integer.valueOf(time[1]));

        return calendar.getTime();
    }

    private Date shiftDateByMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public void printSessionSummary() {
        LOGGER.debug("=================== SESSION SUMMARY ======================");
        LOGGER.debug("Session Type " + getType());
        LOGGER.debug("Session Talks Scheduled " + getTalks().size());
        LOGGER.debug("Session Time Available " + getTimeAvailable());
        LOGGER.debug("========================================================");
    }

    public enum SessionType {
        MORNING("Morning Session", "09:00"),
        AFTERNOON("Afternoon Session", "13:00"),
        LUNCH("Lunch Event", "12:00"),
        NETWORKING("Networking Event", "16:00");

        SessionType(String name, String startTime) {
            this.name = name;
            this.startTime = startTime;
        }

        String name;
        String startTime;
    }

}
