package com.aorobator.githubresearch;

import org.eclipse.egit.github.core.client.GitHubClient;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        String userName;
        String password;

        String credentialsLocation = "/Users/andreworobator/IdeaProjects/GitHubResearch/src/credentials.txt";

        FileInputStream fileInputStream = new FileInputStream(credentialsLocation);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

        userName = bufferedReader.readLine();
        password = bufferedReader.readLine();


        //Authentication
        GitHubClient client = new GitHubClient();
        client.setCredentials(userName, password);


    }
}
