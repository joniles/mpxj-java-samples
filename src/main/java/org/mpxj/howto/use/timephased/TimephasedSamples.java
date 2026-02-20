package org.mpxj.howto.use.timephased;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mpxj.*;
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

      file.getResources().forEach(System.out::println);
      System.out.println();

      file.getTasks().forEach(System.out::println);
      System.out.println();

      file.getResourceAssignments().forEach(a -> System.out.println(a + " " +a.getUniqueID()));
      System.out.println();

      List<LocalDateTimeRange> ranges = new TimescaleUtility()
               .createTimescale(LocalDateTime.of(2026, 2, 18, 0, 0), 7, TimescaleUnits.DAYS);


      // Work
      {
         ResourceAssignment assignment = file.getResourceAssignments().getByUniqueID(6);
         List<Duration> work = assignment.getTimephasedWork(ranges, TimeUnit.HOURS);
         writeTableHeader(ranges);
         writeTableRow("Work", work);
         System.out.println();
      }

      // Actual and remaining work
      {
         ResourceAssignment assignment = file.getResourceAssignments().getByUniqueID(6);
         List<Duration> actualWork = assignment.getTimephasedActualWork(ranges, TimeUnit.HOURS);
         List<Duration> remainingWork = assignment.getTimephasedRemainingWork(ranges, TimeUnit.HOURS);
         writeTableHeader(ranges);
         writeTableRow("Actual Work", actualWork);
         writeTableRow("Remaining Work", remainingWork);
         System.out.println();
      }

      // Resource 2
      {
         Resource resource2 = file.getResourceByID(2);
         List<Duration> work = resource2.getTimephasedWork(ranges, TimeUnit.HOURS);
         writeTableHeader(ranges);
         writeTableRow("Resource 2 Work", work);
         System.out.println();
      }

      // Tasks
      {
         Task summaryTask = file.getTaskByID(1);
         Task task1 = file.getTaskByID(2);
         Task task2 = file.getTaskByID(3);
         List<Duration> summaryWork = summaryTask.getTimephasedWork(ranges, TimeUnit.HOURS);
         List<Duration> task1Work = task1.getTimephasedWork(ranges, TimeUnit.HOURS);
         List<Duration> task2Work = task2.getTimephasedWork(ranges, TimeUnit.HOURS);
         writeTableHeader(ranges);
         writeTableRow("Summary Work", summaryWork);
         writeTableRow("Task 1 Work", task1Work);
         writeTableRow("Task 2 Work", task2Work);
         System.out.println();
      }

      // Costs
      {
         Task summaryTask = file.getTaskByID(1);
         Task task1 = file.getTaskByID(2);
         Task task2 = file.getTaskByID(3);
         List<Number> summaryCost = summaryTask.getTimephasedCost(ranges);
         List<Number> task1Cost = task1.getTimephasedCost(ranges);
         List<Number> task2Cost = task2.getTimephasedCost(ranges);
         writeTableHeader(ranges);
         writeTableRow("Summary Cost", summaryCost);
         writeTableRow("Task 1 Cost", task1Cost);
         writeTableRow("Task 2 Cost", task2Cost);
         System.out.println();
      }

      // Material
      {
         // Retrieve an assignment for a  material resource
         ResourceAssignment assignment = file.getResourceAssignments().getByUniqueID(11);

         // Create labels using the correct units for the resource
         String materialUnits = "(" + assignment.getResource().getMaterialLabel() + ")";
         String actualMaterialLabel = "Actual Material " + materialUnits;
         String remainingMaterialLabel = "Remaining Material " + materialUnits;
         String materialLabel = "Material " + materialUnits;

         // Retrieve the timephased values
         List<Number> actualMaterial = assignment.getTimephasedActualMaterial(ranges);
         List<Number> remainingMaterial = assignment.getTimephasedRemainingMaterial(ranges);
         List<Number> material = assignment.getTimephasedMaterial(ranges);

         // Present the values as a table
         writeTableHeader(ranges);
         writeTableRow(actualMaterialLabel, actualMaterial);
         writeTableRow(remainingMaterialLabel, remainingMaterial);
         writeTableRow(materialLabel, material);
         System.out.println();
      }

      // Retrieve work from Tasks using parametrised methods
      {
         // Retrieve tasks
         Task summaryTask = file.getTaskByID(1);
         Task task1 = file.getTaskByID(2);
         Task task2 = file.getTaskByID(3);

         // Retrieve timephased work
         List<Duration> summaryWork = summaryTask.getTimephasedDurationValues(TaskField.WORK, ranges, TimeUnit.HOURS);
         List<Duration> task1Work = task1.getTimephasedDurationValues(TaskField.WORK, ranges, TimeUnit.HOURS);
         List<Duration> task2Work = task2.getTimephasedDurationValues(TaskField.WORK, ranges, TimeUnit.HOURS);

         // Present the values as a table
         writeTableHeader(ranges);
         writeTableRow("Summary Work", summaryWork);
         writeTableRow("Task 1 Work", task1Work);
         writeTableRow("Task 2 Work", task2Work);
         System.out.println();
      }

      // Retrieve costs from Tasks using parametrised methods
      {
         // Retrieve a resource assignment
         ResourceAssignment assignment = file.getResourceAssignments().getByUniqueID(6);

         // Retrieve timephased costs
         List<Number> actualCost = assignment.getTimephasedNumericValues(AssignmentField.ACTUAL_COST, ranges);
         List<Number> remainingCost = assignment.getTimephasedNumericValues(AssignmentField.REMAINING_COST, ranges);
         List<Number> cost = assignment.getTimephasedNumericValues(AssignmentField.COST, ranges);

         // Present the values as a table
         writeTableHeader(ranges);
         writeTableRow("Actual Cost", actualCost);
         writeTableRow("Remaining Cost", remainingCost);
         writeTableRow("Cost", cost);
         System.out.println();
      }

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
