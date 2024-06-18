package org.mpxj.howto.convert;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.reader.UniversalProjectReader;
import net.sf.mpxj.writer.FileFormat;
import net.sf.mpxj.writer.UniversalProjectWriter;

public class ConvertUniversal
{
   public void convert(String inputFile, FileFormat format, String outputFile) throws Exception
   {
      ProjectFile projectFile = new UniversalProjectReader().read(inputFile);
      new UniversalProjectWriter(format).write(projectFile, outputFile);
   }
}