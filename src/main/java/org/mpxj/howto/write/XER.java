package org.mpxj.howto.write;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.writer.FileFormat;
import net.sf.mpxj.writer.UniversalProjectWriter;

public class XER
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      new UniversalProjectWriter(FileFormat.XER).write(project, fileName);
   }
}
