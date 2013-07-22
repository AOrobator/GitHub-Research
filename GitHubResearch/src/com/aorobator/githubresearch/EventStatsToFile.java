package com.aorobator.githubresearch;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.NoSuchPageException;
import org.eclipse.egit.github.core.service.EventService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Given a String[] of usernames, the EventStatsToFile writes the notification statistics to the specified filename
 */

public class EventStatsToFile {
    GitHubClient gitHubClient;
    EventAverager eventAverager;
    EventService mEventService;
    private final String statHeader = "eventsPerDay;commitCommentEventsPerDay;createEventsPerDay;deleteEventsPerDay;" +
            "downloadEventsPerDay;followEventsPerDay;forkEventsPerDay;forkApplyEventsPerDay;gistEventsPerDay;" +
            "gollumEventsPerDay;issueEventsPerDay;issueCommentEventsPerDay;memberEventsPerDay;publicEventsPerDay;" +
            "pullRequestEventsPerDay;pullRequestReviewEventsPerDay;pushDatesEventsPerDay;teamAddEventsPerDay;watchDatesEventsPerDay\n";

    public EventStatsToFile(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
        eventAverager = new EventAverager(this.gitHubClient);
        mEventService = new EventService(this.gitHubClient);
    }

    public void run(String[] users, String filename) throws IOException {
        int statCount = 0;
        FileWriter fileWriter = new FileWriter(filename);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("user;" + statHeader);

        for (String user : users) {

            double[] stats;
            try {
                stats = eventAverager.averages(mEventService.pageUserReceivedEvents(user));
            } catch (NoSuchPageException e) {
                try {
                    stats = eventAverager.averages(mEventService.pageUserReceivedEvents(user));
                } catch (NoSuchPageException e1) {
                    stats = null;
                }

            }
            if (stats == null) {
                continue;
            }

            bufferedWriter.write(user + ";" + stats[0] + ";" + stats[1] + ";" + stats[2] + ";" + stats[3] + ";" +
                    stats[4] + ";" + stats[5] + ";" + stats[6] + ";" + stats[7] + ";" + stats[8] + ";" +
                    stats[9] + ";" + stats[10] + ";" + stats[11] + ";" + stats[12] + ";" + stats[13] + ";" +
                    stats[14] + ";" + stats[15] + ";" + stats[16] + ";" + stats[17] + ";" + stats[18] + "\n");

            System.out.println("Wrote stats for " + user + " " + ++statCount);
        }

        bufferedWriter.close();

    }
}
