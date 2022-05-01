package com.racesplits.race;

import com.racesplits.racer.Racer;
import com.racesplits.racer.RacerSplitTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Race {

    private static String TIME_OF_DAY_FORMAT = "HH:mm:ss";
    private static String SPLIT_TIME_FORMAT = "%02d:%02d";
    private static String COMMA = ",";
    private static String NEWLINE = "\n";
    private static int SECONDS_IN_A_MINUTE = 60;

    private Instant raceStart;
    private Map<String, Racer> racers;
    private Map<String, String> racerNames;
    private ArrayList<RaceLogEntry> raceLog;

    public Race() {
        raceStart = Instant.now();
        racers = new HashMap();
        racerNames = new HashMap();
        raceLog = new ArrayList<>();
    }

    public void addTimeEntryForRacer(String racerNumber) {
        Instant raceTimeEntry = Instant.now();

        Racer racer = getRacer(racerNumber);
        racer.addSplitTime(raceStart, raceTimeEntry);

        DateTimeFormatter timeOfDayFormat = DateTimeFormatter.ofPattern(TIME_OF_DAY_FORMAT).withZone(ZoneId.systemDefault());
        Duration splitTime = racer.getLatestSplit().getDurationSincePreviousSplit();
        String splitTimeFormatted = String.format(SPLIT_TIME_FORMAT, splitTime.toMinutes(), splitTime.getSeconds() % SECONDS_IN_A_MINUTE);
        raceLog.add(new RaceLogEntry(racerNumber, timeOfDayFormat.format(raceTimeEntry), splitTimeFormatted, String.valueOf(racer.getSplitCount()), racer.getName()));
    }

    public Racer getRacer(String racerNumber) {
        if (racers.containsKey(racerNumber)) {
            return racers.get(racerNumber);
        } else {
            Racer racer = new Racer(racerNumber);
            racer.setName(racerNames.get(racerNumber));
            racers.put(racerNumber, racer);
            return racer;
        }
    }

    public void putRacerName(String racerNumber, String racerName) {
        if (!racerNames.containsKey(racerNumber)) {
            racerNames.put(racerNumber, racerName);
        }
    }

    public String getRacerName(String racerNumber) {
        if (racerNames.containsKey(racerNumber)) {
            return racerNames.get(racerNumber);
        } else {
            return "Unknown";
        }
    }

    public String getRaceLog() {
        StringBuffer sb = new StringBuffer();
        sb.append("Racer Number").append(COMMA);
        sb.append("Split Number").append(COMMA);
        sb.append("Split Time").append(COMMA);
        sb.append("Time Of Day").append(COMMA);
        sb.append("Racer Name").append(NEWLINE);

        for (RaceLogEntry raceLogEntry : raceLog) {
            sb.append(raceLogEntry.getBib()).append(COMMA);
            sb.append(raceLogEntry.getRacerSplitNumber()).append(COMMA);
            sb.append(raceLogEntry.getSplitTime()).append(COMMA);
            sb.append(raceLogEntry.getTimeOfDay()).append(COMMA);
            sb.append(raceLogEntry.getRacerName()).append(NEWLINE);
        }
        return sb.toString();
    }

    public String getSortedResults() {
        int raceNumberOfLaps = getNumberOfLaps();
        Instant winningTime = getWinningTime(raceNumberOfLaps);

        Stream<Racer> finshers = racers.values().stream()
                .filter(racer -> !racer.getLatestSplit().getSplitTime().isBefore(winningTime))
                .sorted(Comparator.comparing(r -> r.getLatestSplit().getSplitTime()));

        Stream<Racer> nonFinishers = racers.values().stream()
                .filter(racer -> racer.getLatestSplit().getSplitTime().isBefore(winningTime))
                .sorted(Comparator.comparing(r -> r.getLatestSplit().getSplitTime()));

        List<Racer> sortedResults = Stream.concat(finshers, nonFinishers).collect(Collectors.toList());

        StringBuffer sb = new StringBuffer();
        sb.append("Position").append(COMMA);
        sb.append("Racer Number").append(COMMA);
        sb.append("Name").append(COMMA);
        sb.append("Winner Diff").append(COMMA);
        for (int i=1; i<=raceNumberOfLaps; i++) {
            sb.append("Split "+i);
            if (i<raceNumberOfLaps) {
                sb.append(COMMA);
            } else {
                sb.append(NEWLINE);
            }
        }

        int position=1;
        for (Racer racer : sortedResults) {
            sb.append(position).append(COMMA);
            position++;
            sb.append(racer.getBib()).append(COMMA);
            sb.append(racer.getName()).append(COMMA);

            Instant racerLastSplit = racer.getLatestSplit().getSplitTime();
            if (racerLastSplit.isBefore(winningTime)) {
                sb.append("DNF");
            } else {
                Duration winnerDiff = Duration.between(winningTime, racerLastSplit);
                String winnerDiffFormatted = String.format(SPLIT_TIME_FORMAT, winnerDiff.toMinutes(),
                        winnerDiff.getSeconds() % SECONDS_IN_A_MINUTE);
                sb.append(winnerDiffFormatted);
            }
            sb.append(COMMA);

            ArrayList<RacerSplitTime> splitTimes = racer.getSplitTimes();
            for (int i=0;i<splitTimes.size();i++) {
                Duration durationSincePreviousSplit = splitTimes.get(i).getDurationSincePreviousSplit();
                String splitTimeFormatted = String.format(SPLIT_TIME_FORMAT, durationSincePreviousSplit.toMinutes(),
                        durationSincePreviousSplit.getSeconds() % SECONDS_IN_A_MINUTE);
                if (i == splitTimes.size() - 1) {
                    sb.append(splitTimeFormatted).append(NEWLINE);
                } else {
                    sb.append(splitTimeFormatted).append(COMMA);
                }
            }
        }
        return sb.toString();
    }

    public int getNumberOfLaps() {
        Map<Integer, Long> splitFrequncy = racers.values().stream()
                .mapToInt(Racer::getSplitCount).boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Collections.max(splitFrequncy.entrySet(), Map.Entry.comparingByValue()).getKey().intValue();
    }

    public Instant getWinningTime(int raceNumberOfLaps) {
        List<Instant> finishersLastSpilts = racers.values().stream()
                .filter(racer -> racer.getSplitCount() == raceNumberOfLaps)
                .map(Racer::getLatestSplit)
                .map(RacerSplitTime::getSplitTime)
                .collect(Collectors.toList());

        return Collections.min(finishersLastSpilts);
    }

}
