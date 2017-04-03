package com.demo.apps.conference_scheduler.scheduler.algorithm;

import com.demo.apps.conference_scheduler.model.Session;
import com.demo.apps.conference_scheduler.model.Talk;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.ListIterator;

/**
 * @author devendra.nalawade on 4/2/17
 */
/**
 * Algorithm Used: <a href="https://en.wikipedia.org/wiki/Knapsack_problem">Knapsack Problem</a>
 * Sample Implementation:
 * <a href="http://introcs.cs.princeton.edu/java/23recursion/Knapsack.java.html">
 * Knapsack Sample Implementation
 * </a>
 */
public class SchedulerAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(SchedulerAlgorithm.class);

    private List<Talk> talks;

    public Session schedule(Session session, List<Talk> talks) {
        int W = session.getSessionIntervalInMinutes();
        int N = talks.size();

        int[] profit = new int[N + 1];
        int[] weight = new int[N + 1];
        int i = 1;
        // profit and weight are same in this case
        for (Talk talk : talks) {
            profit[i] = weight[i] = talk.getMinutes();
            i++;
        }

        LOGGER.debug("Remaining Talks to schedule - " + talks);
        int totalMinutesToSchedule = totalMinutesToSchedule(talks);
        LOGGER.debug("Total Minutes to schedule - " + totalMinutesToSchedule);

        if (totalMinutesToSchedule < W) {
            talks.listIterator()
                    .forEachRemaining(talk -> session.addTalk(talk));
            talks.clear();
        } else {
            boolean take[] = schedule(W, N, profit, weight);
            updateTalkList(session, talks, take);
        }

        session.printSessionSummary();
        return session;
    }

    private int totalMinutesToSchedule(List<Talk> talks) {
        return talks.stream()
                .map(Talk::getMinutes)
                .reduce(Math::addExact)
                .get();
    }

    private boolean[] schedule(int W, int N, int[] profit, int[] weight) {
        // opt[n][w] = max profit of packing items 1..n with weight limit w
        // sol[n][w] = does opt solution to pack items 1..n with weight limit w
        // include item n?
        int[][] opt = new int[N + 1][W + 1];
        boolean[][] sol = new boolean[N + 1][W + 1];

        for (int n = 1; n <= N; n++) {
            for (int w = 1; w <= W; w++) {

                // don't take item n
                int option1 = opt[n - 1][w];

                // take item n
                int option2 = Integer.MIN_VALUE;
                if (weight[n] <= w)
                    option2 = profit[n] + opt[n - 1][w - weight[n]];

                // select better of two options
                opt[n][w] = Math.max(option1, option2);
                sol[n][w] = (option2 > option1);
            }
        }

        // determine which items to take
        boolean[] take = new boolean[N + 1];
        for (int n = N, w = W; n > 0; n--) {
            if (sol[n][w]) {
                take[n] = true;
                w = w - weight[n];
            } else {
                take[n] = false;
            }
        }
        return take;
    }

    private void updateTalkList(Session session, List<Talk> talks, boolean[] take) {
        int counter = 0;
        ListIterator<Talk> iterator = talks.listIterator();

        Talk talk = null;
        while (iterator.hasNext()) {
            talk = iterator.next();
            if (take[counter]) {
                session.addTalk(talk);
                iterator.remove();
            }
            counter++;
        }
    }

}
