package com.aorobator.githubresearch;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**Returns different types of collections of users*/
public class UserCollections {
    private String topUsersFileLocation = "/Users/andreworobator/IdeaProjects/GitHubResearch/src/top_users_by_contribution.txt";
    private String snapshotFileLocation = "/Users/andreworobator/IdeaProjects/GitHubResearch/src/snapshot7152013154936.txt";
    private FileInputStream fileInputStream;
    private DataInputStream dataInputStream;
    private BufferedReader bufferedReader;
    private Vector<String> topUsersByContribution;
    private Vector<String> snapshotUsers;
    private GitHubClient gitHubClient;
    private EventService eventService;


    private String[] topUsersByFollowers = {"mojombo", "torvalds", "defunkt",
            "schacon", "paulirish", "pjhyett", "jeresig", "visionmedia",
            "ryanb", "addyosmani", "douglascrockford", "dhh", "wycats",
            "jashkenas", "tpope"};


    private String[] topUsersByCurrentContributionsStreak = {"sferik", "paulmillr",
            "dustin", "Ocramius", "ednapiranha", "antimatter15", "samuelclay",
            "bbatsov", "josevalim", "pydanny", "qiangxue", "Shougo", "tokuhirom",
            "markstory", "addyosmani"};

    private String[] topUsersByOrganizations = {"paulirish", "leto", "mizzy",
            "steveklabnik", "hsbt", "rkh", "wycats", "sindresorhus", "EvanDotPro",
            "rafl", "addyosmani", "dscape", "yrashk", "parndt", "mattt"};

    public UserCollections(GitHubClient gitHubClient) throws IOException {

        this.gitHubClient = gitHubClient;
        eventService = new EventService(this.gitHubClient);

        fileInputStream = new FileInputStream(topUsersFileLocation);
        dataInputStream = new DataInputStream(fileInputStream);
        bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

        topUsersByContribution = new Vector<String>();

        String strLine;
        while ((strLine = bufferedReader.readLine()) != null) {

            //Gets rid of extra unneccesary spaces
            strLine = strLine.replaceAll("\\s+", " ");

            topUsersByContribution.add(strLine.split(" ")[1]);
        }

        fileInputStream = new FileInputStream(snapshotFileLocation);
        dataInputStream = new DataInputStream(fileInputStream);
        bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

        snapshotUsers = new Vector<String>();

        while ((strLine = bufferedReader.readLine()) != null) {

            snapshotUsers.add(strLine);
        }

    }

    public String[] getMostRecentUsers() {
//        PageIterator<Event> publicEvents = eventService.pagePublicEvents();
//        Iterator<Collection<Event>> eventIterator = publicEvents.iterator();
//        HashSet<String> userSet = new HashSet<String>();
//
//        String login;
//        while (eventIterator.hasNext()) {
//
//            Collection<Event> events = eventIterator.next();//Goes through pages
//            for (Event event : events) {
//                login = event.getActor().getLogin();
//                if (login == null) {
//                    login = event.getRepo().getName();
//                }
//                userSet.add(login);
//            }
//        }
//
//        String[] users = new String[userSet.size()];
//        Iterator<String> userSetIterator = userSet.iterator();
//
//        int i = 0;
//        while (userSetIterator.hasNext()) {
//            System.out.println(userSetIterator.next());
////            users[i] = userSetIterator.next();
//        }
//
//        return users;

        String[] users = new String[snapshotUsers.size()];
        int i = 0;
        Iterator<String> stringIterator = snapshotUsers.iterator();
        while (stringIterator.hasNext()) {
            users[i] = stringIterator.next();
            i++;
        }
        return users;

    }

    public String[] getTopUsersByContribution() {
        String[] users = new String[topUsersByContribution.size()];
        int i = 0;
        Iterator<String> stringIterator = topUsersByContribution.iterator();
        while (stringIterator.hasNext()) {
            users[i] = stringIterator.next();
            i++;
        }
        return users;
    }

    public String[] getTopUsersByFollowers() {
        return topUsersByFollowers;
    }

    public String[] getTopUsersByCurrentContributionsStreak() {
        return topUsersByCurrentContributionsStreak;
    }

    public String[] getLinusFollowers() throws IOException {
        Vector<String> userVector = new Vector<String>();
        String fileLocation = "/Users/andreworobator/IdeaProjects/GitHubResearch/linus_followers_usernames.txt";

        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

        String strLine;
        while ((strLine = bufferedReader.readLine()) != null) {
            userVector.add(strLine);
        }

        String[] users = new String[userVector.size()];
        int i = 0;
        Iterator<String> stringIterator = userVector.iterator();
        while (stringIterator.hasNext()) {
            users[i] = stringIterator.next();
            i++;
        }
        return users;


    }

    public void writeLinusFollowers() throws IOException {
        UserService userService = new UserService(gitHubClient);
        PageIterator<User> userPageIterator = userService.pageFollowers("torvalds");
        Iterator<Collection<User>> userIterator = userPageIterator.iterator();
        Vector<String> userVector = new Vector<String>();

        FileWriter fileWriter = new FileWriter("linus_followers_usernames.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        int i = 0;
        while (userIterator.hasNext()) {

            Collection<User> users = userIterator.next();//Goes through pages
            for (User user : users) {
                i++;
                bufferedWriter.write(user.getLogin() + "\n");
                System.out.println(i);
            }
        }

        bufferedWriter.close();

    }

}
