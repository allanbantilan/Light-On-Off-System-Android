package com.scoringapp.powersystem;

public class user {
    private String name;
    private String surname;

    public user() {
        // Default constructor required for Firestore
    }

    public user(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    // Getter and setter methods for the class variables
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
