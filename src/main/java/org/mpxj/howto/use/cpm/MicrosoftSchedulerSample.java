package org.mpxj.howto.use.cpm;

import net.sf.mpxj.*;
import net.sf.mpxj.cpm.MicrosoftScheduler;
import net.sf.mpxj.writer.FileFormat;
import net.sf.mpxj.writer.UniversalProjectWriter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MicrosoftSchedulerSample
{
   public static void main(String[] argv) throws Exception
   {
      new MicrosoftSchedulerSample().process("/Users/joniles/Downloads/test.xml");
   }

   public void process(String filename) throws Exception
   {
      ProjectFile file = new ProjectFile();

      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      file.setDefaultCalendar(calendar);

      Task task1 = file.addTask();
      task1.setName("Task 1");
      task1.setDuration(Duration.getInstance(3, TimeUnit.DAYS));

      Task task2 = file.addTask();
      task2.setName("Task 2");
      task2.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task3 = file.addTask();
      task3.setName("Task 3");
      task3.setDuration(Duration.getInstance(5, TimeUnit.DAYS));

      task3.addPredecessor(new Relation.Builder().predecessorTask(task1));
      task3.addPredecessor(new Relation.Builder().predecessorTask(task2));

      Task task4 = file.addTask();
      task4.setName("Task 4");
      task4.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task5 = file.addTask();
      task5.setName("Task 5");
      task5.setDuration(Duration.getInstance(2, TimeUnit.DAYS));

      Task task6 = file.addTask();
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

      System.out.println(writeTaskHeaders());
      file.getTasks().stream().forEach(t -> System.out.println(writeTaskData(t)));

      new UniversalProjectWriter(FileFormat.MSPDI).write(file, filename);
   }

   private String writeTaskHeaders()
   {
      return TASK_COLUMNS.stream().map(c -> c.toString()).collect(Collectors.joining("\t"));
   }

   private String writeTaskData(Task task)
   {
      return TASK_COLUMNS.stream().map(c -> task.get(c).toString()).collect(Collectors.joining("\t"));
   }

   private static final List<TaskField> TASK_COLUMNS = Arrays.asList(new TaskField[]{
      TaskField.ID,
      TaskField.UNIQUE_ID,
      TaskField.NAME,
      TaskField.START,
      TaskField.FINISH,
      TaskField.EARLY_START,
      TaskField.EARLY_FINISH,
      TaskField.LATE_START,
      TaskField.LATE_FINISH,
      TaskField.TOTAL_SLACK,
      TaskField.CRITICAL
   });
}
