package org.mpxj.howto.use.baselines;

import org.mpxj.MPXJException;
import org.mpxj.ProjectFile;
import org.mpxj.reader.UniversalProjectReader;

public class AstaBaselines
{
   public void linkedBaseline() throws MPXJException
   {
      ProjectFile project = new UniversalProjectReader().read("sample.pp");
      ProjectFile baseline = project.getBaseline();

      System.out.println("Current project name: " + project.getProjectProperties().getName());
      System.out.println("Baseline project name: " + baseline.getProjectProperties().getName());
   }

   public void manualAttachSingle() throws MPXJException
   {
      ProjectFile main = new UniversalProjectReader().read("main.pp");
      ProjectFile baseline = new UniversalProjectReader().read("baseline.pp");
      main.setBaseline(baseline);
   }

   public void manualAttachMultiple() throws MPXJException
   {
      ProjectFile main = new UniversalProjectReader().read("main.pp");
      ProjectFile baseline1 = new UniversalProjectReader().read("baseline1.pp");
      ProjectFile baseline2 = new UniversalProjectReader().read("baseline2.pp");
      main.setBaseline(baseline1, 1);
      main.setBaseline(baseline2, 2);
   }
}
