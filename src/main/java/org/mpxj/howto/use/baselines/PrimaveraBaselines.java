package org.mpxj.howto.use.baselines;

import org.mpxj.MPXJException;
import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraBaselineStrategy;
import org.mpxj.primavera.PrimaveraPMFileReader;
import org.mpxj.reader.UniversalProjectReader;

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
