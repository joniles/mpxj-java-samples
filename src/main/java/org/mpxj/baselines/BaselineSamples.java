package org.mpxj.baselines;

import net.sf.mpxj.*;
import net.sf.mpxj.primavera.PrimaveraBaselineStrategy;
import net.sf.mpxj.primavera.PrimaveraPMFileReader;
import net.sf.mpxj.reader.UniversalProjectReader;

import java.time.LocalDateTime;
import java.util.List;

public class BaselineSamples
{

   public void ReadBaselineAttributes() throws MPXJException
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      Task task = file.getTaskByID(Integer.valueOf(1));

      System.out.println("Start: " + task.getStart());
      System.out.println("Baseline Start: " + task.getBaselineStart());
      System.out.println("Baseline1 Start: " + task.getBaselineStart(1));
      System.out.println("Baseline2 Start: " + task.getBaselineStart(2));

      // ...

      System.out.println("Baseline10 Start: " + task.getBaselineStart(10));
   }

   public void readBaselineDates() throws MPXJException
   {
      ProjectFile file = new UniversalProjectReader().read("sample.mpp");
      ProjectProperties props = file.getProjectProperties();

      for (int baselineNumber=0; baselineNumber <= 10; baselineNumber++)
      {
         LocalDateTime baselineDate;
         String baselineLabel;

         if (baselineNumber == 0)
         {
            baselineDate = props.getBaselineDate();
            baselineLabel = "Baseline";
         }
         else
         {
            baselineDate = props.getBaselineDate(baselineNumber);
            baselineLabel = "Baseline " + baselineNumber;
         }

         if (baselineDate == null)
         {
            System.out.println(baselineLabel + " not set");
         }
         else
         {
            System.out.println(baselineLabel + " set on " + baselineDate);
         }
      }
   }

   public void listReadAllProjects() throws MPXJException
   {
      List<ProjectFile> projects = new UniversalProjectReader().readAll("sample-pmxml.xml");
      System.out.println("The file contains " + projects.size() + " projects");
   }

   public void linkedBaseline() throws MPXJException
   {
      ProjectFile project = new UniversalProjectReader().read("sample-pmxml.xml");
      ProjectFile baseline = project.getBaselines().get(0);

      System.out.println("Current project name: " + project.getProjectProperties().getName());
      System.out.println("Baseline project name: " + baseline.getProjectProperties().getName());
   }

   public void changeBaselineStrategy() throws MPXJException
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setBaselineStrategy(PrimaveraBaselineStrategy.CURRENT_DATES);
      ProjectFile file = reader.read("sample-pmxml.xml");
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
/*
   public void useCustomBaselineStrategy() throws MPXJException
   {
      BaselineStrategy myStrategy = new MyBaselineStratgey();
      ProjectFile main = new UniversalProjectReader().read("main.pp");
      main.getProjectConfig().setBaselineStrategy(myStrategy);

      ProjectFile baseline1 = new UniversalProjectReader().read("baseline1.pp");
      ProjectFile baseline2 = new UniversalProjectReader().read("baseline2.pp");
      main.setBaseline(baseline1, 1);
      main.setBaseline(baseline2, 2);
   }
 */
}
