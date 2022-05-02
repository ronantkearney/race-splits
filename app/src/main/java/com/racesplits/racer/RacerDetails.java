package com.racesplits.racer;

public class RacerDetails {

    String bib;
    String firstName;
    String lastName;
    String club;
    boolean isJunior;

    public RacerDetails(String bib, String firstName, String lastName, String club, boolean isJunior) {
        this.bib = bib;
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.isJunior = isJunior;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public boolean isJunior() {
        return isJunior;
    }

    public void setJunior(boolean junior) {
        isJunior = junior;
    }
}
