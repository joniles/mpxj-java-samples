package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;

public class MPPPresentationData
{
   public void read() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setReadPresentationData(false);
      ProjectFile project = reader.read("my-sample.mpp");
   }
}
