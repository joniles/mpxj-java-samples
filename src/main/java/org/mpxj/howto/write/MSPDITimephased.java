package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.mspdi.MSPDIWriter;

public class MSPDITimephased
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      MSPDIWriter writer = new MSPDIWriter();
      writer.setWriteTimephasedData(true);
      writer.write(project, fileName);
   }
}
