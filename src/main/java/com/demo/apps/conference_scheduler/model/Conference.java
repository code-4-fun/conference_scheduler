package com.demo.apps.conference_scheduler.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author devendra.nalawade on 4/2/17
 */
public class Conference {

    private static final Logger LOGGER = Logger.getLogger(Conference.class);

    private List<Track> tracks;
    private int trackCounter = 0;

    public Conference() {
        this.tracks = new ArrayList<>();
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public Track addTrack() {
        Track track = new Track(trackCounter++);
        this.tracks.add(track);
        return track;
    }

    public void printSchedule() {

        if (tracks == null || tracks.isEmpty()) {
            LOGGER.info("No Sessions are planned for Conference yet!!!");
        } else {
            LOGGER.info("======================== CONFERENCE SCHEDULE =============================");
            tracks.forEach(track -> {
                LOGGER.info("========================================================================");
                track.getSessions().forEach(session -> {
                    LOGGER.info("Track - " + track.getTrackId() + " : " + session.getType().name);
                    if (session.getTalks() != null
                            && !session.getTalks().isEmpty()) {
                        LOGGER.info("Topics: ");
                        session.getTalks()
                                .forEach(topic -> {
                                    LOGGER.info(topic);
                                });
                    }
                });
                LOGGER.info("========================================================================");
            });
            LOGGER.info("======================== CONFERENCE SCHEDULE =============================");
        }

    }

}
