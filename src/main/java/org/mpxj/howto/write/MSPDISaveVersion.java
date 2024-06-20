package org.mpxj.howto.write;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.mspdi.SaveVersion;

public class MSPDISaveVersion
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      MSPDIWriter writer = new MSPDIWriter();
      writer.setSaveVersion(SaveVersion.Project2002);
      writer.write(project, fileName);
   }
}
