package org.mpxj.activity_codes;

import java.util.List;

import net.sf.mpxj.ActivityCode;
import net.sf.mpxj.ActivityCodeValue;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

public class ReadActivityCodes
{
   public static void main(String[] argv) throws Exception {

      if (argv.length != 1)
      {
         System.out.println("Usage: ReadActivityCodes <file name>");
         return;
      }

      ReadActivityCodes reader = new ReadActivityCodes();
      reader.process(argv[0]);
   }


   private void process(String fileName) throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read(fileName);

      System.out.println("This file contains the following activity code definitions:");
      for (ActivityCode code : file.getActivityCodes())
      {
         System.out.println(code.getUniqueID() + ": " + code.getName());
         for (ActivityCodeValue value : code.getValues())
         {
            String parent = value.getParent() == null ? "" : "Parent: " + value.getParent().getUniqueID();
            String description = value.getDescription() == null || value.getDescription().isEmpty() ? "" : "Description: " + value.getDescription();
            String space = !parent.isEmpty() && !description.isEmpty() ? " " : "";

            System.out.println("\t" + value.getUniqueID() + ":\t" + value.getName() + "\t(" + parent + space + description + ")");
         }
         System.out.println();
      }
      System.out.println();

      System.out.println("");
      for (Task task : file.getTasks())
      {
         System.out.println(task.getUniqueID() + ":\t" + task.getActivityID() + "\t" + task.getName());
         List<ActivityCodeValue> values = task.getActivityCodes();
         if (values.isEmpty())
         {
            System.out.println("\t(no activity codes assigned)");
         }
         else
         {
            for (ActivityCodeValue value : values)
            {
               System.out.println("\t" + value.getType().getName() + ":\t" + value.getName());
            }
         }
      }
   }
}
