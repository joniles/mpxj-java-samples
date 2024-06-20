package org.mpxj.howto.use.externalprojects;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

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
