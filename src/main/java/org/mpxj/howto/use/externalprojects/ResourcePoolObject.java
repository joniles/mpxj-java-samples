package org.mpxj.howto.use.externalprojects;

import org.mpxj.ProjectFile;
import org.mpxj.reader.UniversalProjectReader;

public class ResourcePoolObject
{
   public void process() throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      ProjectFile resourcePool = file.getProjectProperties().getResourcePoolObject();
   }
}
