package org.mpxj.howto.use.cpm;

import net.sf.mpxj.*;
import net.sf.mpxj.cpm.MicrosoftScheduler;
import net.sf.mpxj.cpm.SchedulerHelper;
import net.sf.mpxj.writer.FileFormat;
import net.sf.mpxj.writer.UniversalProjectWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MicrosoftSchedulerSample
{
   public static void main(String[] argv) throws Exception
   {
      MicrosoftSchedulerSample sample = new MicrosoftSchedulerSample();
      sample.projectWithoutResources("/Users/joniles/Downloads/project-without-resources.xml");
      sample.projectWithProgress("/Users/joniles/Downloads/project-with-progress.xml");
      sample.projectWithResources("/Users/joniles/Downloads/project-with-resources.xml");
      sample.projectWithResourcesAndProgress("/Users/joniles/Downloads/project-with-resources-and-progress.xml");
   }

   public void projectWithoutResources(String filename) throws Exception
   {
      ProjectFile file = new ProjectFile();

      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);

      Task summary1 = file.addTask();
      summary1.setName("Summary 1");

      Task task1 = summary1.addTask();
      task1.setName("Task 1");
      task1.setDuration(Duration.getInstance(4, TimeUnit.DAYS));

      Task task2 = summary1.addTask();
      task2.setName("Task 2");
      task2.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task3 = summary1.addTask();
      task3.setName("Task 3");
      task3.setDuration(Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = summary2.addTask();
      task4.setName("Task 4");
      task4.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task5 = summary2.addTask();
      task5.setName("Task 5");
      task5.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task6 = summary2.addTask();
      task6.setName("Task 6");
      task6.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = file.addTask();
      milestone1.setName("Milestone 1");
      milestone1.setDuration(Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      new MicrosoftScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.MSPDI).write(file, filename);
   }

   public void projectWithProgress(String filename) throws Exception
   {
      ProjectFile file = new ProjectFile();

      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);

      Task summary1 = file.addTask();
      summary1.setName("Summary 1");

      Task task1 = summary1.addTask();
      task1.setName("Task 1");
      task1.setDuration(Duration.getInstance(4, TimeUnit.DAYS));
      task1.setActualStart(LocalDateTime.of(2025, 4, 11, 8, 0));
      SchedulerHelper.progressTask(task1, 25.0);

      Task task2 = summary1.addTask();
      task2.setName("Task 2");
      task2.setDuration(Duration.getInstance(2, TimeUnit.DAYS));
      task2.setActualStart(LocalDateTime.of(2025, 4, 11, 8, 0));
      task2.setActualDuration(Duration.getInstance(1, TimeUnit.DAYS));
      task2.setRemainingDuration(Duration.getInstance(1, TimeUnit.DAYS));
      task2.setPercentageComplete(50.0);

      Task task3 = summary1.addTask();
      task3.setName("Task 3");
      task3.setDuration(Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = summary2.addTask();
      task4.setName("Task 4");
      task4.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task5 = summary2.addTask();
      task5.setName("Task 5");
      task5.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task6 = summary2.addTask();
      task6.setName("Task 6");
      task6.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = file.addTask();
      milestone1.setName("Milestone 1");
      milestone1.setDuration(Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      new MicrosoftScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.MSPDI).write(file, filename);
   }

   public void projectWithResources(String filename) throws Exception
   {
      ProjectFile file = new ProjectFile();

      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);

      Resource resource1 = file.addResource();
      resource1.setName("Resource 1");
      ProjectCalendar calendar1 = file.addDefaultDerivedCalendar();
      resource1.setCalendar(calendar1);
      calendar1.setParent(calendar);
      calendar1.setName("Resource 1");
      calendar1.addCalendarException(LocalDate.of(2025, 4, 14));

      Resource resource2 = file.addResource();
      resource2.setName("Resource 2");
      ProjectCalendar calendar2 = file.addDefaultDerivedCalendar();
      resource2.setCalendar(calendar2);
      calendar2.setParent(calendar);
      calendar2.setName("Resource 2");

      Task summary1 = file.addTask();
      summary1.setName("Summary 1");

      Task task1 = summary1.addTask();
      task1.setName("Task 1");
      ResourceAssignment assignment1 = task1.addResourceAssignment(resource1);
      assignment1.setWork(Duration.getInstance(32, TimeUnit.HOURS));

      Task task2 = summary1.addTask();
      task2.setName("Task 2");
      ResourceAssignment assignment2 = task2.addResourceAssignment(resource2);
      assignment2.setWork(Duration.getInstance(16, TimeUnit.HOURS));

      Task task3 = summary1.addTask();
      task3.setName("Task 3");
      task3.setDuration(Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = summary2.addTask();
      task4.setName("Task 4");
      task4.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task5 = summary2.addTask();
      task5.setName("Task 5");
      task5.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task6 = summary2.addTask();
      task6.setName("Task 6");
      task6.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = file.addTask();
      milestone1.setName("Milestone 1");
      milestone1.setDuration(Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      new MicrosoftScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.MSPDI).write(file, filename);
   }

   public void projectWithResourcesAndProgress(String filename) throws Exception
   {
      ProjectFile file = new ProjectFile();

      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);

      Resource resource1 = file.addResource();
      resource1.setName("Resource 1");
      ProjectCalendar calendar1 = file.addDefaultDerivedCalendar();
      resource1.setCalendar(calendar1);
      calendar1.setParent(calendar);
      calendar1.setName("Resource 1");
      calendar1.addCalendarException(LocalDate.of(2025, 4, 14));

      Resource resource2 = file.addResource();
      resource2.setName("Resource 2");
      ProjectCalendar calendar2 = file.addDefaultDerivedCalendar();
      resource2.setCalendar(calendar2);
      calendar2.setParent(calendar);
      calendar2.setName("Resource 2");

      Task summary1 = file.addTask();
      summary1.setName("Summary 1");

      Task task1 = summary1.addTask();
      task1.setName("Task 1");
      task1.setActualStart(LocalDateTime.of(2025, 4, 11, 8, 0));
      ResourceAssignment assignment1 = task1.addResourceAssignment(resource1);
      assignment1.setWork(Duration.getInstance(32, TimeUnit.HOURS));
      assignment1.setActualWork(Duration.getInstance(8, TimeUnit.HOURS));
      assignment1.setRemainingWork(Duration.getInstance(24, TimeUnit.HOURS));

      Task task2 = summary1.addTask();
      task2.setName("Task 2");
      task2.setActualStart(LocalDateTime.of(2025, 4, 11, 8, 0));
      ResourceAssignment assignment2 = task2.addResourceAssignment(resource2);
      assignment2.setWork(Duration.getInstance(16, TimeUnit.HOURS));
      SchedulerHelper.progressAssignment(assignment2, 50.0);

      Task task3 = summary1.addTask();
      task3.setName("Task 3");
      task3.setDuration(Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = summary2.addTask();
      task4.setName("Task 4");
      task4.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task5 = summary2.addTask();
      task5.setName("Task 5");
      task5.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task6 = summary2.addTask();
      task6.setName("Task 6");
      task6.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = file.addTask();
      milestone1.setName("Milestone 1");
      milestone1.setDuration(Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      new MicrosoftScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.MSPDI).write(file, filename);
   }

   private void printTasks(ProjectFile file)
   {
      System.out.println(writeTaskHeaders());
      file.getTasks().forEach(t -> System.out.println(writeTaskData(t)));
   }

   private String writeTaskHeaders()
   {
      return TASK_COLUMNS.stream().map(TaskField::toString).collect(Collectors.joining("\t"));
   }

   private String writeTaskData(Task task)
   {
      return TASK_COLUMNS.stream().map(c -> String.valueOf(task.get(c))).collect(Collectors.joining("\t"));
   }

   private static final List<TaskField> TASK_COLUMNS = Arrays.asList(
      TaskField.ID,
      TaskField.UNIQUE_ID,
      TaskField.NAME,
      TaskField.DURATION,
      TaskField.START,
      TaskField.FINISH,
      TaskField.EARLY_START,
      TaskField.EARLY_FINISH,
      TaskField.LATE_START,
      TaskField.LATE_FINISH,
      TaskField.TOTAL_SLACK,
      TaskField.CRITICAL);
}
