package com.aorobator.githubresearch;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.service.EventService;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * An EventAverager computes the averages of all 18 GitHub events per day for a single user.
 *
 * @author Andrew Orobator
 */

public class EventAverager {

    GitHubClient mGitHubClient;
    EventService mEventService;

    static int count = 0;

    public static final String TYPE_COMMIT_COMMENT = "CommitCommentEvent";
    public static final String TYPE_CREATE = "CreateEvent";
    public static final String TYPE_DELETE = "DeleteEvent";
    public static final String TYPE_DOWNLOAD = "DownloadEvent";
    public static final String TYPE_FOLLOW = "FollowEvent";
    public static final String TYPE_FORK = "ForkEvent";
    public static final String TYPE_FORK_APPLY = "ForkApplyEvent";
    public static final String TYPE_GIST = "GistEvent";
    public static final String TYPE_GOLLUM = "GollumEvent";
    public static final String TYPE_ISSUE_COMMENT = "IssueCommentEvent";
    public static final String TYPE_ISSUES = "IssuesEvent";
    public static final String TYPE_MEMBER = "MemberEvent";
    public static final String TYPE_PUBLIC = "PublicEvent";
    public static final String TYPE_PULL_REQUEST = "PullRequestEvent";
    public static final String TYPE_PULL_REQUEST_REVIEW_COMMENT = "PullRequestReviewCommentEvent";
    public static final String TYPE_PUSH = "PushEvent";
    public static final String TYPE_TEAM_ADD = "TeamAddEvent";
    public static final String TYPE_WATCH = "WatchEvent";

    private double commitCommentAvg;
    private double createAvg;
    private double deleteAvg;
    private double downloadAvg;
    private double followAvg;
    private double forkAvg;
    private double forkApplyAvg;
    private double gistAvg;
    private double gollumAvg;
    private double issueAvg;
    private double issueCommentAvg;
    private double memberAvg;
    private double publicAvg;
    private double pullRequestAvg;
    private double pullRequestReviewAvg;
    private double pushAvg;
    private double teamAddAvg;
    private double watchAvg;

    private Date oldestDate;
    private Date mostRecentDate;

    private double days;
    private double eventsPerDay;
    private Event oldestEvent;

    /**
     * Constructor takes in a GitHubClient in order to make API calls. If using
     * authentication, authenticate the GitHubClient before passing it to the EventAverager.
     *
     * @param gitHubClient A possibly authenticated GitHubClient
     */
    public EventAverager(GitHubClient gitHubClient) {
        this.mGitHubClient = gitHubClient;
        this.mEventService = new EventService(mGitHubClient);
    }

