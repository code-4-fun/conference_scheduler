package com.demo.apps.conference_scheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author devendra.nalawade on 4/2/17
 */
public class Track {

    private int trackId;
    private List<Session> sessions;

    public Track(int trackId) {
        this.trackId = trackId;
        this.sessions = new ArrayList<>();

        // initialise sessions
        this.sessions.add(new Session(180, Session.SessionType.MORNING, 0, true));
        this.sessions.add(new Session(60, Session.SessionType.LUNCH, 0, false));
        this.sessions.add(new Session(180, Session.SessionType.AFTERNOON, 60, true));
        this.sessions.add(new Session(120, Session.SessionType.NETWORKING, 0, false));
    }

    public int getTrackId() {
        return trackId;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public int getTotalTimeAvailable() {
        int totalTimeAvailable =
                getSessions()
                        .stream()
                        .filter(Session::isAvailableForSchedule)
                        .map(session -> session.getTimeAvailable())
                        .reduce(Math::addExact)
                        .get();
        return totalTimeAvailable;
    }
}
