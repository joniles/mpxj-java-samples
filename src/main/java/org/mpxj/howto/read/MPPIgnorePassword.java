package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;

public class MPPIgnorePassword
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setRespectPasswordProtection(false);
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
