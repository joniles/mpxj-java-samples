package org.mpxj.howto.convert;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.writer.ProjectWriter;

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
