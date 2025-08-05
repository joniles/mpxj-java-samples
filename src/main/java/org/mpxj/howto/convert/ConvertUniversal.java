package org.mpxj.howto.convert;

import org.mpxj.ProjectFile;
import org.mpxj.reader.UniversalProjectReader;
import org.mpxj.writer.FileFormat;
import org.mpxj.writer.UniversalProjectWriter;

public class ConvertUniversal
{
   public void convert(String inputFile, FileFormat format, String outputFile) throws Exception
   {
      ProjectFile projectFile = new UniversalProjectReader().read(inputFile);
      new UniversalProjectWriter(format).write(projectFile, outputFile);
   }
}