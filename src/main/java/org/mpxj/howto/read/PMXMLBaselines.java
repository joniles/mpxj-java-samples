package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraPMFileReader;
import net.sf.mpxj.primavera.PrimaveraBaselineStrategy;

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
