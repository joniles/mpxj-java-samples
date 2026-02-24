package org.mpxj.timephased;

import org.mpxj.*;
import org.mpxj.mspdi.MSPDIWriter;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Sample class illustrating how timephased data can be written to an MSPDI file.
 */
public class WriteTimephasedData {

   public static void main(String[] argv) throws Exception {

      if (argv.length != 1) {
         System.out.println("Usage: WriteTimephasedData <output file name>");
         return;
      }

      new WriteTimephasedData().process(argv[0]);
   }

   public void process(String outputFileName) throws Exception {
      ProjectFile file = new ProjectFile();
      file.addDefaultBaseCalendar();

      addFlatDistributionTask(file);
      addFlatDistributionPartiallyCompleteTask(file);
      addFlatDistributionCompleteTask(file);

      addCustomDistributionTask(file);
      addCustomDistributionPartiallyCompleteTask(file);
      addCustomDistributionCompleteTask(file);

      addSplitTask(file);
      addSplitTaskPartiallyCompleteSplit(file);
      addSplitTaskFirstSplitComplete(file);
      addSplitTaskSecondSplitPartiallyComplete(file);
      addSplitTaskComplete(file);

      MSPDIWriter writer = new MSPDIWriter();

      // By default, timephased data is not written, so we need to enable it
      writer.setWriteTimephasedData(true);

      writer.write(file, outputFileName);
   }

   /**
    * We don't need to write timephased data for the resource assignment as
    * we're undertaking the default amount of work per day.
    *
    * @param file parent file
    */
   private void addFlatDistributionTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 1");

