package org.mpxj.activity_codes;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mpxj.*;
import org.mpxj.reader.UniversalProjectReader;

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


   /**
    * Reads a schedule file and displays any activity code definitions it contains,
    * then lists all tasks along with any activity code value assignments.
    *
    * @param fileName file to open
    */
   private void process(String fileName) throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read(fileName);

      //
      // Display activity code definitions
      //
      System.out.println("This file contains the following activity code definitions:");
      for (ActivityCode code : file.getActivityCodes())
      {
         //
         // Display definition of this activity code
         //
         String scope = "Scope: " + code.getScope();
         switch (code.getScope())
         {
            case EPS:
            {
               scope = scope + " (EPS ID: " + code.getScopeEpsUniqueID() + ")";
               break;
            }

            case PROJECT:
            {
               scope = scope + " (PROJECT ID: " + code.getScopeProjectUniqueID() + ")";
               break;
            }
         }
         System.out.println(code.getUniqueID() + ": " + code.getName() + " (Sequence: " +code.getSequenceNumber() + ", " + scope + ")");

         //
         // Display the values defined for this activity code
         //
         for (ActivityCodeValue value : code.getValues())
         {
            String parent = value.getParentValue() == null ? null : "Parent: " + value.getParentValue().getUniqueID();
            String description = value.getDescription() == null || value.getDescription().isEmpty() ? null : "Description: " + value.getDescription();
            String sequenceNumber = "Sequence: " + value.getSequenceNumber();
            String color = value.getColor() == null ? null : "Color: " + getColor(value);

            String label = Stream.of(parent, description, sequenceNumber, color).filter(Objects::nonNull).collect(Collectors.joining(", "));
            System.out.println("\t" + value.getUniqueID() + ":\t" + value.getName() + "\t(" + label + ")");
         }
         System.out.println();
      }
      System.out.println();

      //
      // Display all tasks along with any activity codes assigned
      //
      System.out.println("Tasks showing any activity code values they have been assigned");
      for (Task task : file.getTasks())
      {
         //
         // Display task details
         //
         System.out.println(task.getUniqueID() + ":\t" + task.getActivityID() + "\t" + task.getName());

         //
         // Display any activity code values for this task
         //
         Map<ActivityCode, ActivityCodeValue> values = task.getActivityCodeValues();
         if (values.isEmpty())
         {
            System.out.println("\t(no activity codes assigned)");
         }
         else
         {
            for (Map.Entry<ActivityCode, ActivityCodeValue> entry : values.entrySet())
            {
               System.out.println("\t" + entry.getKey().getName() + ":\t" + entry.getValue().getName());
            }
         }
      }
   }

   private String getColor(ActivityCodeValue value)
   {
      String stringValue = "000000" + Integer.toHexString(value.getColor().getRGB()).toUpperCase();
      return "#" + stringValue.substring(stringValue.length() - 6);
   }
}
