package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.writer.FileFormat;
import org.mpxj.writer.UniversalProjectWriter;

public class MSPDI
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      new UniversalProjectWriter(FileFormat.MSPDI).write(project, fileName);
   }
}
