package com.demo.apps.conference_scheduler;

import com.demo.apps.conference_scheduler.model.Conference;
import com.demo.apps.conference_scheduler.model.Talk;
import com.demo.apps.conference_scheduler.scheduler.ConferenceManager;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ConferenceSchedulerApp {

    private static final Logger LOGGER = Logger.getLogger(ConferenceSchedulerApp.class);

    public static void main(String[] args) {
        ConferenceManager manager = new ConferenceManager();
        //Conference conference = manager.schedule(createDummyTalks(15));
        DataReader reader = new ConferenceSchedulerApp().new DataReader();
        Conference conference = manager.schedule(reader.readTalksFromFile("topics.txt"));
        conference.printSchedule();
    }

    private static List<Talk> createDummyTalks(int number) {
        List<Talk> talks = new ArrayList<>(number);
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            talks.add(new Talk("Talk - " + i, random.nextInt(60)));
        }
        return talks;
    }

    class DataReader {

        private static final String DATA_DIR = "src/main/resources/data";

        public List<Talk> readTalksFromFile(String fileName) {

            if (fileName == null || fileName.trim().length() <= 0) {
                return null;
            }

            File dataFile = new File(DATA_DIR + File.separator + fileName);
            LOGGER.info("Reading Data from File - " + dataFile.getAbsolutePath());

            if (dataFile.exists() && dataFile.isFile()) {

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(dataFile));
                    return reader.lines()
                            .map(line -> Talk.parseTalk(line))
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                            reader = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                return null;
            }

        }

    }

}