      Task task = file.addTask();
      task.setName("Task 1 - Flat Distribution");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setWorkContour(WorkContour.FLAT);
   }

   /**
    * 40h task, 10h complete. Flat distribution, so no timephased data needed.
    *
    * @param file parent file
    */
   private void addFlatDistributionPartiallyCompleteTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 2");

      Task task = file.addTask();
      task.setName("Task 2 - Flat Distribution Partially Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(10, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(30, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(10, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(30, TimeUnit.HOURS));
      assignment.setWorkContour(WorkContour.FLAT);
   }

   private void addFlatDistributionCompleteTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 3");

      Task task = file.addTask();
      task.setName("Task 3 - Flat Distribution Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      task.setActualFinish(task.getActualFinish());
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(0, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(0, TimeUnit.HOURS));
      assignment.setWorkContour(WorkContour.FLAT);
   }

   /**
    * We'll create timephased data to allow us to show that we work
    * 10h on day 1, 6h on day 2, then 8h per day for the remaining days.
    *
    * @param file parent file
    */
   private void addCustomDistributionTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 4");

      Task task = file.addTask();
      task.setName("Task 4 - Custom Distribution");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));

      // Create a resource assignment
      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));

      // Day 1 - 10h
      TimephasedWork day1RemainingWork = new TimephasedWork();
      day1RemainingWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1RemainingWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1RemainingWork.setAmountPerHour(Duration.getInstance(1.25, TimeUnit.HOURS));
      day1RemainingWork.setTotalAmount(Duration.getInstance(10, TimeUnit.HOURS));

      // Day 2 - 6h
      TimephasedWork day2RemainingWork = new TimephasedWork();
      day2RemainingWork.setStart(LocalDateTime.of(2024, 3, 5, 8, 0));
      day2RemainingWork.setFinish(LocalDateTime.of(2024, 3, 5, 17, 0));
      day2RemainingWork.setAmountPerHour(Duration.getInstance(0.75, TimeUnit.HOURS));
      day2RemainingWork.setTotalAmount(Duration.getInstance(6, TimeUnit.HOURS));

      // Remaining days - 8h/day
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      remainingWork.setAmountPerHour(Duration.getInstance(1, TimeUnit.HOURS));
      remainingWork.setTotalAmount(Duration.getInstance(24, TimeUnit.HOURS));

      // Add timephased data to the resource assignment
      assignment.getRawTimephasedRemainingRegularWork()
         .addAll(Arrays.asList(day1RemainingWork, day2RemainingWork, remainingWork));
   }

   /**
    * We'll create timephased data to allow us to show that we work
    * 10h on day 1, 6h on day 2, then 8h per day for the remaining days.
    * Day 1 has 4h of actual work.
    *
    * @param file parent file
    */
   private void addCustomDistributionPartiallyCompleteTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 5");

      Task task = file.addTask();
      task.setName("Task 5 - Custom Distribution Partially Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(4, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(36, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(4, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(36, TimeUnit.HOURS));

      // Important - MS Project needs this as well as the timephased data
      // to correctly represent the actual and remaining work
      assignment.setStop(LocalDateTime.of(2024, 3, 4, 12, 0));
      assignment.setResume(LocalDateTime.of(2024, 3, 4, 13, 0));

      // Day 1 actual work - 4h
      TimephasedWork day1ActualWork = new TimephasedWork();
      day1ActualWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1ActualWork.setFinish(LocalDateTime.of(2024, 3, 4, 12, 0));
      day1ActualWork.setTotalAmount(Duration.getInstance(4, TimeUnit.HOURS));

      // Day 1 remaining - 6h
      TimephasedWork day1RemainingWork = new TimephasedWork();
      day1RemainingWork.setStart(LocalDateTime.of(2024, 3, 4, 13, 0));
      day1RemainingWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1RemainingWork.setTotalAmount(Duration.getInstance(6, TimeUnit.HOURS));

      // Day 2 - 6h
      TimephasedWork day2RemainingWork = new TimephasedWork();
      day2RemainingWork.setStart(LocalDateTime.of(2024, 3, 5, 8, 0));
      day2RemainingWork.setFinish(LocalDateTime.of(2024, 3, 5, 17, 0));
      day2RemainingWork.setTotalAmount(Duration.getInstance(6, TimeUnit.HOURS));

      // Remaining days - 8h/day
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      remainingWork.setTotalAmount(Duration.getInstance(24, TimeUnit.HOURS));

      assignment.getRawTimephasedActualRegularWork().add(day1ActualWork);
      assignment.getRawTimephasedRemainingRegularWork().addAll(Arrays.asList(day1RemainingWork, day2RemainingWork, remainingWork));
   }

   /**
    * We'll create timephased data to allow us to show that we work
    * 10h on day 1, 6h on day 2, then 8h per day for the remaining days.
    * All work is complete.
    *
    * @param file parent file
    */
   private void addCustomDistributionCompleteTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 6");

      Task task = file.addTask();
      task.setName("Task 6 - Custom Distribution Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(0, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(0, TimeUnit.HOURS));

      // Day 1 actual work - 10h
      TimephasedWork day1ActualWork = new TimephasedWork();
      day1ActualWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1ActualWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1ActualWork.setTotalAmount(Duration.getInstance(10, TimeUnit.HOURS));

      // Day 2 actual - 6h
      TimephasedWork day2ActualWork = new TimephasedWork();
      day2ActualWork.setStart(LocalDateTime.of(2024, 3, 5, 8, 0));
      day2ActualWork.setFinish(LocalDateTime.of(2024, 3, 5, 17, 0));
      day2ActualWork.setTotalAmount(Duration.getInstance(6, TimeUnit.HOURS));

      // Remaining days - 8h/day
      TimephasedWork actualWork = new TimephasedWork();
      actualWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      actualWork.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      actualWork.setTotalAmount(Duration.getInstance(24, TimeUnit.HOURS));

      assignment.getRawTimephasedActualRegularWork().addAll(Arrays.asList(day1ActualWork, day2ActualWork, actualWork));
   }

   /**
    * Create a split task, 1 working day, 1 non-working day
    * followed by the rest of the work.
    *
    * @param file parent file
    */
   private void addSplitTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 7");

      Task task = file.addTask();
      task.setName("Task 7 - Split");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));

      // This is important - MS Project will accept the timephased data without this,
      // but the split won't show up on the Gantt Chart unless ths is set
      assignment.setWorkContour(WorkContour.CONTOURED);

      // Day 1 - 8h
      TimephasedWork day1RemainingWork = new TimephasedWork();
      day1RemainingWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1RemainingWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1RemainingWork.setTotalAmount(Duration.getInstance(8, TimeUnit.HOURS));

      // Day 2 - split

      // Remaining days - 8h/day
      // Note the gap between the end of the first working day and the start of the next working day.
      // This gives us the split.
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      remainingWork.setTotalAmount(Duration.getInstance(32, TimeUnit.HOURS));

      assignment.getRawTimephasedRemainingRegularWork().addAll(Arrays.asList(day1RemainingWork, remainingWork));
   }

   /**
    * Split task with 1 day, a gap of 1 day, then the remaining work.
    * 4h of work has been done on the first day.
    *
    * @param file parent file
    */
   private void addSplitTaskPartiallyCompleteSplit(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 8");

      Task task = file.addTask();
      task.setName("Task 8 - Split Partially Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(4, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(36, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getActualStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(4, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(36, TimeUnit.HOURS));

      // Important - MS Project needs this as well as the timephased data
      // to correctly represent the actual and remaining work
      assignment.setStop(LocalDateTime.of(2024, 3, 4, 12, 0));
      assignment.setResume(LocalDateTime.of(2024, 3, 4, 13, 0));

      // This is important - MS Project will accept the timephased data without this,
      // but the split won't show up on the Gantt Chart unless ths is set
      assignment.setWorkContour(WorkContour.CONTOURED);

      // Day 1 actual - 4h
      TimephasedWork day1ActualWork = new TimephasedWork();
      day1ActualWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1ActualWork.setFinish(LocalDateTime.of(2024, 3, 4, 12, 0));
      day1ActualWork.setTotalAmount(Duration.getInstance(4, TimeUnit.HOURS));

      // Day 1 remaining - 4h
      TimephasedWork day1RemainingWork = new TimephasedWork();
      day1RemainingWork.setStart(LocalDateTime.of(2024, 3, 4, 13, 0));
      day1RemainingWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1RemainingWork.setTotalAmount(Duration.getInstance(4, TimeUnit.HOURS));

      // Day 2 - split

      // Remaining days - 8h/day
      // Note the gap between the end of the first working day and the start of the next working day.
      // This gives us the split.
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      remainingWork.setTotalAmount(Duration.getInstance(36, TimeUnit.HOURS));

      assignment.getRawTimephasedActualRegularWork().add(day1ActualWork);
      assignment.getRawTimephasedRemainingRegularWork().addAll(Arrays.asList(day1RemainingWork, remainingWork));
   }

   /**
    * Split task with 1 day, a gap of 1 day, then the remaining work.
    * The first split (8h) is complete.
    *
    * @param file parent file
    */
   private void addSplitTaskFirstSplitComplete(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 9");

      Task task = file.addTask();
      task.setName("Task 9 - Split First Split Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(8, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(32, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getActualStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(8, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(32, TimeUnit.HOURS));

      // Important - MS Project needs this as well as the timephased data
      // to correctly represent the actual and remaining work
      assignment.setStop(LocalDateTime.of(2024, 3, 4, 17, 0));
      assignment.setResume(LocalDateTime.of(2024, 3, 4, 17, 0));

      // This is important - MS Project will accept the timephased data without this,
      // but the split won't show up on the Gantt Chart unless ths is set
      assignment.setWorkContour(WorkContour.CONTOURED);

      // Day 1 actual - 4h
      TimephasedWork day1ActualWork = new TimephasedWork();
      day1ActualWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1ActualWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1ActualWork.setTotalAmount(Duration.getInstance(8, TimeUnit.HOURS));

      // Day 2 - split

      // Remaining days - 8h/day
      // Note the gap between the end of the first working day and the start of the next working day.
      // This gives us the split.
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      remainingWork.setTotalAmount(Duration.getInstance(32, TimeUnit.HOURS));

      assignment.getRawTimephasedActualRegularWork().add(day1ActualWork);
      assignment.getRawTimephasedRemainingRegularWork().add(remainingWork);
   }

   /**
    * Split task with 1 day, a gap of 1 day, then the remaining work.
    * The first split (8h) is complete, second spit has 4h actual work.
    *
    * @param file parent file
    */
   private void addSplitTaskSecondSplitPartiallyComplete(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 10");

      Task task = file.addTask();
      task.setName("Task 10 - Split Second Split Partially Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(16, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(24, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getActualStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(16, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(24, TimeUnit.HOURS));

      // Important - MS Project needs this as well as the timephased data
      // to correctly represent the actual and remaining work
      assignment.setStop(LocalDateTime.of(2024, 3, 6, 17, 0));
      assignment.setResume(LocalDateTime.of(2024, 3, 6, 17, 0));

      // This is important - MS Project will accept the timephased data without this,
      // but the split won't show up on the Gantt Chart unless ths is set
      assignment.setWorkContour(WorkContour.CONTOURED);

      // Day 1 actual - 4h
      TimephasedWork day1ActualWork = new TimephasedWork();
      day1ActualWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1ActualWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1ActualWork.setTotalAmount(Duration.getInstance(8, TimeUnit.HOURS));

      // Day 2 - split

      // Day 3 actual work
      TimephasedWork day3ActualWork = new TimephasedWork();
      day3ActualWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      day3ActualWork.setFinish(LocalDateTime.of(2024, 3, 6, 17, 0));
      day3ActualWork.setTotalAmount(Duration.getInstance(8, TimeUnit.HOURS));

      // Remaining days - 8h/day
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 7, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      remainingWork.setTotalAmount(Duration.getInstance(24, TimeUnit.HOURS));

      assignment.getRawTimephasedActualRegularWork().addAll(Arrays.asList(day1ActualWork, day3ActualWork, remainingWork));
      assignment.getRawTimephasedRemainingRegularWork().add(remainingWork);
   }

   /**
    * Split task with 1 day, a gap of 1 day, then the remaining work.
    * Entire task is complete.
    *
    * @param file parent file
    */
   private void addSplitTaskComplete(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 11");

      Task task = file.addTask();
      task.setName("Task 11 - Split Complete");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setActualStart(task.getStart());
      task.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setActualWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(0, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setActualStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setActualWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(0, TimeUnit.HOURS));

      // This is important - MS Project will accept the timephased data without this,
      // but the split won't show up on the Gantt Chart unless ths is set
      assignment.setWorkContour(WorkContour.CONTOURED);

      // Day 1 - 8h
      TimephasedWork day1actualWork = new TimephasedWork();
      day1actualWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1actualWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1actualWork.setTotalAmount(Duration.getInstance(8, TimeUnit.HOURS));

      // Day 2 - split

      // Remaining days - 8h/day
      // Note the gap between the end of the first working day and the start of the next working day.
      // This gives us the split.
      TimephasedWork actualWork = new TimephasedWork();
      actualWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      actualWork.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      actualWork.setTotalAmount(Duration.getInstance(32, TimeUnit.HOURS));

      assignment.getRawTimephasedActualRegularWork().addAll(Arrays.asList(day1actualWork, actualWork));
   }
}
