package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraPMFileWriter;

public class PMXMLBaselines
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      PrimaveraPMFileWriter writer = new PrimaveraPMFileWriter();
      writer.setWriteBaselines(true);
      writer.write(project, fileName);
   }
}
