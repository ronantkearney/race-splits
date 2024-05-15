package com.racesplits.race;

public class RaceLogEntry {

    private String bib;
    private String timeOfDay;
    private String splitTime;
    private String racerSplitNumber;
    private String racerName;

    public RaceLogEntry(String bib, String timeOfDay, String splitTime, String racerSplitNumber, String racerName) {
        this.bib = bib;
        this.timeOfDay = timeOfDay;
        this.splitTime = splitTime;
        this.racerSplitNumber = racerSplitNumber;
        this.racerName = racerName;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getSplitTime() {
        return splitTime;
    }

    public void setSplitTime(String splitTime) {
        this.splitTime = splitTime;
    }

    public String getRacerSplitNumber() {
        return racerSplitNumber;
    }

    public void setRacerSplitNumber(String racerSplitNumber) {
        this.racerSplitNumber = racerSplitNumber;
    }

    public String getRacerName() {
        return racerName;
    }

    public void setRacerName(String racerName) {
        this.racerName = racerName;
    }
}
