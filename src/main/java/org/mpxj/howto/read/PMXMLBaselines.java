package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraPMFileReader;
import net.sf.mpxj.primavera.PrimaveraBaselineStrategy;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class PMXMLBaselines
{
   public void read() throws Exception
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setBaselineStrategy(PrimaveraBaselineStrategy.CURRENT_ATTRIBUTES);
      InputStream is = new FileInputStream("my-sample.xml");
      List<ProjectFile> files = reader.readAll(is);
   }
}
