package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.mpx.MPXReader;

public class MPXIgnoreTextModels
{
   public void read() throws Exception
   {
      MPXReader reader = new MPXReader();
      reader.setIgnoreTextModels(false);
      ProjectFile project = reader.read("my-sample.mpx");
   }
}
