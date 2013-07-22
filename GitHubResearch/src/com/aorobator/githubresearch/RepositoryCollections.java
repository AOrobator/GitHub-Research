package com.aorobator.githubresearch;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.*;
import java.util.Vector;

/**
 * Returns different types of collections of repositories
 */
public class RepositoryCollections {

    private FileInputStream fileInputStream;
    private DataInputStream dataInputStream;
    private BufferedReader bufferedReader;
    private final String mostStarredReposFileLoc = "/Users/andreworobator/IdeaProjects/GitHubResearch/src/generated/mostPopularRepositories.txt";
    private RepositoryService repositoryService;

    public RepositoryCollections(GitHubClient client) {
        repositoryService = new RepositoryService(client);
    }

    /** @return repositories The top 50 most starred repositories as specified by https://github.com/popular/starred*/
    public Vector<Repository> getMostPopularRepos() throws IOException {

        Vector<Repository> repositories = new Vector<Repository>();

        fileInputStream = new FileInputStream(mostStarredReposFileLoc);
        dataInputStream = new DataInputStream(fileInputStream);
        bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

        String strLine;
        while ((strLine = bufferedReader.readLine()) != null) {
            String owner = strLine.split("/")[0];
            String repoName = strLine.split("/")[1];
            repositories.add(repositoryService.getRepository(owner, repoName));
        }

        return repositories;

    }
}
