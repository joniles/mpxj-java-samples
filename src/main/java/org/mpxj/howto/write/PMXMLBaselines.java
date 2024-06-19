package org.mpxj.howto.write;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraPMFileWriter;

public class PMXMLBaselines
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      PrimaveraPMFileWriter writer = new PrimaveraPMFileWriter();
      writer.setWriteBaselines(true);
      writer.write(project, fileName);
   }
}
