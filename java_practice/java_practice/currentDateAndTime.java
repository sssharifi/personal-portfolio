import java.util.*;

class currentDateAndTime {

    public static void main(String args[]) {
        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();
     
        month = date.get(Calendar.MONTH);
        day = date.get(Calendar.DAY_OF_MONTH);
        year = date.get(Calendar.YEAR);
       
        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);
 
        System.out.println("Current date is  "+(month+1)+"/"+day+"/"+year);
        System.out.println("Current time is  "+hour+" : "+minute+" : "+second);
   }   
}      