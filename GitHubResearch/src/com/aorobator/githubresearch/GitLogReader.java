package com.aorobator.githubresearch;

import java.io.*;
import java.util.Date;
import java.util.HashSet;


/**
 * A GitLogReader takes a git log plaintext file as input and parses it to create
 * Commit objects that it then writes to a csv file.
 */
public class GitLogReader {

    FileInputStream fileInputStream;
    DataInputStream dataInputStream;
    BufferedReader bufferedReader;
    String fileLocation = "/Users/andreworobator/Dropbox/Work_SU13/ruby-commits.txt";

    public void run() throws IOException {

        fileInputStream = new FileInputStream(fileLocation);
        dataInputStream = new DataInputStream(fileInputStream);
        bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

        FileWriter fileWriter = new FileWriter("ruby_commits.csv");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        int commitCount = 0;
        int filesChanged = 0;
        int insertions = 0;
        int deletions = 0;

        HashSet<String> authors = new HashSet<String>();

        String commitHash = "";
        Date commitDate = new Date();
        String committer = "Committer";
        String author = "";
        int comments = 0;

        String strLine;
        while ((strLine = bufferedReader.readLine()) != null) {
            if (strLine.startsWith("commit")) {
                commitCount++;
                commitHash = strLine.replace("commit ", "");
            } else if (strLine.startsWith("Author")) {
                author = strLine.split(" ")[1];
                authors.add(author);
            } else if (strLine.startsWith("Date:")) {
                String[] dateStrings = strLine.split(" ");
                int year = Integer.parseInt(dateStrings[7]) - 1900;
                int month = -1;
                String dateMonth = dateStrings[4];
                if (dateMonth.equals("Jan")) {
                    month = 0;
                } else if (dateMonth.equals("Feb")) {
                    month = 1;
                } else if (dateMonth.equals("Mar")) {
                    month = 2;
                } else if (dateMonth.equals("Apr")) {
                    month = 3;
                } else if (dateMonth.equals("May")) {
                    month = 4;
                } else if (dateMonth.equals("Jun")) {
                    month = 5;
                } else if (dateMonth.equals("Jul")) {
                    month = 6;
                } else if (dateMonth.equals("Aug")) {
                    month = 7;
                } else if (dateMonth.equals("Sep")) {
                    month = 8;
                } else if (dateMonth.equals("Oct")) {
                    month = 9;
                } else if (dateMonth.equals("Nov")) {
                    month = 10;
                } else if (dateMonth.equals("Dec")) {
                    month = 11;
                }

                int date = Integer.parseInt(dateStrings[5]);
                String[] time = dateStrings[6].split(":");
                int hrs = Integer.parseInt(time[0]);
                int min = Integer.parseInt(time[1]);
                int sec = Integer.parseInt(time[2]);
                commitDate = new Date(year, month, date, hrs, min, sec);

            } else if (strLine.contains("files changed") || strLine.contains("file changed") ||
                    (strLine.contains("insertion") && strLine.contains("(+)")) || (strLine.contains("deletion") && strLine.contains("(-)"))) {

                //Gets rid of extra unneccesary spaces
                strLine = strLine.replaceAll("\\s+", " ");

                String[] modifications = strLine.split(" ");
                if (strLine.contains("files changed") || strLine.contains("file changed")) {
                    filesChanged = Integer.parseInt(modifications[1]);
                }
                if (strLine.contains("insertion")) {
                    insertions = Integer.parseInt(modifications[4]);
                }
                if (strLine.contains("deletion")) {
                    if (modifications.length == 6) {
                        deletions = Integer.parseInt(modifications[4]);
                    } else {
                        deletions = Integer.parseInt(modifications[6]);
                    }
                }
                bufferedWriter.write(new Commit(commitHash, commitDate, committer, author, comments, filesChanged, insertions, deletions).toString() + "\n");
            }

            //Reset for the next iteration
            filesChanged = 0;
            insertions = 0;
            deletions = 0;

        }
        bufferedWriter.close();
        System.out.println();
        System.out.println("Found " + commitCount + " commits.");
        System.out.println("Found " + authors.size() + " authors");


    }
}