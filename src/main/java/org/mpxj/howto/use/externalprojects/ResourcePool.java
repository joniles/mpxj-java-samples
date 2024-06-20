package org.mpxj.howto.use.externalprojects;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.reader.UniversalProjectReader;

public class ResourcePool
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      String path = file.getProjectProperties().getResourcePoolFile();
   }
}
