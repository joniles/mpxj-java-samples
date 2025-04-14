package org.mpxj.howto.convert;

import org.mpxj.ProjectFile;
import org.mpxj.mpp.MPPReader;
import org.mpxj.mpx.MPXWriter;
import org.mpxj.reader.ProjectReader;
import org.mpxj.writer.ProjectWriter;

public class ConvertMppToMpx
{
   public void convert(String inputFile, String outputFile) throws Exception
   {
      ProjectReader reader = new MPPReader();
      ProjectFile projectFile = reader.read(inputFile);

      ProjectWriter writer = new MPXWriter();
      writer.write(projectFile, outputFile);
   }
}
