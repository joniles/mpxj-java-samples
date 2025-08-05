package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.reader.UniversalProjectReader;

public class Asta
{
   public void read() throws Exception
   {
      UniversalProjectReader reader = new UniversalProjectReader();
      ProjectFile project = reader.read("my-sample.pp");
   }
}
