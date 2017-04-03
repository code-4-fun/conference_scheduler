package com.demo.apps.conference_scheduler.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author devendra.nalawade on 4/2/17
 */
public class Talk {

    private String title;
    private int minutes;

    private Date startTime;
    private Date endTime;

    public Talk(String title, int minutes) {
        this.title = title;
        this.minutes = minutes;
    }

    public String getTitle() {
        return title;
    }

    public int getMinutes() {
        return minutes;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public static Talk parseTalk(String talkDataStr) {
        if (talkDataStr == null || talkDataStr.trim().length() <= 0) {
            return null;
        }

        final String separator = "~";
        String data[] = talkDataStr.split(separator);

        if (data[1] == null || data[1].trim() == "") {
            int minutes = new Integer(data[1].trim());
            return new Talk(data[0], minutes);
        } else if ("lightning".equals(data[1])) {
            return new LighteningTalk(data[0]);
        } else if (data[1].contains("min")) {
            int minutes = new Integer(data[1].replace("min", "").trim());
            return new Talk(data[0], minutes);
        } else {
            throw new RuntimeException("Talk Definition is not Valid - " + talkDataStr);
        }

    }

    @Override
    public String toString() {
        return "[" +
                "'" + title + '\'' +
                ", startTime=" + getHoursAndMinutes(startTime) +
                ", endTime=" + getHoursAndMinutes(endTime) +
                ", minutes=" + minutes +
                ']';
    }

    private String getHoursAndMinutes(Date time) {
        if (time == null) {
            return "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return dateFormat.format(time);
    }

}
