package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.mpp.MPPReader;

public class MPPPresentationData
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setReadPresentationData(false);
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
