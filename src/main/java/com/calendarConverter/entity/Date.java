package com.calendarConverter.entity;

import com.calendarConverter.utility.Calendar;
import org.springframework.stereotype.Component;

@Component
public class Date{
    private int dd;
    private int mm;
    private int yy;
    private int dayOfWeek = 0;

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    private Calendar calendar;

    public Date () {
        dd = 1;
        mm = 1;
        yy = 2000;
    }

    public void setDate(int dd, int mm, int yy, Calendar calendar) {
        this.dd = dd;
        this.mm = mm;
        this.yy = yy;
        this.calendar = calendar;
    }
    public int[] getDate() {
        return new int[]{dd, mm, yy};
    }
    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {

        return "Date values in class Date  :{" + dd + ", " + mm + ", " + yy + "}";

    }
}
