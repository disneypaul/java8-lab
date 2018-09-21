// This is your Editor pane. Write your Java here and 
// use the command line to execute commands
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.*;
import java.time.format.*;
import java.util.Optional; 

public class Sample1{
  public static void main(String [] args){
    String name="WelcomeJava";
    Runnable r1=() -> System.out.println(name);
    String name1="";
    name1=name.toUpperCase();
    //Runnable r2=() -> System.out.println(name1);
    //r1.run();
    LocalDate date1 = LocalDate.now();
      LocalDate date2 = date1.plus(1, ChronoUnit.MONTHS);
      Period period = Period.between(date2, date1);
      System.out.println("Period: " + period);
      
      DateTimeFormatter dateFormat=DateTimeFormatter.ISO_DATE;
   // LocalDate dateOfBirth= LocalDate.of(2015,Month.FEBRUARY,31);
       //System.out.println(dateFormat.format(dateOfBirth));
       
        String[] str = new String[10];        
            str[5] = null;;
            str[4] = "JAVA OPTIONAL CLASS EXAMPLE";
            Optional<String> checkNull = Optional.ofNullable(str[5]);  
            if(checkNull.isPresent()){  // It Checks, value is present or not  
                String lowercaseString = str[5].toLowerCase();  
                System.out.print(lowercaseString);  
            }else  
                System.out.println("String value is not present");  
            
           final Clock clock=Clock.systemUTC();
    System.out.println(clock.instant());
    System.out.println(clock.millis());
    
  }
} 
