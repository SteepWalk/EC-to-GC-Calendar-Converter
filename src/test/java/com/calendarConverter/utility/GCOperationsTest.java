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

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class GCOperationsTest {

    @Autowired
    private GCOperations GCOperations;

    @Autowired
    private Utility utility;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testingFormatterND() {
        int [] testDate = {1,1,2000};
        int [] buffer = {0,0,0};
        int totalDays = 0;
        //compared to testDate to test formatterND
        int[] compareDate = {1,1,2000};


        // Convert int[] to Integer[] because asList doesn't work on primitives
        Integer[] boxedArray = IntStream.of(utility.getPlusGCMonths()).boxed().toArray(Integer[]::new);


        for (int i=1; i<=12; i++) {
            int dayLimit = 30;
            if(i == 2) {
                dayLimit = 29;
            }
            if(Arrays.asList(boxedArray).contains(i)) {
                System.out.println("i: " + i);
                dayLimit = 31;
            }
            for (int j=1; j<=dayLimit; j++) {
                compareDate[0] = j;
                compareDate[1] = i;
                buffer = testDate.clone();

                GCOperations.formatterND(testDate);
                boolean equalDates = (compareDate[0] == testDate[0] && compareDate[1] == testDate[1]);
                if(!(utility.validateDate(testDate, Calendar.GC))) {
                    System.out.println("Unformatted date: {" + String.valueOf(buffer[0]) + ", " + String.valueOf(buffer[1]) + ", " + String.valueOf(buffer[2]) + "} Iterations: " + String.valueOf(totalDays));
                    System.out.println("Invalid date: {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));
                    break;
                }
                if(!equalDates) {
                    System.out.println("testDate : {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));
                    System.out.println("compareDate : {" + String.valueOf(compareDate[0]) + ", " + String.valueOf(compareDate[1]) + ", " + String.valueOf(compareDate[2]) + "} Iterations: " + String.valueOf(totalDays));

                }
                assertTrue(utility.validateDate(testDate, Calendar.GC));
                testDate[0]+=1;
                totalDays++;
            }

        }
        System.out.println("Total days: " + totalDays);
        System.out.println("Final date: {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));
    }
}