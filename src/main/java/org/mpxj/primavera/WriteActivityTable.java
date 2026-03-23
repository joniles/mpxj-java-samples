package org.mpxj.primavera;

import org.mpxj.*;
import org.mpxj.reader.UniversalProjectReader;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class prints a tabular view of the WBS and Activity hierarchy from P6.
 * The columns displayed are passed as arguments. If the equivalent table
 * is set up in P6 and copied as text, the result can be compared directly
 * with the output generated here using diff. This can be used to verify that
 * MPXJ provides the same results as P6.
 */
public class WriteActivityTable {
   public static void main(String[] argv) throws Exception {
      new WriteActivityTable().process(argv[0]);
   }

   public void process(String filename) throws MPXJException {
      ProjectFile file = new UniversalProjectReader().read(filename);
      writeTable(file, TaskField.ACTIVITY_ID, TaskField.PLANNED_COST, TaskField.PLANNED_COST_LABOR, TaskField.PLANNED_COST_NON_LABOR);
   }

   public void writeTable(ProjectFile file, TaskField... fields) {
      writeTasks("", file.getChildTasks(), fields);
   }

   private void writeTasks(String prefix, List<Task> tasks, TaskField[] fields)
   {
      String newPrefix = prefix + "  ";
      for (Task task : tasks)
      {
         String values = Arrays.stream(fields).map(f -> formatField(task, f)).collect(Collectors.joining("\t"));
         System.out.println(prefix + values);
         writeTasks(newPrefix, task.getChildTasks(), fields);
      }
   }

   private String formatField(Task task, TaskField field)
   {
      if (field == TaskField.ACTIVITY_ID)
      {
         String label = task.getActivityID();
         if (task.getSummary()) {
            label = label + "  " + task.getName();
         }
         return label;
      }

      Object value = task.get(field);

      switch(field.getDataType())
      {
         case DURATION:
         {
            return formatDuration((Duration)value);
         }

         case CURRENCY:
         {
            return formatCurrency((Number)value);
         }

         default:
         {
            return String.valueOf(value);
         }
      }
   }

   private String formatDuration(Duration duration)
   {
      if (duration == null)
      {
         return "";
      }
      return CURRENCY_FORMAT.format(duration.getDuration());
   }

   private String formatCurrency(Number cost)
   {
      if (cost == null)
      {
         cost = (double) 0;
      }
      return CURRENCY_FORMAT.format(cost);
   }

   private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#,###,##0.00");
}
