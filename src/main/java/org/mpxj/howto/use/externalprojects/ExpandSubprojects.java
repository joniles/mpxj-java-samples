package org.mpxj.howto.use.externalprojects;

import org.mpxj.ProjectFile;
import org.mpxj.Task;
import org.mpxj.reader.UniversalProjectReader;

public class ExpandSubprojects
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
      System.out.println("Task has child tasks: " + externalProjectTask.hasChildTasks());
      externalProjectTask.expandSubproject();
      System.out.println("Task has child tasks: " + externalProjectTask.hasChildTasks());
   }
}
