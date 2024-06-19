package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;

public class MPPWithPassword
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setReadPassword("my secret password");
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
