package com.racesplits.racer;

import java.time.Duration;
import java.time.Instant;

public class RacerSplitTime {

    private Instant splitTime;
    private Duration durationSinceStart;
    private Duration durationSincePreviousSplit;

    public RacerSplitTime(Instant splitTime, Duration durationSinceStart, Duration durationSincePreviousSplit) {
        this.splitTime = splitTime;
        this.durationSinceStart = durationSinceStart;
        this.durationSincePreviousSplit = durationSincePreviousSplit;
    }

    public Instant getSplitTime() {
        return splitTime;
    }

    public void setSplitTime(Instant splitTime) {
        this.splitTime = splitTime;
    }

    public Duration getDurationSinceStart() {
        return durationSinceStart;
    }

    public void setDurationSinceStart(Duration durationSinceStart) {
        this.durationSinceStart = durationSinceStart;
    }

    public Duration getDurationSincePreviousSplit() {
        return durationSincePreviousSplit;
    }

    public void setDurationSincePreviousSplit(Duration durationSincePreviousSplit) {
        this.durationSincePreviousSplit = durationSincePreviousSplit;
    }
}
