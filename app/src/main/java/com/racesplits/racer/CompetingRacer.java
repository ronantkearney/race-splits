package com.racesplits.racer;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CompetingRacer {

    private RacerDetails racerDetails;
    private List<RacerSplitTime> splitTimes;

    public CompetingRacer(RacerDetails racer) {
        this.racerDetails = racer;
        splitTimes = new ArrayList();
    }

    public List<RacerSplitTime> getSplitTimes() {
        return splitTimes;
    }

    public int numberOfSplitsForRacer() {
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
        return numberOfSplitsForRacer() > 0
                ? splitTimes.get(numberOfSplitsForRacer() - 1)
                : null;
    }

    private Instant getPreviousSplit(Instant raceStart) {

        int splitCount = numberOfSplitsForRacer();
        if (splitCount == 0) {
            return raceStart;
        } else {
            RacerSplitTime racerSplitTime = splitTimes.get(splitCount - 1);
            return racerSplitTime.getSplitTime();
        }
    }

    public RacerDetails getRacerDetails() {
        return racerDetails;
    }

    public void setRacerDetails(RacerDetails racerDetails) {
        this.racerDetails = racerDetails;
    }
}
