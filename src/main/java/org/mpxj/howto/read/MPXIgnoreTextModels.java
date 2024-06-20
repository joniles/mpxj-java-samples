package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpx.MPXReader;

public class MPXIgnoreTextModels
{
   public void read() throws Exception
   {
      MPXReader reader = new MPXReader();
      reader.setIgnoreTextModels(false);
      ProjectFile project = reader.read("my-sample.mpx");
   }
}
