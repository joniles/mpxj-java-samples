package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.MSPDIReader;

public class MSPDICompatibleInput
{
   public void read() throws Exception
   {
      MSPDIReader reader = new MSPDIReader();
      reader.setMicrosoftProjectCompatibleInput(false);
      ProjectFile project = reader.read("my-sample.xml");
   }
}
