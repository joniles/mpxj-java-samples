package org.mpxj.timephased;

import net.sf.mpxj.*;
import net.sf.mpxj.common.DefaultTimephasedWorkContainer;
import net.sf.mpxj.mspdi.MSPDIWriter;

import java.time.LocalDateTime;
import java.util.Arrays;

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
      addCustomDistributionTask(file);
      addSplitTask(file);
      addSplitTaskPartiallyCompleteSplit(file);

      MSPDIWriter writer = new MSPDIWriter();

      // By default, timephased data is not written, so we need to enable it
      writer.setWriteTimephasedData(true);

      // tell MPXJ not to change the timephased data before writing it
      writer.setSplitTimephasedAsDays(false);

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
    * We'll create timephased data to allow us to show that we work
    * 10h on day 1, 6h on day 2, then 8h per day for the remaining days.
    *
    * @param file parent file
    */
   private void addCustomDistributionTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 2");

      Task task = file.addTask();
      task.setName("Task 2 - Custom Distribution");
      task.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      task.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      task.setDuration(Duration.getInstance(40, TimeUnit.HOURS));
      task.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      task.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));

      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setStart(task.getStart());
      assignment.setWork(Duration.getInstance(40, TimeUnit.HOURS));
      assignment.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));

      // Day 1 - 10h
      TimephasedWork day1RemainingWork = new TimephasedWork();
      day1RemainingWork.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1RemainingWork.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1RemainingWork.setTotalAmount(Duration.getInstance(10, TimeUnit.HOURS));

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

      DefaultTimephasedWorkContainer work = new DefaultTimephasedWorkContainer(assignment, null, Arrays.asList(day1RemainingWork, day2RemainingWork, remainingWork), false);
      assignment.setTimephasedWork(work);
   }


   /**
    * Create a split task, 1 working day, 1 non-working day
    * followed by the rest of the work.
    *
    * @param file parent file
    */
   private void addSplitTask(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 3");

      Task task = file.addTask();
      task.setName("Task 3 - Split");
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

      // Remaining days - 8h/day
      // Note the gap between the end of the first working day and the start of the next working day.
      // This gives us the split.
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      remainingWork.setTotalAmount(Duration.getInstance(32, TimeUnit.HOURS));

      DefaultTimephasedWorkContainer work = new DefaultTimephasedWorkContainer(assignment, null, Arrays.asList(day1RemainingWork, remainingWork), false);
      assignment.setTimephasedWork(work);
   }

   private void addSplitTaskPartiallyCompleteSplit(ProjectFile file) {
      Resource resource = file.addResource();
      resource.setName("Resource 4");

      Task task = file.addTask();
      task.setName("Task 4 - Split Partially Complete");
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

      // Remaining days - 8h/day
      // Note the gap between the end of the first working day and the start of the next working day.
      // This gives us the split.
      TimephasedWork remainingWork = new TimephasedWork();
      remainingWork.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainingWork.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      remainingWork.setTotalAmount(Duration.getInstance(36, TimeUnit.HOURS));

      DefaultTimephasedWorkContainer actualWork = new DefaultTimephasedWorkContainer(assignment, null, Arrays.asList(day1ActualWork), false);
      assignment.setTimephasedActualWork(actualWork);

      DefaultTimephasedWorkContainer remainingWork = new DefaultTimephasedWorkContainer(assignment, null, Arrays.asList(day1RemainingWork, remainingWork), false);
      assignment.setTimephasedWork(remainingWork);
   }
}
