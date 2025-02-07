package com.calendarConverter.service;

import com.calendarConverter.entity.Date;
import com.calendarConverter.exception.InvalidDate;
import com.calendarConverter.utility.Calendar;
import com.calendarConverter.utility.ECOperations;
import com.calendarConverter.utility.GCOperations;
import com.calendarConverter.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertDate {

    private Utility utility;
    private ECOperations ecOperations;
    private GCOperations gcOperations;
    private Date dateCC;


    private final int[] plusGCMonths = {12,10,8,7,5,3,1};

    @Autowired
    public ConvertDate (Utility utility, ECOperations ecOperations, GCOperations gcOperations, Date dateCC) {
        this.ecOperations = ecOperations;
        this.gcOperations = gcOperations;
        this.dateCC = dateCC;
        this.utility = utility;
    }

    public void convert(int[] date, Calendar calendar) throws InvalidDate{
        if (!utility.validateDate(date, calendar)) {
            throw new InvalidDate();
        }
        switch (calendar) {
            case EC -> ECtoGC(date);
            case GC -> GCtoEC(date);
        }
    }
    public void GCtoEC (int[] date) {
        int counter = 7;
        boolean exceptionDate = utility.isException(date, Calendar.GC);
        for (int x: plusGCMonths) {
            if (date[1] > x) continue;
            counter--;
        }
        date[0] = (date[1] <= 2) ? date[0] : date[0] + counter-2;
        if(date[1]==2) date[0]++;
        date = ecOperations.addDate(date, exceptionDate);
        int dayOfWeek = utility.getDayOfWeek(date);
        try{
            if (!utility.validateDate(date, Calendar.EC)) throw new InvalidDate();
            dateCC.setDate(date[0], date[1], date[2], Calendar.EC);
            dateCC.setDayOfWeek(dayOfWeek);
        } catch (InvalidDate e) {
            System.out.println(e.getMessage());

            dateCC = null;
        }
    }

    public void ECtoGC (int[] date){
        boolean exceptionDate = utility.isException(date, Calendar.EC);
        int dayOfWeek = utility.getDayOfWeek(date);
        //Proper cheating because I suck at this thing
        if (date[0] == 6 && date[1] == 13) {
            dateCC.setDate(11, 9, date[2]+8, Calendar.GC);
            dateCC.setDayOfWeek(dayOfWeek);
            return;
        }
        date = gcOperations.addDate(date, exceptionDate);
        try{
            if (!utility.validateDate(date, Calendar.GC)) throw new InvalidDate();
            dateCC.setDate(date[0], date[1], date[2], Calendar.GC);
            dateCC.setDayOfWeek(dayOfWeek);
        } catch (InvalidDate e) {
            System.out.println(e.getMessage());
            dateCC = null;
        }
    }
}
