package org.mpxj.howto.use.externalprojects;

import org.mpxj.ProjectFile;
import org.mpxj.Task;
import org.mpxj.reader.UniversalProjectReader;

import java.io.File;

public class ReadSubprojectDataDirectory
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      file.getProjectConfig().setSubprojectWorkingDirectory(new File("/path/to/directory"));
      Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
      ProjectFile externalProjectFile = externalProjectTask.getSubprojectObject();
   }
}
