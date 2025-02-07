package com.calendarConverter.utility;

import com.calendarConverter.entity.Date;
import com.calendarConverter.exception.InvalidDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Utility {
    @Autowired
    private Date dateCC;
    private final int[] plusGCMonths = {12,10,8,7,5,3,1};

    public int[] getPlusGCMonths() {
        return plusGCMonths.clone();
    }
    //this method returns 1 if date1 is greater, 2, if it lesser and 0 if equal
    public int compareDate (int[] date1, int[] date2) {
        if (date1[2] > date2[2]) return 1;
        if (date1[2] < date2[2]) return 2;
        if (date1[1] > date2[1]) return 1;
        if (date1[1] < date2[1]) return 2;
        if (date1[0] > date2[0]) return 1;
        if (date1[0] < date2[0]) return 2;
        return 0;
    }

    public boolean validateDate(int[] date, Calendar calendar) {
        switch (calendar) {
            case EC:
                if (date[0] > 0 && date[0] <= 30 && date[1] < 13 && date[1] > 0) {

                    return true;
                } else if (date[1] == 13) {
                    if ((date[2] + 1) % 4 == 0 && date[0] == 6) {

                        return true;
                    }
                    if (date[0] <= 5) {

                        return true;
                    }
                } else {

                    return false;
                }
                break;
            case GC:
                if (date[0] > 0 && date[1] <= 12 && date[1] > 0) {

                    boolean plusMonth = checkForPlusMonth(date[1], true);

                    if (date[1] == 2 & date[0] >= 29) {
                        if (date[0] == 29 && date[2] % 4 == 0) {

                            return true;
                        }

                        return false;
                    }
                    if (plusMonth) {

                        return date[0] <= 31;
                    } else {

                        return date[0] <= 30;
                    }
                }
        }

        return false;

    }

    public boolean checkForPlusMonth (int month, boolean forPlusMonth) {

        boolean plusMonth = false;
        boolean postPlusMonth = false;
        if (forPlusMonth) {
            for (int x: plusGCMonths) {
                if (month == x) {
                    plusMonth = true;
                }
            }

            return plusMonth;
        }

        for (int x: plusGCMonths) {
            if (month-1 == x || month == 1) {
                postPlusMonth = true;
            }
        }

        return postPlusMonth;
    }

    public boolean isException (int[] date, Calendar calendar) {

        int adjustedDate = date[2] + 1;

        if (calendar.equals(Calendar.GC)) {
            if (!((adjustedDate % 4 == 0) || (date[2] % 4 == 0))) {

                return false;
            }
            if (date[1] >= 9 && adjustedDate % 4 == 0  || (date[1] <= 2 && date[2] % 4 == 0)) {
                if ((date[1] == 9 && date[0] < 11)) {

                    return false;
                }

                return true;
            }
        } else if (calendar.equals(Calendar.EC)) {
            if (!(( adjustedDate % 4 == 0) || (date[2] % 4 == 0))) {

                return false;
            }

            if (date[1] == 13 && adjustedDate % 4 == 0 && date[0] == 6 || (date[1] <= 6 && date[2] % 4 == 0)) {

                return date[1] != 6 || date[0] <= 21;
            }
        }





        return false;
    }

    public int getDayOfWeek(int[] date) {

        int dayOfWeekY = 0;
        int dayOfWeek = 0;
        int y = date[2];
        int m = date[1];
        int d = date[0];
        int baseY = y-(y%4);
        //System.out.println("BaseY: " + baseY);
        int startY = baseY-(baseY%28);
        //System.out.println("StartY: " + startY);
        int baseDay = 0;

        {
            //find the day of the week the given year starts on
            int x = (baseY%28)/4;
            baseDay = ((2+(x*5))%7==0)? 7:(2+(x*5))%7;
            //System.out.println("BaseDay: " + baseDay);
            x = (baseDay +y%4)%7;
            dayOfWeekY = (x==0)?7:x;
            //System.out.println("First day of year: " + dayOfWeekY);
            //Convert day of month to day of year
            m-=1;
            d-=1;
            d = (30*m) +d;
            //Find the day of the week for the given date
            x = d%7;
            x = dayOfWeekY + x;
            dayOfWeek = (x%7==0) ?7 :x%7;
        }

        //System.out.println("The day is: " + dayOfWeek);
        return dayOfWeek;
    }
    public String setDayOfWeek() {
        switch (dateCC.getDayOfWeek()) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
        }
        return "Invalid Date";
    }
    public void throwInvalidDate() throws InvalidDate {
        throw new InvalidDate();
    }
}
