package org.mpxj.howto.use.externalprojects;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

public class ExternalPredecessors
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      for (Task task : file.getTasks())
      {
         if (task.getExternalTask())
         {
            System.out.println(task.getName() + " is an external predecessor");
            System.out.println("The path to the file containing this task is: "
               + task.getSubprojectFile());
            System.out.println("The ID of the task in this file is: "
               + task.getSubprojectTaskID());
            System.out.println("The Unique ID of the task in this file is: "
               + task.getSubprojectTaskUniqueID());
         }
      }
   }
}
