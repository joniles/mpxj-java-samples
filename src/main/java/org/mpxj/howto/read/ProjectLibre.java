package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.reader.UniversalProjectReader;

public class ProjectLibre
{
   public void read() throws Exception
   {
      UniversalProjectReader reader = new UniversalProjectReader();
      ProjectFile project = reader.read("my-sample.pod");
   }
}
