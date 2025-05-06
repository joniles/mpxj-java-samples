package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.mspdi.MSPDIReader;

public class MSPDICompatibleInput
{
   public void read() throws Exception
   {
      MSPDIReader reader = new MSPDIReader();
      reader.setMicrosoftProjectCompatibleInput(false);
      ProjectFile project = reader.read("my-sample.xml");
   }
}
