package org.mpxj.howto.use.timephased;

import java.time.format.DateTimeFormatter;

import org.mpxj.LocalDateTimeRange;
import org.mpxj.MPXJException;
import org.mpxj.ProjectFile;
import org.mpxj.Task;
import org.mpxj.reader.UniversalProjectReader;

public class SplitTaskSample
{
   public static void main(String[] argv) throws Exception
   {

      if (argv.length != 1) {
         System.out.println("Usage: SplitTaskSample <input file name>");
         return;
      }

      new SplitTaskSample().process(argv[0]);
   }

   public void process(String filename) throws MPXJException
   {
      ProjectFile file = new UniversalProjectReader().read(filename);
      System.out.println("|ID|Name|Start|Finish|");
      System.out.println("|---|---|---|---|");
      file.getTasks().forEach(this::dumpSplitDetails);
   }

   private void dumpSplitDetails(Task task)
   {
      System.out.println("|" + task.getID() + " | " + task.getName() + "|||");
      for (LocalDateTimeRange range : task.getWorkSplits())
      {
         System.out.println("|||" + DATE_FORMAT.format(range.getStart()) + " | " + DATE_FORMAT.format(range.getEnd()) + "|");
      }
   }

   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}
