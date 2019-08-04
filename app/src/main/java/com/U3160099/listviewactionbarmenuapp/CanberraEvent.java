package com.U3160099.listviewactionbarmenuapp;

import java.util.Date;

class CanberraEvent {
    String id;
    String title;
    //int imageResource;
    String uri;
    String score;

    public CanberraEvent(String id, String title, String uri, String score) {
        this.id = id;
        this.title = title;
        this.uri = uri;
        this.score = score;
    }

    public CanberraEvent(String title, String uri, String score) {
        this.title = title;
        this.uri = uri;
        this.score = score;
    }

    public String getTitle(){
        return this.title;
    }

    public String getUri(){return this.uri;}

    public String getScore(){
        return this.score;
    }

    public String getId(){return this.id;}

    @Override
    public String toString(){
        return title;
    }
}
