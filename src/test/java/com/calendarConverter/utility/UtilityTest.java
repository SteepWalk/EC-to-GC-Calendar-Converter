package com.calendarConverter.utility;

import com.calendarConverter.config.AppConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class UtilityTest {

    @Autowired
    private Utility utility;



    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void everyValidDayOfTheEcCalendar() {
        int [] testDate = {1,1,2003};


        int totalDays = 0;

        //loops over every EC date and checks for validity match
        for (int i=1; i<=13; i++) {
            int dayLimit = 30;
            if (i==13) {
                dayLimit = 6;
            }
            for (int j=1; j<=dayLimit; j++) {
                testDate[0] = j;
                testDate[1] = i;
                totalDays++;
                System.out.println("Date : {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));

                assertTrue(utility.validateDate(testDate, Calendar.EC));


            }
        }
        System.out.println("Total num of days: " + totalDays);
    }

    @Test
    void everyValidDayOfTheGcCalendar() {
        int [] testDateGC = {1,1,2004};

        // Convert int[] to Integer[] because asList doesn't work on primitives
        Integer[] boxedArray = IntStream.of(utility.getPlusGCMonths()).boxed().toArray(Integer[]::new);
        int totalDaysGC = 0;

        for (int i=1; i<=12; i++) {
            int dayLimit = 30;
            if(i == 2) {
                dayLimit = 29;
            }
            if(Arrays.asList(boxedArray).contains(i)) {
                dayLimit = 31;
            }
            for (int j=1; j<=dayLimit; j++) {
                testDateGC[0] = j;
                testDateGC[1] = i;
                assertTrue(utility.validateDate(testDateGC, Calendar.GC));
                totalDaysGC++;
                System.out.println("Date : {" + String.valueOf(testDateGC[0]) + ", " + String.valueOf(testDateGC[1]) + ", " + String.valueOf(testDateGC[2]) + "} Iterations: " + String.valueOf(totalDaysGC));

            }
        }
        System.out.println("Total num of days: " + totalDaysGC);
    }

    @Test
    void checkECExceptionDates() {
        //used to repeat the for loop without increasing the value of i in the loop condition
        boolean loopRestart = true;
        int [] testDate = {1,1,2003};


        int totalDays = 0;

        //loops over every EC date and checks for validity match
        for (int i=1; i<=13; i++) {
            int dayLimit = 30;
            if (i==13) {
                dayLimit = 6;
            }
            for (int j=1; j<=dayLimit; j++) {
                testDate[0] = j;
                testDate[1] = i;

                if(utility.isException(testDate, Calendar.EC))
                {
                    System.out.println("Date : {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));
                    totalDays++;
                }

            }
            if(i == 13 && loopRestart) {
                i = 0;
                testDate[2] += 1;
                loopRestart = false;
            }

            if(totalDays > 400) {
                break;
            }
        }
        System.out.println("Total num of EC exception days: " + totalDays);

        int [] testDateGC = {1,1,2003};

        // Convert int[] to Integer[] because asList doesn't work on primitives
        Integer[] boxedArray = IntStream.of(utility.getPlusGCMonths()).boxed().toArray(Integer[]::new);
        int totalDaysGC = 0;
        loopRestart = true;
        for (int i=1; i<=12; i++) {
            int dayLimit = 30;
            if(i == 2) {
                dayLimit = (!loopRestart) ? 29 : 28;
                System.out.println("February dayLimit: " + dayLimit);
            }
            if(Arrays.asList(boxedArray).contains(i)) {
                dayLimit = 31;
            }
            for (int j=1; j<=dayLimit; j++) {
                testDateGC[0] = j;
                testDateGC[1] = i;

                if(utility.isException(testDateGC, Calendar.GC))
                {
                    System.out.println("Date : {" + String.valueOf(testDateGC[0]) + ", " + String.valueOf(testDateGC[1]) + ", " + String.valueOf(testDateGC[2]) + "} Iterations: " + String.valueOf(totalDaysGC));
                    totalDaysGC++;
                }
            }

            if(i == 12 && loopRestart) {
                i = 0;
                testDateGC[2] += 1;
                loopRestart = false;
            }

            if(totalDaysGC > 400) {
                break;
            }
        }

        System.out.println("Total num of GC exception days: " + totalDaysGC);

        assertEquals(totalDaysGC, totalDays);
    }
}

