package org.mpxj.howto.use.universal;

import org.mpxj.ProjectFile;
import org.mpxj.reader.ProjectReader;
import org.mpxj.reader.UniversalProjectReader;

public class SimpleExample
{
   public void process() throws Exception
   {
      ProjectReader reader = new UniversalProjectReader();
      ProjectFile project = reader.read("example.mpp");
   }
}
