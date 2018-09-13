
import java.time.temporal.TemporalAdjusters;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class DateTime2{
  public static void main(String[] args){
    LocalDateTime localDateTime = LocalDateTime.now();
    LocalDateTime localDateTimeWith = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
    
    System.out.println("LocalDateTime ["+localDateTime+"] with last day of month ["+localDateTimeWith+"]");
    //LocalDateTime [2018-09-13T11:19:50.913929] with last day of month [2018-09-30T11:19:50.913929]
    
    Date date = new Date();
    Instant instant = date.toInstant();
    System.out.println("Date ["+date+"] Instant ["+instant+"]");
    
    ZoneId defaultZoneId = ZoneId.systemDefault();
    System.out.println("ZoneId default ["+defaultZoneId+"]");
    
    LocalDate localDate2 = instant.atZone(defaultZoneId).toLocalDate();
    System.out.println("LocalDate ["+localDate2+"]");
  }
}

/**
LocalDateTime [2018-09-13T11:31:49.818795] with last day of month [2018-09-30T11:31:49.818795]
Date [Thu Sep 13 11:31:49 UTC 2018] Instant [2018-09-13T11:31:49.876Z]
ZoneId default [Etc/UTC]
LocalDate [2018-09-13]
**/
