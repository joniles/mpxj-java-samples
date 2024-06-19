package org.mpxj.howto.use.universal;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.reader.UniversalProjectReader;

public class SimpleExample
{
   public void process() throws Exception
   {
      ProjectReader reader = new UniversalProjectReader();
      ProjectFile project = reader.read("example.mpp");
   }
}
