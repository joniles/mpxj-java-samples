package org.mpxj.howto.use.externalprojects;

import org.mpxj.ProjectFile;
import org.mpxj.Task;
import org.mpxj.reader.UniversalProjectReader;

public class IdentifySubprojectTasks
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      for (Task task : file.getTasks())
      {
         if (task.getExternalProject())
         {
            System.out.println(task.getName() + " is a subproject");
            System.out.println("The path to the file is: "
               + task.getSubprojectFile());
            System.out.println("The GUID of this project is: "
               + task.getSubprojectGUID());
            System.out.println("The offset used when displaying Unique ID values is: "
               + task.getSubprojectTasksUniqueIDOffset());
         }
      }
   }
}
