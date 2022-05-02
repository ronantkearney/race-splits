package com.racesplits;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import static org.junit.Assert.*;

import com.racesplits.race.Race;
import com.racesplits.racer.CompetingRacer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
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
        List<CompetingRacer> sortedResults = race.getSortedResults();
        String formattedResultsStr = race.formatResultsAsString(sortedResults);
        Workbook formattedResultsXls = race.formatResultsAsXls(sortedResults);

        //Then
        System.out.println(formattedResultsStr);

    }
}