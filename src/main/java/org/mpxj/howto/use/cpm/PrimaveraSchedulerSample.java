package org.mpxj.howto.use.cpm;

import net.sf.mpxj.*;
import net.sf.mpxj.cpm.CpmException;
import net.sf.mpxj.cpm.PrimaveraScheduler;
import net.sf.mpxj.writer.FileFormat;
import net.sf.mpxj.writer.UniversalProjectWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrimaveraSchedulerSample
{
   public static void main(String[] argv) throws Exception
   {
      PrimaveraSchedulerSample sample = new PrimaveraSchedulerSample();
      //sample.projectWithoutResources("/Users/joniles/Downloads/project-without-resources.xml");
      //sample.projectWithProgress("/Users/joniles/Downloads/project-with-progress.xml");
      sample.projectWithResources("/Users/joniles/Downloads/project-with-resources.xml");
//      sample.projectWithResourcesAndProgress("/Users/joniles/Downloads/project-with-resources-and-progress.xml");
   }

   public void projectWithoutResources(String filename) throws Exception
   {
      ProjectFile file = new ProjectFile();

      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);

      Task summary1 = file.addTask();
      summary1.setName("Summary 1");

      Task task1 = createTask(summary1, ActivityType.TASK_DEPENDENT, "Task 1", Duration.getInstance(4, TimeUnit.DAYS));
      Task task2 = createTask(summary1, ActivityType.TASK_DEPENDENT,"Task 2", Duration.getInstance(2, TimeUnit.DAYS));
      Task task3 = createTask(summary1, ActivityType.TASK_DEPENDENT,"Task 3", Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = createTask(summary2, ActivityType.TASK_DEPENDENT, "Task 4", Duration.getInstance(2, TimeUnit.DAYS));
      Task task5 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 5", Duration.getInstance(2, TimeUnit.DAYS));
      Task task6 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 6", Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = createTask(file, ActivityType.FINISH_MILESTONE,"Milestone 1", Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      new PrimaveraScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.PMXML).write(file, filename);
   }

   public void projectWithProgress(String filename) throws Exception
   {
      ProjectFile file = new ProjectFile();

      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);

      Task summary1 = file.addTask();
      summary1.setName("Summary 1");

      Task task1 = createTask(summary1, ActivityType.TASK_DEPENDENT,"Task 1", Duration.getInstance(4, TimeUnit.DAYS));
      Task task2 = createTask(summary1, ActivityType.TASK_DEPENDENT,"Task 2", Duration.getInstance(2, TimeUnit.DAYS));
      Task task3 = createTask(summary1, ActivityType.TASK_DEPENDENT,"Task 3", Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 4", Duration.getInstance(2, TimeUnit.DAYS));
      Task task5 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 5", Duration.getInstance(2, TimeUnit.DAYS));
      Task task6 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 6", Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = createTask(file, ActivityType.FINISH_MILESTONE,"Milestone 1", Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      progressTask(task1, LocalDateTime.of(2025, 4, 11, 8, 0), 25.0);
      progressTask(task2, LocalDateTime.of(2025, 4, 11, 8, 0), 50.0);
      file.getProjectProperties().setStatusDate(LocalDateTime.of(2025, 4, 11, 17, 0));

      new PrimaveraScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.PMXML).write(file, filename);
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
      task1.setActivityType(ActivityType.RESOURCE_DEPENDENT);
      task1.setName("Task 1");
      createResourceAssignment(task1, resource1, Duration.getInstance(32, TimeUnit.HOURS));

      Task task2 = summary1.addTask();
      task2.setActivityType(ActivityType.RESOURCE_DEPENDENT);
      task2.setName("Task 2");
      createResourceAssignment(task2, resource2, Duration.getInstance(16, TimeUnit.HOURS));

      Task task3 = createTask(summary1, ActivityType.TASK_DEPENDENT,"Task 3", Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 4", Duration.getInstance(2, TimeUnit.DAYS));
      Task task5 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 5", Duration.getInstance(2, TimeUnit.DAYS));
      Task task6 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 6", Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = createTask(file, ActivityType.FINISH_MILESTONE,"Milestone 1", Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      new PrimaveraScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.PMXML).write(file, filename);
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
      task1.setActivityType(ActivityType.RESOURCE_DEPENDENT);
      task1.setName("Task 1");

      ResourceAssignment assignment1 = createResourceAssignment(task1, resource1, Duration.getInstance(32, TimeUnit.HOURS));

      Task task2 = summary1.addTask();
      task2.setActivityType(ActivityType.RESOURCE_DEPENDENT);
      task2.setName("Task 2");
      ResourceAssignment assignment2 = createResourceAssignment(task2, resource2, Duration.getInstance(16, TimeUnit.HOURS));

      Task task3 = createTask(summary1, ActivityType.TASK_DEPENDENT,"Task 3", Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task summary2 = file.addTask();
      summary2.setName("Summary 2");

      Task task4 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 4", Duration.getInstance(2, TimeUnit.DAYS));
      Task task5 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 5", Duration.getInstance(2, TimeUnit.DAYS));
      Task task6 = createTask(summary2, ActivityType.TASK_DEPENDENT,"Task 6", Duration.getInstance(2, TimeUnit.DAYS));

      task6.addPredecessor(new Relation.Builder().predecessorTask(task4));
      task6.addPredecessor(new Relation.Builder().predecessorTask(task5).lag(Duration.getInstance(1, TimeUnit.DAYS)));

      Task milestone1 = createTask(file, ActivityType.FINISH_MILESTONE,"Milestone 1", Duration.getInstance(0, TimeUnit.DAYS));

      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task3));
      milestone1.addPredecessor(new Relation.Builder().predecessorTask(task6));

      task1.setActualStart(LocalDateTime.of(2025, 4, 11, 8, 0));
      progressAssignment(assignment1, 25.0);

      task2.setActualStart(LocalDateTime.of(2025, 4, 11, 8, 0));
      progressAssignment(assignment2, 50.0);

      new PrimaveraScheduler().schedule(file, LocalDateTime.of(2025, 4, 11, 8, 0));

      printTasks(file);

      new UniversalProjectWriter(FileFormat.PMXML).write(file, filename);
   }

   private Task createTask(ChildTaskContainer parent, ActivityType type, String name, Duration duration)
   {
      Task task = parent.addTask();
      task.setActivityType(type);
      task.setName(name);
      task.setDuration(duration);
      task.setActualDuration(Duration.getInstance(0, duration.getUnits()));
      task.setRemainingDuration(duration);
      return task;
   }

   private ResourceAssignment createResourceAssignment(Task task, Resource resource, Duration work)
   {
      ResourceAssignment assignment = task.addResourceAssignment(resource);
      assignment.setWork(work);
      assignment.setActualWork(Duration.getInstance(0, work.getUnits()));
      assignment.setRemainingWork(work);
      return assignment;
   }

   private void progressTask(Task task, LocalDateTime actualStart, double percentComplete) throws CpmException
   {
      double durationValue = task.getDuration().getDuration();
      TimeUnit durationUnits = task.getDuration().getUnits();
      task.setActualStart(actualStart);
      task.setPercentageComplete(percentComplete);
      task.setActualDuration(Duration.getInstance((percentComplete * durationValue) / 100.0, durationUnits));
      task.setRemainingDuration(Duration.getInstance(((100.0 - percentComplete) * durationValue) / 100.0, durationUnits));
   }

   public static void progressAssignment(ResourceAssignment assignment, double percentComplete) throws CpmException
   {
      double workValue = assignment.getWork().getDuration();
      TimeUnit workUnits = assignment.getWork().getUnits();
      assignment.setPercentageWorkComplete(percentComplete);
      assignment.setActualWork(Duration.getInstance((percentComplete * workValue) / 100.0, workUnits));
      assignment.setRemainingWork(Duration.getInstance(((100.0 - percentComplete) * workValue) / 100.0, workUnits));
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
