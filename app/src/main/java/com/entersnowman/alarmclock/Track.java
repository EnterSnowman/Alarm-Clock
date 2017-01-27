package com.entersnowman.alarmclock;

/**
 * Created by Valentin on 27.01.2017.
 */

public class Track {
    private String path;
    private String author;
    private String name;

    public Track(String path, String author, String name) {
        this.path = path;
        this.author = author;
        this.name = name;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
