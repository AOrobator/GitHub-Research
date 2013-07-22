package com.aorobator.githubresearch;

import java.util.Date;

/**
 * The Commit class represents a single commit as it's associated metadata.
 *
 * @author Andrew Orobator
 */

public class Commit {

    final String commitHash;
    final Date commitDate;
    final String committer;
    final String author;
    final int comments;
    final int filesChanged;
    final int linesAdded;
    final int linesRemoved;
    final int linesModified;

    public Commit(String commitHash, Date commitDate, String committer, String author, int comments, int filesChanged, int linesAdded, int linesRemoved) {
        this.commitHash = commitHash;
        this.commitDate = commitDate;
        this.committer = committer;
        this.author = author;
        this.comments = comments;
        this.filesChanged = filesChanged;
        this.linesAdded = linesAdded;
        this.linesRemoved = linesRemoved;
        this.linesModified = linesAdded + linesRemoved;
    }



    @Override
    public String toString() {
        return commitHash + ";" + commitDate.toString() + ";" + committer + ";" + author + ";" + filesChanged + ";"
                + linesAdded + ";" + linesRemoved + ";" + linesModified + ";" + comments;
    }

}
