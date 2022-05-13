package org.mpxj.calendars;

import net.sf.mpxj.*;
import net.sf.mpxj.common.DateHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarSamples {

   public static void main(String[] argv) throws Exception {
      CalendarSamples samples = new CalendarSamples();
      samples.process();
   }

   private void process() throws Exception {
      //
      // Create a default calendar
      //
      ProjectFile file = new ProjectFile();
      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      System.out.println("The calendar name is " + calendar.getName());
      System.out.println();
      simpleCalendarDump(calendar);

      //
      // Make Saturday a working day and Monday a non-working day
      //
      calendar.setWorkingDay(Day.SATURDAY, true);
      calendar.setWorkingDay(Day.MONDAY, false);
      simpleCalendarDump(calendar);
      simpleCalendarDump(calendar);

      //
      // Show the "raw form" of the working hours for Tuesday
      //
      List<DateRange> hours = calendar.getHours(Day.TUESDAY);
      hours.forEach(System.out::println);
      System.out.println();

      //
      // Show a formatted version of Tuesday's working hours
      //
      System.out.println(formatDateRanges(hours));
      System.out.println();

      //
      // Show a detailed dump of the whole calendar
      //
      detailedCalendarDump(calendar);

      //
      // Add some working hours to Saturday using constants supplied by MPXJ
      //
      hours = calendar.getCalendarHours(Day.SATURDAY);
      hours.add(ProjectCalendarDays.DEFAULT_WORKING_MORNING);
      hours.add(ProjectCalendarDays.DEFAULT_WORKING_AFTERNOON);
      detailedCalendarDump(calendar);

      //
      // Create our own working hoyrs for Saturday
      //
      Calendar javaCalendar = Calendar.getInstance();
      javaCalendar.set(Calendar.HOUR_OF_DAY, 9);
      javaCalendar.set(Calendar.MINUTE, 0);
      Date startTime = javaCalendar.getTime();

      javaCalendar.set(Calendar.HOUR_OF_DAY, 14);
      javaCalendar.set(Calendar.MINUTE, 30);
      Date finishTime = javaCalendar.getTime();

      hours = calendar.getCalendarHours(Day.SATURDAY);
      hours.clear();
      hours.add(new DateRange(startTime, finishTime));

      detailedCalendarDump(calendar);

      //
      // Set up the same working hours, but use a helper method
      //
      startTime = DateHelper.getTime(9, 0);
      finishTime = DateHelper.getTime(14, 30);
      hours.clear();
      hours.add(new DateRange(startTime, finishTime));
      detailedCalendarDump(calendar);

      //
      // See how many working hours on Saturday
      //
      Duration duration = calendar.getWork(Day.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
      System.out.println();

      //
      // Let's try a naive approach to making Saturday 24 hours
      //
      startTime = DateHelper.getTime(0, 0);
      finishTime = DateHelper.getTime(0, 0);
      hours.clear();
      hours.add(new DateRange(startTime, finishTime));
      System.out.println(formatDateRanges(calendar.getHours(Day.SATURDAY)));

      duration = calendar.getWork(Day.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
      System.out.println();

      //
      // Create an end date which is +1 day
      //
      javaCalendar.set(Calendar.HOUR_OF_DAY, 0);
      javaCalendar.set(Calendar.MINUTE, 0);
      startTime = javaCalendar.getTime();

      javaCalendar.add(Calendar.DAY_OF_YEAR, 1);
      finishTime = javaCalendar.getTime();

      hours.clear();
      hours.add(new DateRange(startTime, finishTime));
      System.out.println(formatDateRanges(calendar.getHours(Day.SATURDAY)));
      System.out.println();

      duration = calendar.getWork(Day.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
   }

   private void simpleCalendarDump(ProjectCalendar calendar)
   {
      for (Day day : Day.values()) {
         String working = calendar.isWorkingDay(day) ? "Working" : "Non-working";
         System.out.println(day + " is a " + working + " day");
      }
      System.out.println();
   }

   private void detailedCalendarDump(ProjectCalendar calendar)
   {
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
