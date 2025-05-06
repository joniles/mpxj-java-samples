package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.writer.FileFormat;
import org.mpxj.writer.UniversalProjectWriter;

public class MPX
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      new UniversalProjectWriter(FileFormat.MPX).write(project, fileName);
   }
}