    /**
     * Given a collection of events, averages(PageIterator<Event> events)
     * returns an array of doubles consisting of the daily averages for each
     * event type.
     *
     * @param eventPageIterator Collection of events
     * @return An array of doubles indicating the daily averages of the the
     *         notifcations. The array is of the format:
     *         {eventsPerDay, commitCommentEventsPerDay, createEventsPerDay, deleteEventsPerDay,
     *         downloadEventsPerDay, followEventsPerDay, forkEventsPerDay, forkApplyEventsPerDay,
     *         gistEventsPerDay, gollumEventsPerDay, issueEventsPerDay, issueCommentEventsPerDay,
     *         memberEventsPerDay, publicEventsPerDay, pullRequestEventsPerDay, pullRequestReviewEventsPerDay,
     *         pushDatesEventsPerDay, teamAddEventsPerDay, watchDatesEventsPerDay};
     */
    public double[] averages(PageIterator<Event> eventPageIterator) {
        Iterator<Collection<Event>> eventIterator = eventPageIterator.iterator();

        Vector<Date> commitCommentDates = new Vector<Date>();
        Vector<Date> createDates = new Vector<Date>();
        Vector<Date> deleteDates = new Vector<Date>();
        Vector<Date> downloadDates = new Vector<Date>();
        Vector<Date> followDates = new Vector<Date>();
        Vector<Date> forkDates = new Vector<Date>();
        Vector<Date> forkApplyDates = new Vector<Date>();
        Vector<Date> gistDates = new Vector<Date>();
        Vector<Date> gollumDates = new Vector<Date>();
        Vector<Date> issueCommentDates = new Vector<Date>();
        Vector<Date> issueDates = new Vector<Date>();
        Vector<Date> memberDates = new Vector<Date>();
        Vector<Date> publicDates = new Vector<Date>();
        Vector<Date> pullRequestDates = new Vector<Date>();
        Vector<Date> pullRequestReviewDates = new Vector<Date>();
        Vector<Date> pushDates = new Vector<Date>();
        Vector<Date> teamAddDates = new Vector<Date>();
        Vector<Date> watchDates = new Vector<Date>();

        oldestDate = new Date();
        mostRecentDate = new Date();
        mostRecentDate.setTime(0);
        int eventCount = 0;
        oldestEvent = new Event();

        while (eventIterator.hasNext()) {

            Collection<Event> events = eventIterator.next();//Goes through pages
            for (Event event : events) {
                eventCount++;

                //Look for the oldest Event
                if (event.getCreatedAt().before(oldestDate)) {
                    oldestDate = event.getCreatedAt();
                    oldestEvent = event;
                }

                //Look for the most recent Event
                if (event.getCreatedAt().after(mostRecentDate)) {
                    mostRecentDate = event.getCreatedAt();
                }

                String eventType = event.getType();

                if (eventType.equals(TYPE_COMMIT_COMMENT)) {
                    commitCommentDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_CREATE)) {
                    createDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_DELETE)) {
                    deleteDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_DOWNLOAD)) {
                    downloadDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_FOLLOW)) {
                    followDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_FORK)) {
                    forkDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_FORK_APPLY)) {
                    forkApplyDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_GIST)) {
                    gistDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_GOLLUM)) {
                    gollumDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_ISSUE_COMMENT)) {
                    issueCommentDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_ISSUES)) {
                    issueDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_MEMBER)) {
                    memberDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_PUBLIC)) {
                    publicDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_PULL_REQUEST)) {
                    pullRequestDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_PULL_REQUEST_REVIEW_COMMENT)) {
                    pullRequestReviewDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_PUSH)) {
                    pushDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_TEAM_ADD)) {
                    teamAddDates.add(event.getCreatedAt());
                } else if (eventType.equals(TYPE_WATCH)) {
                    watchDates.add(event.getCreatedAt());
                }
            }
        }

        long timeDifference = mostRecentDate.getTime() - oldestDate.getTime();

        int secondsPerMinute = 60;
        int minutesPerHour = 60;
        int hoursPerDay = 24;
        int secondsPerDay = secondsPerMinute * minutesPerHour * hoursPerDay;
        int millisecondsPerSecond = 1000;

        days = (timeDifference / (secondsPerDay * 1.0 * millisecondsPerSecond));
        eventsPerDay = eventCount / (days * 1.0);


        /* printInfo(); */ //Optional

        commitCommentAvg = dayAverage(commitCommentDates, timeDifference, oldestDate);
        createAvg = dayAverage(createDates, timeDifference, oldestDate);
        deleteAvg = dayAverage(deleteDates, timeDifference, oldestDate);
        downloadAvg = dayAverage(downloadDates, timeDifference, oldestDate);
        followAvg = dayAverage(followDates, timeDifference, oldestDate);
        forkAvg = dayAverage(forkDates, timeDifference, oldestDate);
        forkApplyAvg = dayAverage(forkApplyDates, timeDifference, oldestDate);
        gistAvg = dayAverage(gistDates, timeDifference, oldestDate);
        gollumAvg = dayAverage(gollumDates, timeDifference, oldestDate);
        issueAvg = dayAverage(issueDates, timeDifference, oldestDate);
        issueCommentAvg = dayAverage(issueCommentDates, timeDifference, oldestDate);
        memberAvg = dayAverage(memberDates, timeDifference, oldestDate);
        publicAvg = dayAverage(publicDates, timeDifference, oldestDate);
        pullRequestAvg = dayAverage(pullRequestDates, timeDifference, oldestDate);
        pullRequestReviewAvg = dayAverage(pullRequestReviewDates, timeDifference, oldestDate);
        pushAvg = dayAverage(pushDates, timeDifference, oldestDate);
        teamAddAvg = dayAverage(teamAddDates, timeDifference, oldestDate);
        watchAvg = dayAverage(watchDates, timeDifference, oldestDate);

        /* printStats(); */ //Optional

        double[] results = {eventsPerDay, commitCommentAvg, createAvg, deleteAvg,
                downloadAvg, followAvg, forkAvg, forkApplyAvg, gistAvg, gollumAvg,
                issueAvg, issueCommentAvg, memberAvg, publicAvg, pullRequestAvg,
                pullRequestReviewAvg, pushAvg, teamAddAvg, watchAvg};

        return results;
    }

    /**
     * Computes the daily average of an event.
     *
     * @param dates          A collection of dates that the given event happened on.
     * @param timeDifference The difference in time in milliseconds between the
     *                       oldest overall event out of all types and the most
     *                       recent event out of all types
     * @return result        The daily average for the given event by (# of occurrences
     *                       of the given event)/(time between oldest overall event and
     *                       most recent overall event)
     */
    public double dayAverage(Vector<Date> dates, long timeDifference, Date oldestDate) {

        int secondsPerMinute = 60;
        int minutesPerHour = 60;
        int hoursPerDay = 24;
        int secondsPerDay = secondsPerMinute * minutesPerHour * hoursPerDay;
        int millisecondsPerSecond = 1000;
        double numDays = (timeDifference / (secondsPerDay * 1.0 * millisecondsPerSecond));

        double result = dates.size()/numDays;

        return result;

    }


    /**
     * Prints the statistics that were just calculated
     */
    private void printStats() {
        System.out.println();
        System.out.println("Daily average " + TYPE_COMMIT_COMMENT + ": " + commitCommentAvg);
        System.out.println("Daily average " + TYPE_CREATE + ": " + createAvg);
        System.out.println("Daily average " + TYPE_DELETE + ": " + deleteAvg);
        System.out.println("Daily average " + TYPE_DOWNLOAD + ": " + downloadAvg);
        System.out.println("Daily average " + TYPE_FOLLOW + ": " + followAvg);
        System.out.println("Daily average " + TYPE_FORK + ": " + forkAvg);
        System.out.println("Daily average " + TYPE_FORK_APPLY + ": " + forkApplyAvg);
        System.out.println("Daily average " + TYPE_GIST + ": " + gistAvg);
        System.out.println("Daily average " + TYPE_GOLLUM + ": " + gollumAvg);
        System.out.println("Daily average " + TYPE_ISSUES + ": " + issueAvg);
        System.out.println("Daily average " + TYPE_ISSUE_COMMENT + ": " + issueCommentAvg);
        System.out.println("Daily average " + TYPE_MEMBER + ": " + memberAvg);
        System.out.println("Daily average " + TYPE_PUBLIC + ": " + publicAvg);
        System.out.println("Daily average " + TYPE_PULL_REQUEST + ": " + pullRequestAvg);
        System.out.println("Daily average " + TYPE_PULL_REQUEST_REVIEW_COMMENT + ": " + pullRequestReviewAvg);
        System.out.println("Daily average " + TYPE_PUSH + ": " + pushAvg);
        System.out.println("Daily average " + TYPE_TEAM_ADD + ": " + teamAddAvg);
        System.out.println("Daily average " + TYPE_WATCH + ": " + watchAvg);
        System.out.println();
    }

    /**
     * Prints metadata about the collection of events
     */
    private void printInfo() {
        System.out.println();
        System.out.println("Oldest event: " + oldestDate.toString());
        System.out.println("Most recent event: " + mostRecentDate.toString());
        System.out.println("Days: " + days);
        System.out.println("Notifications per day: " + eventsPerDay);

        System.out.println();
        System.out.println("Oldest event type: " + oldestEvent.getType().toString());
        System.out.println("Oldest event author: " + oldestEvent.getActor().getName());
        System.out.println("Oldest event repo: " + oldestEvent.getRepo().getName());
        System.out.println();
    }

}
