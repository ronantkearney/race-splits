package com.racesplits.race;

import com.racesplits.racer.CompetingRacer;
import com.racesplits.racer.RacerDetails;
import com.racesplits.racer.RacerSplitTime;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private Map<String, CompetingRacer> competingRacers;
    private Map<String, RacerDetails> registeredRacers;
    private ArrayList<RaceLogEntry> raceLog;

    public Race() {
        raceStart = Instant.now();
        competingRacers = new HashMap();
        registeredRacers = new HashMap();
        raceLog = new ArrayList<>();
    }

    public void addTimeEntryForRacer(String racerNumber) {
        Instant raceTimeEntry = Instant.now();

        CompetingRacer racer = getRacer(racerNumber);
        racer.addSplitTime(raceStart, raceTimeEntry);

        DateTimeFormatter timeOfDayFormat = DateTimeFormatter.ofPattern(TIME_OF_DAY_FORMAT).withZone(ZoneId.systemDefault());
        Duration splitTime = racer.getLatestSplit().getDurationSincePreviousSplit();
        String splitTimeFormatted = String.format(SPLIT_TIME_FORMAT, splitTime.toMinutes(), splitTime.getSeconds() % SECONDS_IN_A_MINUTE);
        raceLog.add(new RaceLogEntry(racerNumber, timeOfDayFormat.format(raceTimeEntry), splitTimeFormatted, String.valueOf(racer.numberOfSplitsForRacer()),
                racer.getRacerDetails().getFirstName() + " " + racer.getRacerDetails().getLastName()));
    }

    public CompetingRacer getRacer(String racerNumber) {
        if (competingRacers.containsKey(racerNumber)) {
            return competingRacers.get(racerNumber);
        } else {
            if (registeredRacers.containsKey(racerNumber)) {
                RacerDetails racerDetails = registeredRacers.get(racerNumber);
                CompetingRacer competingRacer = new CompetingRacer(racerDetails);
                competingRacers.put(racerNumber, competingRacer);
                return competingRacer;
            } else {
                RacerDetails unknownRacer = new RacerDetails(racerNumber,
                        "unknown", "unknown", "unknown");
                CompetingRacer competingRacer = new CompetingRacer(unknownRacer);
                competingRacers.put(racerNumber, competingRacer);
                return competingRacer;
            }
        }
    }

    public void addRegisteredRacer(String racerNumber, String firstName, String lastName, String club) {
        if (!registeredRacers.containsKey(racerNumber)) {
            RacerDetails racerDetails = new RacerDetails(racerNumber, firstName, lastName, club);
            registeredRacers.put(racerNumber, racerDetails);
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

    public List<CompetingRacer> getSortedResults() {
        int raceNumberOfLaps = getNumberOfLaps();
        Instant winningTime = getWinningTime(raceNumberOfLaps);

        Stream<CompetingRacer> finishers = competingRacers.values().stream()
                .filter(racer -> racer.numberOfSplitsForRacer() >= raceNumberOfLaps)
                .filter(racer -> !racer.getLatestSplit().getSplitTime().isBefore(winningTime))
                .sorted(Comparator.comparing(r -> r.getLatestSplit().getSplitTime()));

        Stream<CompetingRacer> nonFinishers = competingRacers.values().stream()
                .filter(racer -> racer.numberOfSplitsForRacer() < raceNumberOfLaps)
                .filter(racer -> racer.getLatestSplit().getSplitTime().isBefore(winningTime))
                .sorted(Comparator.comparing(r -> r.getLatestSplit().getSplitTime()));

        return Stream.concat(finishers, nonFinishers).collect(Collectors.toList());
    }

    public String formatResultsAsString(List<CompetingRacer> sortedRacerResultList) {

        int raceNumberOfLaps = getNumberOfLaps();
        Instant winningTime = getWinningTime(raceNumberOfLaps);

        StringBuffer sb = new StringBuffer();
        sb.append("Position").append(COMMA);
        sb.append("Bib").append(COMMA);
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
        for (CompetingRacer racer : sortedRacerResultList) {
            sb.append(position).append(COMMA);
            position++;
            sb.append(racer.getRacerDetails().getBib()).append(COMMA);
            sb.append(racer.getRacerDetails().getFirstName() + " " + racer.getRacerDetails().getLastName()).append(COMMA);
            sb.append(racer.getRacerDetails().getClub()).append(COMMA);

            Instant racerLastSplit = racer.getLatestSplit().getSplitTime();
            if (racerLastSplit.isBefore(winningTime)) {
                sb.append("+LAP");
            } else {
                Duration winnerDiff = Duration.between(winningTime, racerLastSplit);
                String winnerDiffFormatted = String.format(SPLIT_TIME_FORMAT, winnerDiff.toMinutes(),
                        winnerDiff.getSeconds() % SECONDS_IN_A_MINUTE);
                sb.append(winnerDiffFormatted);
            }
            sb.append(COMMA);

            List<RacerSplitTime> splitTimes = racer.getSplitTimes();
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
        Map<Integer, Long> splitFrequncy = competingRacers.values().stream()
                .mapToInt(CompetingRacer::numberOfSplitsForRacer).boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Collections.max(splitFrequncy.entrySet(), Map.Entry.comparingByValue()).getKey().intValue();
    }

    public Instant getWinningTime(int raceNumberOfLaps) {
        List<Instant> finishersLastSpilts = competingRacers.values().stream()
                .filter(racer -> racer.numberOfSplitsForRacer() == raceNumberOfLaps)
                .map(CompetingRacer::getLatestSplit)
                .map(RacerSplitTime::getSplitTime)
                .collect(Collectors.toList());

        return Collections.min(finishersLastSpilts);
    }

}
