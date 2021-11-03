package com.racesplits.race;

import java.time.Instant;

public class TimeEntry {

    private String bib;
    private Instant timeEntry;

    public TimeEntry(String bib, Instant timeEntry) {
        this.bib = bib;
        this.timeEntry = timeEntry;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public Instant getTimeEntry() {
        return timeEntry;
    }

    public void setTimeEntry(Instant timeEntry) {
        this.timeEntry = timeEntry;
    }
}
