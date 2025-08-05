package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.mpp.MPPReader;

public class MPPWithPassword
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setReadPassword("my secret password");
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
