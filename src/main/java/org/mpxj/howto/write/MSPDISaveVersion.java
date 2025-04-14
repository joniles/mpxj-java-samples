package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.mspdi.MSPDIWriter;
import org.mpxj.mspdi.SaveVersion;

public class MSPDISaveVersion
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      MSPDIWriter writer = new MSPDIWriter();
      writer.setSaveVersion(SaveVersion.Project2002);
      writer.write(project, fileName);
   }
}
