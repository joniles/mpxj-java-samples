package org.mpxj.calendars;

import net.sf.mpxj.*;
import net.sf.mpxj.common.DateHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarSamples {

   public static void main(String[] argv) throws Exception {
      CalendarSamples samples = new CalendarSamples();
      samples.basicOperations();
      samples.calendarHierarchy();
      samples.calendarUniqueID();
      samples.defaultCalendar();
      samples.workingOrNonWorkingExceptions();
   }

   private void basicOperations() throws Exception {
      //
      // Create a default calendar
      //
      System.out.println("Create a default calendar");
      ProjectFile file = new ProjectFile();
      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      System.out.println("The calendar name is " + calendar.getName());
      System.out.println();
      simpleCalendarDump(calendar);

      //
      // Make Saturday a working day and Monday a non-working day
      //
      System.out.println("Make Saturday a working day and Monday a non-working day");
      calendar.setWorkingDay(Day.SATURDAY, true);
      calendar.setWorkingDay(Day.MONDAY, false);
      simpleCalendarDump(calendar);

      //
      // Show the "raw form" of the working hours for Tuesday
      //
      System.out.println("Show the \"raw form\" of the working hours for Tuesday");
      List<DateRange> hours = calendar.getCalendarHours(Day.TUESDAY);
      hours.forEach(System.out::println);
      System.out.println();

      //
      // Show a formatted version of Tuesday's working hours
      //
      System.out.println("Show a formatted version of Tuesday's working hours");
      System.out.println(formatDateRanges(hours));
      System.out.println();

      //
      // Show a detailed dump of the whole calendar
      //
      System.out.println("Show a detailed dump of the whole calendar");
      detailedCalendarDump(calendar);

      //
      // Add some working hours to Saturday using constants supplied by MPXJ
      //
      System.out.println("Add some working hours to Saturday using constants supplied by MPXJ");
      hours = calendar.getCalendarHours(Day.SATURDAY);
      hours.add(ProjectCalendarDays.DEFAULT_WORKING_MORNING);
      hours.add(ProjectCalendarDays.DEFAULT_WORKING_AFTERNOON);
      detailedCalendarDump(calendar);

      //
      // Create our own working hours for Saturday
      //
      System.out.println("Create our own working hours for Saturday");
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
      System.out.println("Set up the same working hours, but use a helper method");
      startTime = DateHelper.getTime(9, 0);
      finishTime = DateHelper.getTime(14, 30);
      hours.clear();
      hours.add(new DateRange(startTime, finishTime));
      detailedCalendarDump(calendar);

      //
      // Show how many working hours there are on Saturday
      //
      System.out.println("Show how many working hours there are on Saturday");
      Duration duration = calendar.getWork(Day.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
      System.out.println();

      //
      // Let's try a naive approach to making Saturday 24 hours
      //
      System.out.println("Let's try a naive approach to making Saturday 24 hours");
      startTime = DateHelper.getTime(0, 0);
      finishTime = DateHelper.getTime(0, 0);
      hours.clear();
      hours.add(new DateRange(startTime, finishTime));
      System.out.println(formatDateRanges(calendar.getCalendarHours(Day.SATURDAY)));

      duration = calendar.getWork(Day.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
      System.out.println();

      //
      // Make 24 hour days work by using an end time which is +1 day
      //
      System.out.println("Make 24 hour days work by using an end time which is +1 day");
      javaCalendar.set(Calendar.HOUR_OF_DAY, 0);
      javaCalendar.set(Calendar.MINUTE, 0);
      startTime = javaCalendar.getTime();

      javaCalendar.add(Calendar.DAY_OF_YEAR, 1);
      finishTime = javaCalendar.getTime();

      hours.clear();
      hours.add(new DateRange(startTime, finishTime));
      System.out.println(formatDateRanges(calendar.getCalendarHours(Day.SATURDAY)));
      duration = calendar.getWork(Day.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
      System.out.println();

      //
      // Add an exception for a single day
      //
      System.out.println("Add an exception for a single day");
      DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      Date exceptionDate = df.parse("10/05/2022");

      boolean workingDate = calendar.isWorkingDate(exceptionDate);
      System.out.println(df.format(exceptionDate) + " is a " + (workingDate ? "working" : "non-working") + " day");

      ProjectCalendarException exception = calendar.addCalendarException(exceptionDate);
      exception.setName("A day off");

      workingDate = calendar.isWorkingDate(exceptionDate);
      System.out.println(df.format(exceptionDate) + " is a " + (workingDate ? "working" : "non-working") + " day");

      //
      // Make this a half-day
      //
      System.out.println("Make this a half-day");
      startTime = DateHelper.getTime(8, 0);
      finishTime = DateHelper.getTime(12, 0);
      exception.add(new DateRange(startTime, finishTime));
      workingDate = calendar.isWorkingDate(exceptionDate);
      System.out.println(df.format(exceptionDate) + " is a " + (workingDate ? "working" : "non-working") + " day");

      System.out.println("Working time on Tuesdays is normally "
         + calendar.getWork(Day.TUESDAY, TimeUnit.HOURS) + " but on "
         + df.format(exceptionDate) + " it is "
         + calendar.getWork(exceptionDate, TimeUnit.HOURS));
      System.out.println();

      //
      // Add an exception affecting a number of days
      //
      System.out.println("Add an exception affecting a number of days");
      dateDump(calendar, "23/05/2022", "28/05/2022");

      Date exceptionStartDate = df.parse("24/05/2022");
      Date exceptionEndDate = df.parse("26/05/2022");
      exception = calendar.addCalendarException(exceptionStartDate, exceptionEndDate);
      startTime = DateHelper.getTime(9, 0);
      finishTime = DateHelper.getTime(13, 0);
      exception.add(new DateRange(startTime, finishTime));

      dateDump(calendar, "23/05/2022", "28/05/2022");

      //
      // Represent a "crunch" period in October.
      // Three weeks of 16 hour weekdays, with 8 hour days at weekends
      //
      System.out.println("Represent a \"crunch\" period in October");
      Date weekStart = df.parse("01/10/2022");
      Date weekEnd = df.parse("21/10/2022");
      calendar = file.addDefaultBaseCalendar();
      ProjectCalendarWeek week = calendar.addWorkWeek();
      week.setName("Crunch Time!");
      week.setDateRange(new DateRange(weekStart, weekEnd));
      Arrays.stream(Day.values()).forEach(d -> week.setWorkingDay(d, true));

      startTime = DateHelper.getTime(9, 0);
      finishTime = DateHelper.getTime(17, 0);
      DateRange weekendHours = new DateRange(startTime, finishTime);
      Arrays.asList(Day.SATURDAY, Day.SUNDAY)
         .stream().forEach(d -> week.addCalendarHours(d).add(weekendHours));

      startTime = DateHelper.getTime(5, 0);
      finishTime = DateHelper.getTime(21, 0);
      DateRange weekdayHours = new DateRange(startTime, finishTime);
      Arrays.asList(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY)
         .stream().forEach(d -> week.addCalendarHours(d).add(weekdayHours));

      detailedCalendarDump(week);

      dateDump(calendar, "24/09/2022", "01/10/2022");
      dateDump(calendar, "01/10/2022", "08/10/2022");

      //
      // Creating a recurring exception
      //
      RecurringData recurringData = new RecurringData();
      exception = calendar.addCalendarException(recurringData);

      recurringData.setRecurrenceType(RecurrenceType.YEARLY);
      recurringData.setOccurrences(5);
      recurringData.setDayNumber(Integer.valueOf(1));
      recurringData.setMonthNumber(Integer.valueOf(1));
      recurringData.setStartDate(df.parse("01/01/2023"));
      System.out.println(recurringData);
   }

   private void simpleCalendarDump(ProjectCalendarDays calendar)
   {
      for (Day day : Day.values()) {
         String dayType = calendar.getCalendarDayType(day).toString();
         System.out.println(day + " is a " + dayType + " day");
      }
      System.out.println();
   }

   private void detailedCalendarDump(ProjectCalendarDays calendar)
   {
      for (Day day : Day.values()) {
         String dayType = calendar.getCalendarDayType(day).toString();
         System.out.println(day
            + " is a " + dayType + " day ("
            + formatDateRanges(calendar.getCalendarHours(day)) + ")");
      }
      System.out.println();
   }

   private String formatDateRanges(List<DateRange> hours) {
      DateFormat df = new SimpleDateFormat("HH:mm");
      return hours.stream()
         .map(h -> df.format(h.getStart()) + "-" + df.format(h.getEnd()))
         .collect(Collectors.joining(", "));
   }

   private void dateDump(ProjectCalendar calendar, String startDate, String endDate) throws Exception
   {
      DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

      Calendar start = Calendar.getInstance();
      start.setTime(df.parse(startDate));
      Calendar end = Calendar.getInstance();
      end.setTime(df.parse(endDate));

      for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
         System.out.println(df.format(date) + "\t" + calendar.getWork(date, TimeUnit.HOURS));
      }

      System.out.println();
   }

   private void calendarHierarchy() throws Exception
   {
      DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

      ProjectFile file = new ProjectFile();
      ProjectCalendar parentCalendar = file.addDefaultBaseCalendar();
      Date christmasDay = df.parse("25/12/2023");
      parentCalendar.addCalendarException(christmasDay);

      ProjectCalendar childCalendar = file.addDefaultDerivedCalendar();
      childCalendar.setParent(parentCalendar);

      System.out.println(christmasDay + " is a working day: " + childCalendar.isWorkingDate(christmasDay));
      System.out.println();

      parentCalendar.setCalendarDayType(Day.TUESDAY, DayType.NON_WORKING);
      System.out.println("Is " + Day.TUESDAY + " a working day: " + childCalendar.isWorkingDay(Day.TUESDAY));
      System.out.println();

      simpleCalendarDump(parentCalendar);
      simpleCalendarDump(childCalendar);

      childCalendar.setCalendarDayType(Day.TUESDAY, DayType.WORKING);
      Date startTime = DateHelper.getTime(9, 0);
      Date finishTime = DateHelper.getTime(12, 30);
      childCalendar.addCalendarHours(Day.TUESDAY).add(new DateRange(startTime, finishTime));
   }

   private void calendarUniqueID()
   {
      ProjectFile file = new ProjectFile();
      ProjectCalendar calendar1 = file.addCalendar();
      calendar1.setName("Calendar 1");

      ProjectCalendar calendar2 = file.addCalendar();
      calendar2.setName("Calendar 2");

      ProjectCalendar calendar3 = file.addCalendar();
      calendar3.setName("Calendar 3");

      file.getCalendars().forEach(c -> System.out.println(c.getName()));
      System.out.println();

      file.getCalendars().forEach(c -> System.out.println(c.getName() + " (Unique ID: " + c.getUniqueID() + ")"));
      System.out.println();

      ProjectCalendar calendar = file.getCalendars().getByUniqueID(2);
      System.out.println(calendar.getName());
   }

   private void defaultCalendar()
   {
      ProjectFile file = new ProjectFile();
      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);
      System.out.println("The default calendar name is " + file.getDefaultCalendar().getName());
      System.out.println();
   }

   private void workingOrNonWorkingExceptions() throws Exception
   {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

      ProjectFile file = new ProjectFile();
      ProjectCalendar calendar = file.addDefaultBaseCalendar();

      //
      // Add an exception without hours - this makes the day non-working
      //
      ProjectCalendarException nonWorkingException  = calendar.addCalendarException(df.parse("2022-05-23"));
      System.out.println("Exception represents a working day: " + !nonWorkingException.isEmpty());

      //
      // Add an exception with hours to make this day working but with non-default hours
      //
      ProjectCalendarException workingException  = calendar.addCalendarException(df.parse("2022-05-24"));
      Date startTime = DateHelper.getTime(9, 0);
      Date finishTime = DateHelper.getTime(13, 0);
      workingException.add(new DateRange(startTime, finishTime));
      System.out.println("Exception represents a working day: " + !workingException.isEmpty());
   }
}
