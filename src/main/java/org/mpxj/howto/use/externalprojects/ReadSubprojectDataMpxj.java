package org.mpxj.howto.use.externalprojects;

import org.mpxj.ProjectFile;
import org.mpxj.Task;
import org.mpxj.reader.UniversalProjectReader;

public class ReadSubprojectDataMpxj
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
      ProjectFile externalProjectFile = externalProjectTask.getSubprojectObject();
   }
}
