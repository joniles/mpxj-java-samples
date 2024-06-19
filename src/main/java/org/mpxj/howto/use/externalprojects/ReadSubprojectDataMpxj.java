package org.mpxj.howto.use.externalprojects;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

public class ReadSubprojectDataMpxj
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
      ProjectFile externalProjectFile = externalProjectTask.getSubprojectObject();
   }
}
