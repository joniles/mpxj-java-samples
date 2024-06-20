package org.mpxj.howto.use.baselines;

import net.sf.mpxj.BaselineStrategy;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraPMFileReader;
import net.sf.mpxj.reader.UniversalProjectReader;

public class CustomStrategy
{
   public class MyBaselineStrategy implements BaselineStrategy
   {
      @Override
      public void clearBaseline(ProjectFile project, int index) {

      }

      @Override
      public void populateBaseline(ProjectFile project, ProjectFile baseline, int index) {

      }
   }

   public void singleBaseline() throws MPXJException
   {
      BaselineStrategy myStrategy = new MyBaselineStrategy();
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setBaselineStrategy(myStrategy);
      ProjectFile file = reader.read("sample-pmxml.xml");
   }

   public void multipleBaselines() throws MPXJException
   {
      BaselineStrategy myStrategy = new MyBaselineStrategy();
      ProjectFile main = new UniversalProjectReader().read("main.pp");
      main.getProjectConfig().setBaselineStrategy(myStrategy);

      ProjectFile baseline1 = new UniversalProjectReader().read("baseline1.pp");
      ProjectFile baseline2 = new UniversalProjectReader().read("baseline2.pp");
      main.setBaseline(baseline1, 1);
      main.setBaseline(baseline2, 2);
   }
}
