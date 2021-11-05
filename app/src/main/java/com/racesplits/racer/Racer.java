package com.racesplits.racer;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Racer {

    private String bib;
    private String name;
    private ArrayList<RacerSplitTime> splitTimes;
    private Duration differenceFromWinningTime;

    public Racer(String bib) {
        this.bib = bib;
        splitTimes = new ArrayList();
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<RacerSplitTime> getSplitTimes() {
        return splitTimes;
    }

    public int getSplitCount() {
        return splitTimes.size();
    }

    public void addSplitTime(Instant raceStart, Instant timeEntry) {
        RacerSplitTime racerSplitTime = calculateSplitTime(raceStart, timeEntry);
        splitTimes.add(racerSplitTime);
    }

    private RacerSplitTime calculateSplitTime(Instant raceStart, Instant timeEntry) {
        Instant previousSplit = getPreviousSplit(raceStart);
        return new RacerSplitTime(timeEntry, Duration.between(raceStart, timeEntry), Duration.between(previousSplit, timeEntry));
    }

    public RacerSplitTime getLatestSplit() {
        int splitCount = getSplitCount();
        if (splitCount == 0) {
            return null;
        } else {
            return splitTimes.get(splitCount - 1);
        }
    }

    private Instant getPreviousSplit(Instant raceStart) {

        int splitCount = getSplitCount();
        if (splitCount == 0) {
            return raceStart;
        } else {
            RacerSplitTime racerSplitTime = splitTimes.get(splitCount - 1);
            return racerSplitTime.getSplitTime();
        }
    }


}
