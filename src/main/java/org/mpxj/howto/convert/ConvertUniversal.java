package org.mpxj.howto.convert;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.reader.UniversalProjectReader;
import net.sf.mpxj.writer.ProjectWriter;
import net.sf.mpxj.writer.ProjectWriterUtility;

public class ConvertUniversal
{
   public void convert(String inputFile, String outputFile) throws Exception
   {
      UniversalProjectReader reader = new UniversalProjectReader();
      ProjectFile projectFile = reader.read(inputFile);

      ProjectWriter writer = ProjectWriterUtility.getProjectWriter(outputFile);
      writer.write(projectFile, outputFile);
   }
}
