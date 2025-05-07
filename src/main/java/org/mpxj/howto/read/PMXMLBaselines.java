package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraPMFileReader;
import org.mpxj.primavera.PrimaveraBaselineStrategy;

import java.util.List;

public class PMXMLBaselines
{
   public void read() throws Exception
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setBaselineStrategy(PrimaveraBaselineStrategy.CURRENT_ATTRIBUTES);
      List<ProjectFile> files = reader.readAll("my-sample.xml");
   }
}
