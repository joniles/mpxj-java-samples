package org.mpxj.calendars;

import net.sf.mpxj.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
      calendar.setWorkingDay(DayOfWeek.SATURDAY, true);
      calendar.setWorkingDay(DayOfWeek.MONDAY, false);
      simpleCalendarDump(calendar);

      //
      // Show the "raw form" of the working hours for Tuesday
      //
      System.out.println("Show the \"raw form\" of the working hours for Tuesday");
      List<LocalTimeRange> hours = calendar.getCalendarHours(DayOfWeek.TUESDAY);
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
      hours = calendar.getCalendarHours(DayOfWeek.SATURDAY);
      hours.add(ProjectCalendarDays.DEFAULT_WORKING_MORNING);
      hours.add(ProjectCalendarDays.DEFAULT_WORKING_AFTERNOON);
      detailedCalendarDump(calendar);

      //
      // Create our own working hours for Saturday
      //
      System.out.println("Create our own working hours for Saturday");
      LocalTime startTime = LocalTime.of(9,0);
      LocalTime finishTime = LocalTime.of(14, 30);
      hours = calendar.getCalendarHours(DayOfWeek.SATURDAY);
      hours.clear();
      hours.add(new LocalTimeRange(startTime, finishTime));

      detailedCalendarDump(calendar);

      //
      // Show how many working hours there are on Saturday
      //
      System.out.println("Show how many working hours there are on Saturday");
      Duration duration = calendar.getWork(DayOfWeek.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
      System.out.println();

      //
      // Let's try a naive approach to making Saturday 24 hours
      //
      System.out.println("Let's try a naive approach to making Saturday 24 hours");
      startTime = LocalTime.MIDNIGHT;
      finishTime = LocalTime.MIDNIGHT;
      hours.clear();
      hours.add(new LocalTimeRange(startTime, finishTime));
      System.out.println(formatDateRanges(calendar.getCalendarHours(DayOfWeek.SATURDAY)));

      duration = calendar.getWork(DayOfWeek.SATURDAY, TimeUnit.HOURS);
      System.out.println(duration);
      System.out.println();

      //
      // Add an exception for a single day
      //
      System.out.println("Add an exception for a single day");
      LocalDate exceptionDate = LocalDate.of(2022, 5, 10);

      boolean workingDate = calendar.isWorkingDate(exceptionDate);
      System.out.println(exceptionDate + " is a " + (workingDate ? "working" : "non-working") + " day");

      ProjectCalendarException exception = calendar.addCalendarException(exceptionDate);
      exception.setName("A day off");

      workingDate = calendar.isWorkingDate(exceptionDate);
      System.out.println(exceptionDate + " is a " + (workingDate ? "working" : "non-working") + " day");

      //
      // Make this a half-day
      //
      System.out.println("Make this a half-day");
      startTime = LocalTime.of(8, 0);
      finishTime = LocalTime.of(12, 0);
      exception.add(new LocalTimeRange(startTime, finishTime));
      workingDate = calendar.isWorkingDate(exceptionDate);
      System.out.println(exceptionDate + " is a " + (workingDate ? "working" : "non-working") + " day");

      System.out.println("Working time on Tuesdays is normally "
         + calendar.getWork(DayOfWeek.TUESDAY, TimeUnit.HOURS) + " but on "
         + exceptionDate + " it is "
         + calendar.getWork(exceptionDate, TimeUnit.HOURS));
      System.out.println();

      //
      // Add an exception affecting a number of days
      //
      System.out.println("Add an exception affecting a number of days");
      dateDump(calendar, LocalDate.of(2022, 5, 23), LocalDate.of(2022, 5, 28));

      LocalDate exceptionStartDate = LocalDate.of(2022, 5, 24);
      LocalDate exceptionEndDate = LocalDate.of(2022, 5, 26);
      exception = calendar.addCalendarException(exceptionStartDate, exceptionEndDate);
      startTime = LocalTime.of(9, 0);
      finishTime = LocalTime.of(13, 0);
      exception.add(new LocalTimeRange(startTime, finishTime));

      dateDump(calendar, LocalDate.of(2022, 5, 23), LocalDate.of(2022, 5, 28));

      //
      // Represent a "crunch" period in October.
      // Three weeks of 16 hour weekdays, with 8 hour days at weekends
      //
      System.out.println("Represent a \"crunch\" period in October");
      LocalDate weekStart = LocalDate.of(2022, 10, 1);
      LocalDate weekEnd = LocalDate.of(2022, 10, 21);
      calendar = file.addDefaultBaseCalendar();
      ProjectCalendarWeek week = calendar.addWorkWeek();
      week.setName("Crunch Time!");
      week.setDateRange(new LocalDateRange(weekStart, weekEnd));
      Arrays.stream(DayOfWeek.values()).forEach(d -> week.setWorkingDay(d, true));

      startTime = LocalTime.of(9, 0);
      finishTime = LocalTime.of(17, 0);
      LocalTimeRange weekendHours = new LocalTimeRange(startTime, finishTime);
      Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
         .stream().forEach(d -> week.addCalendarHours(d).add(weekendHours));

      startTime = LocalTime.of(5, 0);
      finishTime = LocalTime.of(21, 0);
      LocalTimeRange weekdayHours = new LocalTimeRange(startTime, finishTime);
      Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
         .stream().forEach(d -> week.addCalendarHours(d).add(weekdayHours));

      detailedCalendarDump(week);

      dateDump(calendar, LocalDate.of(2022, 9, 24), LocalDate.of(2022, 10, 1));
      dateDump(calendar, LocalDate.of(2022, 10, 1), LocalDate.of(2022, 10, 8));

      //
      // Creating a recurring exception
      //
      RecurringData recurringData = new RecurringData();
      exception = calendar.addCalendarException(recurringData);

      recurringData.setRecurrenceType(RecurrenceType.YEARLY);
      recurringData.setOccurrences(5);
      recurringData.setDayNumber(Integer.valueOf(1));
      recurringData.setMonthNumber(Integer.valueOf(1));
      recurringData.setStartDate(LocalDate.of(2023, 1, 1));
      System.out.println(recurringData);
   }

   private void simpleCalendarDump(ProjectCalendarDays calendar)
   {
      for (DayOfWeek day : DayOfWeek.values()) {
         String dayType = calendar.getCalendarDayType(day).toString();
         System.out.println(day + " is a " + dayType + " day");
      }
      System.out.println();
   }

   private void detailedCalendarDump(ProjectCalendarDays calendar)
   {
      for (DayOfWeek day : DayOfWeek.values()) {
         String dayType = calendar.getCalendarDayType(day).toString();
         System.out.println(day
            + " is a " + dayType + " day ("
            + formatDateRanges(calendar.getCalendarHours(day)) + ")");
      }
      System.out.println();
   }

   private String formatDateRanges(List<LocalTimeRange> hours) {
      DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
      return hours.stream()
         .map(h -> df.format(h.getStart()) + "-" + df.format(h.getEnd()))
         .collect(Collectors.joining(", "));
   }

   private void dateDump(ProjectCalendar calendar, LocalDate startDate, LocalDate endDate)
   {
      for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
         System.out.println(date + "\t" + calendar.getWork(date, TimeUnit.HOURS));
      }

      System.out.println();
   }

   private void calendarHierarchy()
   {
      ProjectFile file = new ProjectFile();
      ProjectCalendar parentCalendar = file.addDefaultBaseCalendar();
      LocalDate christmasDay = LocalDate.of(2023, 12, 25);
      parentCalendar.addCalendarException(christmasDay);

      ProjectCalendar childCalendar = file.addDefaultDerivedCalendar();
      childCalendar.setParent(parentCalendar);

      System.out.println(christmasDay + " is a working day: " + childCalendar.isWorkingDate(christmasDay));
      System.out.println();

      parentCalendar.setCalendarDayType(DayOfWeek.TUESDAY, DayType.NON_WORKING);
      System.out.println("Is " + DayOfWeek.TUESDAY + " a working day: " + childCalendar.isWorkingDay(DayOfWeek.TUESDAY));
      System.out.println();

      simpleCalendarDump(parentCalendar);
      simpleCalendarDump(childCalendar);

      childCalendar.setCalendarDayType(DayOfWeek.TUESDAY, DayType.WORKING);
      LocalTime startTime = LocalTime.of(9, 0);
      LocalTime finishTime = LocalTime.of(12, 30);
      childCalendar.addCalendarHours(DayOfWeek.TUESDAY).add(new LocalTimeRange(startTime, finishTime));
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

   private void workingOrNonWorkingExceptions()
   {
      ProjectFile file = new ProjectFile();
      ProjectCalendar calendar = file.addDefaultBaseCalendar();

      //
      // Add an exception without hours - this makes the day non-working
      //
      ProjectCalendarException nonWorkingException  = calendar.addCalendarException(LocalDate.of(2022, 5, 23));
      System.out.println("Exception represents a working day: " + !nonWorkingException.isEmpty());

      //
      // Add an exception with hours to make this day working but with non-default hours
      //
      ProjectCalendarException workingException  = calendar.addCalendarException(LocalDate.of(2022, 5, 24));
      LocalTime startTime = LocalTime.of(9, 0);
      LocalTime finishTime = LocalTime.of(13, 0);
      workingException.add(new LocalTimeRange(startTime, finishTime));
      System.out.println("Exception represents a working day: " + !workingException.isEmpty());
   }
}
