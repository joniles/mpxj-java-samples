package org.mpxj.tasks;

import net.sf.mpxj.ChildTaskContainer;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

public class TaskSamples
{
   public static void main(String[] args) throws Exception
   {
      new TaskSamples().listTaskHierarchy("/Users/joniles/Downloads/ImportTest.mpp");
   }

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
}
