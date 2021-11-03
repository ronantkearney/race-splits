package com.racesplits.race;

import com.racesplits.racer.Racer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Race {

    private Instant raceStart;
    private Map<String, Racer> racers;
    private ArrayList<TimeEntry> raceLog;

    public Race() {
        raceStart = Instant.now();
        racers = new HashMap();
        raceLog = new ArrayList<>();
    }

    public void addTimeEntryForRacer(String racerNumber) {
        Instant now = Instant.now();

        Racer racer = getRacer(racerNumber);
        racer.addSplitTime(raceStart, now);
        raceLog.add(new TimeEntry(racerNumber, now));
    }

    public Racer getRacer(String racerNumber) {
        if (racers.containsKey(racerNumber)) {
            return racers.get(racerNumber);
        } else {
            Racer racer = new Racer(racerNumber);
            racers.put(racerNumber, racer);
            return racer;
        }
    }
}
