package com.calendarConverter.utility;

import org.springframework.stereotype.Component;

//Operations used in converting from GC to EC
@Component
public class ECOperations {
    private final int baseECDD = 22;
    private final int baseECMM = 3;


    public int[] addDate (int[] ECDate, boolean exceptionDate) {
        ECDate[0] += baseECDD;
        ECDate[1] += baseECMM;
        ECDate[2] -= 8;

        formatter(ECDate);
        if (exceptionDate) {
            boolean pagme = ECDate[0] == 1 && ECDate[1] == 1;
            if (pagme) {
                ECDate[0] = 6;
                ECDate[1] = 13;
                ECDate[2]--;

                return ECDate;
            }
            ECDate[0]--;
            formatter(ECDate);
            return ECDate;
        }
        return ECDate;
    }

    public void formatter (int[] date) {
        while(date[0] > 30) {
            date[0] -= 30;
            date[1] += 1;
        }
        while (date[1] >= 13) {
            if (date[1] == 13 && date[0] > 5) {
                date[0] -= 5;
                date[1] -= 12;
                date[2] += 1;
            } else {
                date[0] -= 5;
                date[1] -= 12;
                date[2] += 1;
            }

        }
        while(date[0] <= 0) {
            if (date[1] == 1) {
                date[0]+= 5;
                date[1] = 13;
                date[2]--;
            } else {
                date[0] += 30;
                date[1]--;
            }
        }

    }
}
