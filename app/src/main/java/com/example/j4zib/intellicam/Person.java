package com.example.j4zib.intellicam;

public class Person {
    private String name;
    private String imageURL;
    private int spam;

    public Person(){

    }

    public Person(String name, String imageURL, int spam) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public int getSpam() {
        return spam;
    }

    public void setSpam(int spam) {
        this.spam = spam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
