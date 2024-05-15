package com.racesplits.racer;

public class RacerDetails {

    String bib;
    String firstName;
    String lastName;
    String club;

    public RacerDetails(String bib, String firstName, String lastName, String club) {
        this.bib = bib;
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
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
}
