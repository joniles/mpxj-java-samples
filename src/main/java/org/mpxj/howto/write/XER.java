package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.writer.FileFormat;
import org.mpxj.writer.UniversalProjectWriter;

import java.util.List;

public class XER
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      new UniversalProjectWriter(FileFormat.XER).write(project, fileName);
   }

   public void write(List<ProjectFile> projects, String fileName) throws Exception
   {
      new UniversalProjectWriter(FileFormat.XER).write(projects, fileName);
   }
}
