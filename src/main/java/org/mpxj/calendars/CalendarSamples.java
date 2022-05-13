package org.mpxj.calendars;

import net.sf.mpxj.DateRange;
import net.sf.mpxj.Day;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarSamples {

   public static void main(String[] argv) {
      CalendarSamples samples = new CalendarSamples();
      samples.process();
   }

   private void process() {
      ProjectFile file = new ProjectFile();
      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      System.out.println("The calendar name is " + calendar.getName());
      System.out.println();

      for (Day day : Day.values()) {
         String working = calendar.isWorkingDay(day) ? "Working" : "Non-working";
         System.out.println(day + " is a " + working + " day");
      }
      System.out.println();

      calendar.setWorkingDay(Day.SATURDAY, true);
      calendar.setWorkingDay(Day.MONDAY, false);

      for (Day day : Day.values()) {
         String working = calendar.isWorkingDay(day) ? "Working" : "Non-working";
         System.out.println(day + " is a " + working + " day");
      }
      System.out.println();

      List<DateRange> hours = calendar.getHours(Day.TUESDAY);
      hours.forEach(System.out::println);
      System.out.println();

      System.out.println(formatDateRanges(hours));
      System.out.println();

      for (Day day : Day.values()) {
         String working = calendar.isWorkingDay(day) ? "Working" : "Non-working";
         System.out.println(day
            + " is a " + working + " day ("
            + formatDateRanges(calendar.getHours(day)) + ")");
      }
      System.out.println();

   }

   private String formatDateRanges(List<DateRange> hours) {
      DateFormat df = new SimpleDateFormat("HH:mm");
      return hours.stream()
         .map(h -> df.format(h.getStart()) + "-" + df.format(h.getEnd()))
         .collect(Collectors.joining(", "));
   }
}
