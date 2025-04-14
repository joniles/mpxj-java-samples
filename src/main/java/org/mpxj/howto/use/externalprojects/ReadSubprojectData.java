package org.mpxj.howto.use.externalprojects;

import org.mpxj.ProjectFile;
import org.mpxj.Task;
import org.mpxj.reader.UniversalProjectReader;

public class ReadSubprojectData
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      Task externalProject = file.getTaskByID(Integer.valueOf(1));
      String filePath = externalProject.getSubprojectFile();
      ProjectFile externalProjectFile = new UniversalProjectReader().read(filePath);
   }
}
