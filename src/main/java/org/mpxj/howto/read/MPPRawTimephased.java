package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;

public class MPPRawTimephased
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setUseRawTimephasedData(true);
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
