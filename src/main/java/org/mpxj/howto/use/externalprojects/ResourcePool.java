package org.mpxj.howto.use.externalprojects;

import org.mpxj.ProjectFile;
import org.mpxj.reader.UniversalProjectReader;

public class ResourcePool
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      String path = file.getProjectProperties().getResourcePoolFile();
   }
}
