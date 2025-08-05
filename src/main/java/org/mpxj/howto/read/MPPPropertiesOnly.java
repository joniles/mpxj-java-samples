package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.mpp.MPPReader;

public class MPPPropertiesOnly
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setReadPropertiesOnly(true);
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
