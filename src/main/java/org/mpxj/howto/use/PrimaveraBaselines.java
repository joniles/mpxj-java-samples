package org.mpxj.howto.use;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraBaselineStrategy;
import net.sf.mpxj.primavera.PrimaveraPMFileReader;
import net.sf.mpxj.reader.UniversalProjectReader;

public class PrimaveraBaselines
{
   public void linkedBaseline() throws MPXJException
   {
      ProjectFile project = new UniversalProjectReader().read("sample-pmxml.xml");
      ProjectFile baseline = project.getBaseline();

      System.out.println("Current project name: " + project.getProjectProperties().getName());
      System.out.println("Baseline project name: " + baseline.getProjectProperties().getName());
   }

   public void changeBaselineStrategy() throws MPXJException
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setBaselineStrategy(PrimaveraBaselineStrategy.CURRENT_ATTRIBUTES);
      ProjectFile file = reader.read("sample-pmxml.xml");
   }
}
