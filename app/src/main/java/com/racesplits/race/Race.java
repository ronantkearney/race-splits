package com.racesplits.race;

import com.racesplits.racer.Racer;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Race {

    private Instant raceStart;
    private Map<String, Racer> racers;
    private ArrayList<RaceLogEntry> raceLog;

    public Race() {
        raceStart = Instant.now();
        racers = new HashMap();
        raceLog = new ArrayList<>();
    }

    public void addTimeEntryForRacer(String racerNumber) {
        Instant now = Instant.now();

        Racer racer = getRacer(racerNumber);
        racer.addSplitTime(raceStart, now);

        DateTimeFormatter timeOfDayFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
        Duration splitTime = racer.getLatestSplit().getDurationSincePreviousSplit();
        String splitTimeFormatted = String.format("%02d:%02d", splitTime.toMinutes(), splitTime.getSeconds() % 60);
        raceLog.add(new RaceLogEntry(racerNumber, timeOfDayFormat.format(now), splitTimeFormatted, String.valueOf(racer.getSplitCount()), racer.getName()));
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

    public String getRaceLog() {
        StringBuffer sb = new StringBuffer();
        for (RaceLogEntry raceLogEntry : raceLog) {
            sb.append(raceLogEntry.getBib()).append("\t");
            sb.append(raceLogEntry.getRacerSplitNumber()).append("\t");
            sb.append(raceLogEntry.getSplitTime()).append("\t");
            sb.append(raceLogEntry.getTimeOfDay()).append("\t");
            sb.append(raceLogEntry.getRacerName()).append("\n");
        }
        return sb.toString();
    }
}
