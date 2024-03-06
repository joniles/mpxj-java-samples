package org.mpxj.timephased;

import net.sf.mpxj.*;
import net.sf.mpxj.common.DefaultTimephasedWorkContainer;
import net.sf.mpxj.mspdi.MSPDIWriter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WriteTimephasedData {

   public static void main(String[] argv) throws Exception {

      if (argv.length != 1) {
         System.out.println("Usage: WriteTimephasedData <output file name>");
         return;
      }

      // Set the start of our timescale
      LocalDateTime startDate = LocalDateTime.of(2023, 6, 1, 0, 0);

      new WriteTimephasedData().process(argv[0]);
   }

   public void process(String outputFileName) throws Exception
   {
      ProjectFile file = new ProjectFile();
      file.addDefaultBaseCalendar();

      addFlatDistributionTask(file);
      addCustomDistributionTask(file);
      addSplitTask(file);

      MSPDIWriter writer = new MSPDIWriter();
      writer.setWriteTimephasedData(true);
      writer.setSplitTimephasedAsDays(false);
      writer.write(file, outputFileName);
   }

   /**
    * We don't need to write timephased data for the resource assignment as
    * we're undertaking the default amount of work per day.
    *
    * @param file parent file
    */
   private void addFlatDistributionTask(ProjectFile file)
   {
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
   private void addCustomDistributionTask(ProjectFile file)
   {
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
      assignment.setWorkContour(WorkContour.CONTOURED);

      TimephasedWork day1 = new TimephasedWork();
      day1.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1.setTotalAmount(Duration.getInstance(10, TimeUnit.HOURS));

      TimephasedWork day2 = new TimephasedWork();
      day2.setStart(LocalDateTime.of(2024, 3, 5, 8, 0));
      day2.setFinish(LocalDateTime.of(2024, 3, 5, 17, 0));
      day2.setTotalAmount(Duration.getInstance(6, TimeUnit.HOURS));

      TimephasedWork remainder = new TimephasedWork();
      remainder.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainder.setFinish(LocalDateTime.of(2024, 3, 8, 17, 0));
      remainder.setTotalAmount(Duration.getInstance(24, TimeUnit.HOURS));

      DefaultTimephasedWorkContainer work = new DefaultTimephasedWorkContainer(assignment, null, Arrays.asList(day1, day2, remainder), false);
      assignment.setTimephasedWork(work);
   }


   /**
    * In theory, we are creating a split task. However, this functionality does not completely work
    * presently. At the moment, MPXJ only supports timephased data for resource assignments,
    * but MS Project actually writes timephased data for
    * @param file
    */
   private void addSplitTask(ProjectFile file)
   {
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
      assignment.setWorkContour(WorkContour.CONTOURED);
      
      TimephasedWork day1 = new TimephasedWork();
      day1.setStart(LocalDateTime.of(2024, 3, 4, 8, 0));
      day1.setFinish(LocalDateTime.of(2024, 3, 4, 17, 0));
      day1.setTotalAmount(Duration.getInstance(8, TimeUnit.HOURS));

      TimephasedWork day2 = new TimephasedWork();
      day2.setStart(LocalDateTime.of(2024, 3, 5, 8, 0));
      day2.setFinish(LocalDateTime.of(2024, 3, 5, 17, 0));
      day2.setTotalAmount(Duration.getInstance(0, TimeUnit.HOURS));

      TimephasedWork remainder = new TimephasedWork();
      remainder.setStart(LocalDateTime.of(2024, 3, 6, 8, 0));
      remainder.setFinish(LocalDateTime.of(2024, 3, 11, 17, 0));
      remainder.setTotalAmount(Duration.getInstance(32, TimeUnit.HOURS));

      DefaultTimephasedWorkContainer work = new DefaultTimephasedWorkContainer(assignment, null, Arrays.asList(day1, day2, remainder), false);
      assignment.setTimephasedWork(work);
   }
}
