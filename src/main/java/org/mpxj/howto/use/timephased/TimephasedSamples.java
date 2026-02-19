package org.mpxj.howto.use.timephased;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mpxj.Duration;
import org.mpxj.LocalDateTimeRange;
import org.mpxj.MPXJException;
import org.mpxj.ProjectFile;
import org.mpxj.ResourceAssignment;
import org.mpxj.TimeUnit;
import org.mpxj.mpp.TimescaleUnits;
import org.mpxj.reader.UniversalProjectReader;
import org.mpxj.utility.TimescaleUtility;

public class TimephasedSamples
{
   public static void main(String[] argv) throws MPXJException
   {
      new TimephasedSamples().timephasedData();
   }

   public void timescaleRanges()
   {
      // Single range
      LocalDateTimeRange today = new LocalDateTimeRange(LocalDateTime.of(2026, 2, 18, 0, 0), LocalDateTime.of(2026, 2, 19, 0, 0));

      // Multiple ranges
      LocalDateTimeRange day1 = new LocalDateTimeRange(LocalDateTime.of(2026, 2, 18, 0, 0), LocalDateTime.of(2026, 2, 19, 0, 0));
      LocalDateTimeRange day2 = new LocalDateTimeRange(LocalDateTime.of(2026, 2, 19, 0, 0), LocalDateTime.of(2026, 2, 20, 0, 0));
      List<LocalDateTimeRange> timescale = Arrays.asList(day1, day2);
   }

   public void timescaleSegmentCount()
   {
      // Timescale with segment count
      LocalDateTime startDate = LocalDateTime.of(2026, 2, 16, 0, 0, 0);
      List<LocalDateTimeRange> ranges = new TimescaleUtility().createTimescale(startDate, 5, TimescaleUnits.DAYS);
   }

   public void timescaleDateRange()
   {
      // Timescale with date range
      LocalDateTime startDate = LocalDateTime.of(2026, 2, 16, 0, 0, 0);
      LocalDateTime endDate = LocalDateTime.of(2026, 2, 20, 0, 0, 0);
      List<LocalDateTimeRange> ranges = new TimescaleUtility()
               .createTimescale(startDate, endDate, TimescaleUnits.DAYS);

   }

   public void timephasedData() throws MPXJException
   {
      ProjectFile file = new UniversalProjectReader().read(
               getClass().getClassLoader().getResourceAsStream("org/mpxj/timephased-sample.mpp"));
      file.getResourceAssignments().forEach(a -> System.out.println(a + " " +a.getUniqueID()));
      System.out.println();

      List<LocalDateTimeRange> ranges = new TimescaleUtility()
               .createTimescale(LocalDateTime.of(2026, 2, 18, 0, 0), 7, TimescaleUnits.DAYS);

      ResourceAssignment assignment = file.getResourceAssignments().getByUniqueID(6);
      System.out.println(assignment);

      // Work
      List<Duration> work = assignment.getTimephasedWork(ranges, TimeUnit.HOURS);
      writeTableHeader(ranges);
      writeTableRow("Work", work);
      System.out.println();

      // Actual and remaining work
      List<Duration> actualWork = assignment.getTimephasedActualWork(ranges, TimeUnit.HOURS);
      List<Duration> remainingWork = assignment.getTimephasedRemainingWork(ranges, TimeUnit.HOURS);
      writeTableHeader(ranges);
      writeTableRow("Actual Work", actualWork);
      writeTableRow("Remaining Work", remainingWork);
   }

   private void writeTableHeader(List<LocalDateTimeRange> ranges)
   {
      String labels = ranges.stream()
         .map(r -> r.getStart().getDayOfWeek().name().substring(0, 1))
         .collect(Collectors.joining("|"));
      System.out.println("||" + labels + "|");

      String separator = ranges.stream()
         .map(r -> "---")
         .collect(Collectors.joining("|"));
      System.out.println("|---|" + separator+ "|");
   }

   private void writeTableRow(String label, List<?> data)
   {
      String values = data.stream()
         .map(String::valueOf)
         .collect(Collectors.joining("|"));
      System.out.println("|" + label + "|" + values + "|");
   }
}
