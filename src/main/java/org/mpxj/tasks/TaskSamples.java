package org.mpxj.tasks;

import net.sf.mpxj.ChildTaskContainer;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

public class TaskSamples
{
   public static void main(String[] args) throws Exception
   {
      //new TaskSamples().listTaskHierarchy("sample.mpp");
      new TaskSamples().listTaskDates("sample.mpp");
   }

   /**
    * Recursively descend through the task hierarchy displaying the
    * task ID and the name, using tabs as indents to show the
    * task hierarchy.
    *
    * @param fileName file to read
    */
   public void listTaskHierarchy(String fileName) throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read(fileName);
      listTaskHierarchy(file, "\t");
   }

   private void listTaskHierarchy(ChildTaskContainer parent, String indent)
   {
      for(Task task : parent.getChildTasks())
      {
         System.out.println(String.format("%3d", task.getID()) + indent + task.getName().trim());
         listTaskHierarchy(task, indent + "\t");
      }
   }

   public void listTaskDates(String fileName) throws Exception
   {
      System.out.println(fileName);
      ProjectFile file = new UniversalProjectReader().read(fileName);
      System.out.println("ID\tActualStart\tActualDuration\tRemainingDuration");
      for(Task task : file.getTasks())
      {
         System.out.println(task.getID() + "\t" + task.getActualStart() + "\t" + task.getActualDuration() + "\t" + task.getRemainingDuration());
      }
      System.out.println();
   }
}
