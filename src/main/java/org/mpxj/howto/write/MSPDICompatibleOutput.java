package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.mspdi.MSPDIWriter;

public class MSPDICompatibleOutput
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      MSPDIWriter writer = new MSPDIWriter();
      writer.setMicrosoftProjectCompatibleOutput(false);
      writer.write(project, fileName);
   }
}
