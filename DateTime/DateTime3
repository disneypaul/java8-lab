package DateTime;

import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime3 {
   // LocalDate lDate5 = LocalDate.now();

    public static void main(String[] args){
        Date date = new Date();
        LocalDate lDate = LocalDate.now();
        LocalTime lTime = LocalTime.now();

        System.out.printf("Date [%s] lDate [%s] lTime [%s]",date,lDate,lTime);

        /*Sample 2 : Date manipulation */
        LocalDateTime lDateTime1 = LocalDateTime.of(lDate,lTime);
        LocalDateTime lDateTime2 = LocalDateTime.of(2014, Month.APRIL, 18, 12, 45,20);
        LocalDateTime lDateTime3 = lDate.atTime(lTime);
        System.out.printf("\nlDate1 [%s] lDate2 [%s] lDate3 [%s] \n",lDateTime1,lDateTime2,lDateTime3);
        System.out.println("Date ["+lDateTime1+"] +10 days ["+lDateTime1.plusDays(10)+"] -2 years ["+lDateTime1.minusYears(2)+"] +5 months ["+lDateTime1.plus(5, ChronoUnit.MONTHS)+"]");
        /*End of Sample 3*/


        /*Sample 3 : Date related and absolute sample */
        LocalDate lDate2 = LocalDate.of(2014,3,18);
        System.out.println("Before manupulation LDate 2 ["+lDate2+"]");
        lDate2 = lDate2.with(ChronoField.MONTH_OF_YEAR,9);
        lDate2 = lDate2.plusYears(2).minusDays(10);
        lDate2.withYear(2011);      //Immutable assignment - Hence lDate2 object value intact
        System.out.println("After manupulation LDate 2 ["+lDate2+"]");
        /*End of Sample 3 - After manupulation LDate 2 [2016-09-08]*/

        /*Sample 4 - Date collection using TemporalAdjusters */
        /* 4.1 - ThanksGiving day of 2018 - its on 4th thursday in the month of November */
        LocalDate lDate4 = LocalDate.of(2018,9,10);
        lDate4 = lDate4.with(ChronoField.MONTH_OF_YEAR,11).with(TemporalAdjusters.firstInMonth(DayOfWeek.THURSDAY)).plusWeeks(3);
        System.out.println("2018 ThanksGiving on ["+lDate4+"]");

        /* 4.2 - Working days in a week - its on 4th thursday in the month of November */
        LocalDate lDate5 = LocalDate.now().minusDays(2);
        DateTime3 dateTime3Obj = new DateTime3();
       // NextWorkingDay nxtWD = new NextWorkingDay();
        System.out.println("Today ["+lDate5+"] - Next working day ["+lDate5.with(temp -> {
            DayOfWeek dow = DayOfWeek.of(temp.get(ChronoField.DAY_OF_WEEK));
            int days2Add = 1;
            if(dow == DayOfWeek.FRIDAY)
                days2Add = 3;
            else if(dow == DayOfWeek.SATURDAY)
                days2Add = 2;
            return temp.plus(days2Add,ChronoUnit.DAYS);
                }
        )+"]");

        //TimeZone - https://www.mkyong.com/java8/java-display-all-zoneid-and-its-utc-offset/
        ZoneId ist = ZoneId.of("Asia/Kolkata");
        ZoneId uae = ZoneId.of("Asia/Dubai");
        ZoneId chicago = ZoneId.of("America/Chicago");
        ZoneId cherryHill = TimeZone.getDefault().toZoneId();
        System.out.println("Default TZ ["+cherryHill.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)+"]");
        LocalDateTime lDate6 = LocalDateTime.now();
        ZonedDateTime istTime = lDate6.atZone(ist);
        System.out.println("Itinary - Startd from ["+ist.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)+"] at local time ["+istTime+"] " +
                "reaches transit station ["+uae.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)+"] after 4hrs ["+lDate6.plus(4,ChronoUnit.HOURS).atZone(uae)+"] " +
                "starts from trasit station ["+uae.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)+"] at ["+lDate6.plus(6,ChronoUnit.HOURS).atZone(uae)+"] and " +
                "reaches destination ["+chicago.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)+"] after 14 hrs at local time ["+lDate6.plus(20,ChronoUnit.HOURS).atZone(chicago)+"]");


        //Calender
        HijrahDate ramdanDate = HijrahDate.now().with(ChronoField.DAY_OF_MONTH,1).with(ChronoField.MONTH_OF_YEAR,9);
        System.out.println("Ramdan starts on ["+ramdanDate+"] and ends on ["+ramdanDate.with(TemporalAdjusters.lastDayOfMonth())+"]");
        System.out.println("Ramdan starts on ["+ IsoChronology.INSTANCE.date(ramdanDate)+"] and ends on ["+ IsoChronology.INSTANCE.date(ramdanDate.with(TemporalAdjusters.lastDayOfMonth()))+"]");
            /**Ramdan starts on [Hijrah-umalqura AH 1440-09-01] and ends on [Hijrah-umalqura AH 1440-09-29]
             Ramdan starts on [2019-05-06] and ends on [2019-06-03]
             **/
        /*End of Sample 4*/
    }

 /* This class converted to Lambda in-line method
 public class NextWorkingDay implements TemporalAdjuster{
        @Override
        public Temporal adjustInto(Temporal temp){
            DayOfWeek dow = DayOfWeek.of(temp.get(ChronoField.DAY_OF_WEEK));
            int days2Add = 1;
            if(dow == DayOfWeek.FRIDAY)
                days2Add = 3;
            else if(dow == DayOfWeek.SATURDAY)
                days2Add = 2;
            return temp.plus(days2Add,ChronoUnit.DAYS);
        }
    }*/

}
