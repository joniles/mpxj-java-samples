package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;

public class MPPPropertiesOnly
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setReadPropertiesOnly(true);
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
