package org.mpxj.howto.use.baselines;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectProperties;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

import java.time.LocalDateTime;

public class MicrosoftProjectBaselines
{
   public void readBaselineAttributes() throws MPXJException
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

}
