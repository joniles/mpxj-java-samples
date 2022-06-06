package org.mpxj.activity_codes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.sf.mpxj.*;
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
         String scope = "Scope: " + code.getScope();
         if (code.getScope() != ActivityCodeScope.GLOBAL)
         {
            scope = scope + " (ID: " + code.getScopeUniqueID() + ")";
         }

         System.out.println(code.getUniqueID() + ": " + code.getName() + " (Sequence: " +code.getSequenceNumber() + ", " + scope + ")");

         
         for (ActivityCodeValue value : code.getValues())
         {
            String parent = value.getParent() == null ? null : "Parent: " + value.getParent().getUniqueID();
            String description = value.getDescription() == null || value.getDescription().isEmpty() ? null : "Description: " + value.getDescription();
            String sequenceNumber = "Sequence: " + value.getSequenceNumber();
            String color = value.getColor() == null ? null : "Color: " + getColor(value);

            String label = Stream.of(parent, description, sequenceNumber, color).filter(s -> s != null).collect(Collectors.joining(", "));
            System.out.println("\t" + value.getUniqueID() + ":\t" + value.getName() + "\t(" + label + ")");
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

   private String getColor(ActivityCodeValue value)
   {
      String stringValue = "000000" + Integer.toHexString(value.getColor().getRGB()).toUpperCase();
      return "#" + stringValue.substring(stringValue.length() - 6);
   }
}
