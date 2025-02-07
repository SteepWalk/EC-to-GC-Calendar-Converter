package com.calendarConverter.service;

import com.calendarConverter.config.AppConfig;
import com.calendarConverter.entity.Date;
import com.calendarConverter.exception.InvalidDate;
import com.calendarConverter.utility.Calendar;
import com.calendarConverter.utility.Utility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class ConvertDateTest {

    @Autowired
    private ConvertDate convertDate;

    @Autowired
    private Utility utility;

    @Autowired
    Date date;

    @BeforeEach
    void setUp() {
        int[] checkDate = date.getDate();
        System.out.println("This the setUp method and this is the Date : {" + String.valueOf(checkDate[0]) + ", " + String.valueOf(checkDate[1]) + ", " + String.valueOf(checkDate[2]) + "}");
        int[] testDate = {8, 11, 2010};
        System.out.println(utility.validateDate(testDate, Calendar.GC));
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void convertToDayOfWeek() {
        int dayOfWeekY = 0;
        int dayOfWeek = 0;
        int y = 2016;
        int m = 13;
        int d = 5;
        int baseY = y-(y%4);
        System.out.println("BaseY: " + baseY);
        int startY = baseY-(baseY%28);
        System.out.println("StartY: " + startY);
        int baseDay = 0;

        {
            //find the day of the week the given year starts on
            int x = (baseY%28)/4;
            baseDay = ((2+(x*5))%7==0)? 7:(2+(x*5))%7;
            System.out.println("BaseDay: " + baseDay);
            x = (baseDay +y%4)%7;
            dayOfWeekY = (x==0)?7:x;
            System.out.println("First day of year: " + dayOfWeekY);
            //Convert day of month to day of year
            m-=1;
            d-=1;
            d = (30*m) +d;
            //Find the day of the week for the given date
            x = d%7;
            x = dayOfWeekY + x;
            dayOfWeek = (x%7==0) ?7 :x%7;
        }

        System.out.println("The day is: " + dayOfWeek);
    }
    @Test
    void testForECDays21DD2MM() throws InvalidDate {
        int[] problemDate = {11, 9, 2003};
        convertDate.convert(problemDate, Calendar.EC);
        problemDate = date.getDate();
        System.out.println("GC Date: {" + problemDate[0] + ", " + problemDate[1] + ", " + problemDate[2] + "}");
    }
    //Loops over every EC date, and one at a time, converts each to GC date using the ConvertDate class, then
    //converts each resulting GC date back to EC while checking if two results match
    @Test
    void ECtoGCtoEC() throws InvalidDate {
        int [] testDate = {1,1,2000};
        int [] testDateGC = {1,1,2003};


        int totalDays = 0;

        int loopRestart = 0;
        for (int i=1; i<=13; i++) {
            int dayLimit = 30;
            if (i==13) {
                dayLimit = ((testDate[2]+1)%4 == 0) ? 6 : 5;
            }
            for (int j=1; j<=dayLimit; j++) {
                testDate[0] = j;
                testDate[1] = i;
                totalDays++;
                System.out.println("Date : {" + String.valueOf(testDate[0]) + ", " + String.valueOf(testDate[1]) + ", " + String.valueOf(testDate[2]) + "} Iterations: " + String.valueOf(totalDays));
                convertDate.convert(testDate.clone(), Calendar.EC);
                testDateGC = date.getDate();
                System.out.println("DateGC : {" + String.valueOf(testDateGC[0]) + ", " + String.valueOf(testDateGC[1]) + ", " + String.valueOf(testDateGC[2]) + "} Iterations: " + String.valueOf(totalDays));

                convertDate.convert(testDateGC.clone(), Calendar.GC);
                int[] bufferDate = date.getDate();
                System.out.println("Date : {" + String.valueOf(bufferDate[0]) + ", " + String.valueOf(bufferDate[1]) + ", " + String.valueOf(bufferDate[2]) + "} Iterations: " + String.valueOf(totalDays));

                assertEquals(bufferDate[0], testDate[0]);
                assertEquals(bufferDate[1], testDate[1]);
                assertEquals(bufferDate[2], testDate[2]);
            }

            if(i == 13 && loopRestart <= 5) {
                i = 0;
                testDate[2] += 1;
                loopRestart += 1;
            }
        }
    }
}