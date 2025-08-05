package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.reader.UniversalProjectReader;

public class P3Directory
{
   public void read() throws Exception
   {
      UniversalProjectReader reader = new UniversalProjectReader();
      ProjectFile project = reader.read("my-p3-directory");
   }
}
