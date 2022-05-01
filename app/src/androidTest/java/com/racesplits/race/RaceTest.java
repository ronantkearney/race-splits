package com.racesplits.race;

import junit.framework.TestCase;

import org.junit.Ignore;

import java.util.concurrent.TimeUnit;

public class RaceTest extends TestCase {

    @Ignore
    public void testGetSortedResults() {

        //Given
        Race race = new Race();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        for (int i=0; i<3; i++) {
            race.addTimeEntryForRacer("11");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            race.addTimeEntryForRacer("22");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            race.addTimeEntryForRacer("33");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        race.addTimeEntryForRacer("44");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        
        //When
        String sortedResults = race.getSortedResults();

        //Then
        System.out.println(sortedResults);

    }

    public void testGetNumberOfLaps() {
    }

    public void testGetWinningTime() {
    }
}