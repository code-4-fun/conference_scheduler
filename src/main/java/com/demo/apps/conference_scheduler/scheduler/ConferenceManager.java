package com.demo.apps.conference_scheduler.scheduler;

import com.demo.apps.conference_scheduler.model.Conference;
import com.demo.apps.conference_scheduler.model.Session;
import com.demo.apps.conference_scheduler.model.Talk;
import com.demo.apps.conference_scheduler.model.Track;
import com.demo.apps.conference_scheduler.scheduler.algorithm.SchedulerAlgorithm;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author devendra.nalawade on 4/2/17
 */
public class ConferenceManager {

    private static final Logger LOGGER = Logger.getLogger(ConferenceManager.class);

    public Conference schedule(List<Talk> talks) {

        SchedulerAlgorithm algorithm = new SchedulerAlgorithm();
        Conference conference = new Conference();

        Track track = null;
        while (talks != null && !talks.isEmpty()) {
            LOGGER.debug("Talks to schedule - " + talks.size());
            track = conference.addTrack();
            LOGGER.debug("Minutes available on track - " + track.getTrackId() + " : " + track.getTotalTimeAvailable());
            track.getSessions()
                    .stream()
                    .filter(Session::isAvailableForSchedule)
                    .filter(session -> !talks.isEmpty())
                    .forEach(session -> algorithm.schedule(session, talks));
            LOGGER.debug("After Preparing track - " + track.getTotalTimeAvailable());
        }

        return conference;
    }


}
