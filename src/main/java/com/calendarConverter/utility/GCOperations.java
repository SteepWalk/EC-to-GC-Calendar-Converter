package com.calendarConverter.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//Operations used in converting EC to GC
@Component
public class GCOperations {
    private final int baseGCDD = 10;
    private final int baseGCMM = 8;

    private final int[] plusGCMonths = {12,10,8,7,5,3,1};


    private Utility utility;


    @Autowired
    public GCOperations (Utility utility) {
        this.utility = utility;
    }


    public int[] addDate (int[] GCDate, boolean exceptionDate) {
        GCDate[0] += baseGCDD;
        GCDate[1] += baseGCMM;
        GCDate[2] += 7;
        //System.out.println("Added Date: {" + GCDate[0] + ", " + GCDate[1] + ", " + GCDate[2] + "}");
        int temp = GCDate[2];
        formatterST(GCDate);
        //System.out.println("FormatterST Date: {" + GCDate[0] + ", " + GCDate[1] + ", " + GCDate[2] + "}");

        int counter = 7;
        for (int x: plusGCMonths) {
            if (GCDate[1] > x) continue;
            counter--;
        }
        if (temp == GCDate[2]) {
            GCDate[0] = GCDate[0] - counter+5;
        } else {
            GCDate[0] = (GCDate[1] <= 2) ? GCDate[0] : GCDate[0] - counter;
            if(GCDate[1]==2) GCDate[0] -= 3; else if (GCDate[1] == 1) GCDate[0] -= 2;
        }
        //System.out.println("After counters Date: {" + GCDate[0] + ", " + GCDate[1] + ", " + GCDate[2] + "}");

        formatterND(GCDate);
        //System.out.println("FormatterND Date: {" + GCDate[0] + ", " + GCDate[1] + ", " + GCDate[2] + "}");

        if (exceptionDate) {
            //Has to be unnecessarily complicated because I bashed the whole thing
           boolean feb = (GCDate[1] ==2 && GCDate[0] == 29) || (GCDate[1] == 9 && GCDate[0] == 11 && (GCDate[2]+2)%4==0);
            if (feb) {
                return GCDate;
            }
            GCDate[0]++;
        }
        return formatterND(GCDate);
    }

    public void formatterST(int[] date) {

        while(date[0] > 30) {
            date[0] -= 30;
            date[1] += 1;
        }
        while (date[1] > 12) {
            date[1] -= 12;
            date[2] += 1;
        }

    }

    public int[] formatterND(int[] date) {
        if(utility.validateDate(date, Calendar.GC)) {
            return date;
        }
        boolean forceExit = false;
        int counterForForceExit = 0;

        // Handle leap years - if the year is a leap year, adjust February
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; // Days in each month

        // Only adjust invalid dates
        if (utility.validateDate(date, Calendar.GC)) {
            return date; // If the date is valid, no need to proceed
        }

        while (!utility.validateDate(date, Calendar.GC) && !forceExit) {
            // Leap year check
            boolean isLeapYear = (date[2] % 4 == 0 && date[2] % 100 != 0) || (date[2] % 400 == 0);
            if (isLeapYear) {
                daysInMonth[1] = 29;
            } else {
                daysInMonth[1] = 28;
            }

            // Handle month overflow
            while (date[1] > 12) {
                date[1] -= 12;
                date[2] += 1;
            }

            // Handle negative or zero day values
            while (date[0] <= 0) {
                date[1] -= 1; // Move to the previous month
                if (date[1] < 1) {
                    date[1] = 12;
                    date[2] -= 1;
                    isLeapYear = (date[2] % 4 == 0 && date[2] % 100 != 0) || (date[2] % 400 == 0);
                    daysInMonth[1] = isLeapYear ? 29 : 28;
                }
                date[0] += daysInMonth[date[1] - 1]; // Add days from the previous month
            }

            // Handle day overflow within a month
            while (date[0] > daysInMonth[date[1] - 1]) {
                date[0] -= daysInMonth[date[1] - 1];
                date[1] += 1;
                if (date[1] > 12) {
                    date[1] = 1;
                    date[2] += 1;
                    isLeapYear = (date[2] % 4 == 0 && date[2] % 100 != 0) || (date[2] % 400 == 0);
                    daysInMonth[1] = isLeapYear ? 29 : 28;
                }
            }

            // Exit condition to avoid an infinite loop
            counterForForceExit += 1;
            if (counterForForceExit > 100) {
                System.out.println("Force exit after 100 iterations. Date: {" + date[0] + ", " + date[1] + ", " + date[2] + "}");
                forceExit = true;
            }
        }

        return date;
    }





}
