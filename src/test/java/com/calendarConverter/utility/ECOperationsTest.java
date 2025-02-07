package com.calendarConverter.utility;

import com.calendarConverter.config.AppConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class ECOperationsTest {

    @Autowired
    private ECOperations ecOperations;

    @Autowired
    private Utility utility;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testingFormatter() {
        int [] testDate = {1,1,2000};
        int [] buffer = {0,0,0};
        int totalDays = 0;




        for (int i=1; i<=100; i++) {
            for (int j=1; j<=100; j++) {
               testDate[0] = j;
               testDate[1] = i;
               buffer = testDate.clone();
               ecOperations.formatter(testDate);
               if(!(utility.validateDate(testDate, Calendar.GC))) {
                   System.out.println("Unformatted date: {" + String.valueOf(buffer[0]) + ", " + String.valueOf(buffer[1]) + ", " + String.valueOf(buffer[2]) + "} Iterations: " + String.valueOf(totalDays));
                   System.out.println("Invalid date: {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));
                   break;
               }
               assertTrue(utility.validateDate(testDate, Calendar.GC));

               totalDays++;
            }
            if(!(utility.validateDate(testDate, Calendar.GC))) {
                break;
            }
        }
        System.out.println("Total days: " + totalDays);
        System.out.println("Final date: {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));
    }

    @Test
    void testingFormatterMonth() {
        int[] testDate = {6,13,2000};

        ECOperations ecOperations = new ECOperations();
        ecOperations.formatter(testDate);
        assertTrue(utility.validateDate(testDate, Calendar.GC));
        System.out.println("Final date: {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "}");

    }
}