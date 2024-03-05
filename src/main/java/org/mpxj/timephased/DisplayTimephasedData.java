package org.mpxj.timephased;

import net.sf.mpxj.Duration;
import net.sf.mpxj.LocalDateTimeRange;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.mpp.TimescaleUnits;
import net.sf.mpxj.reader.UniversalProjectReader;
import net.sf.mpxj.utility.TimephasedUtility;
import net.sf.mpxj.utility.TimescaleUtility;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DisplayTimephasedData
{
   public static void main(String[] argv) throws Exception {

      if (argv.length != 1)
      {
         System.out.println("Usage: DisplayTimephasedData <file name>");
         return;
      }

      // Set the start of our timescale
      LocalDateTime startDate = LocalDateTime.of(2023, 6, 1, 0, 0);

      new DisplayTimephasedData().process(argv[0], startDate, TimescaleUnits.MONTHS, 7);
   }

   public void process(String fileName, LocalDateTime startDate, TimescaleUnits units, int count) throws Exception
   {
      ProjectFile file = new UniversalProjectReader().read(fileName);

      // Create the timescale
      // This is a list of LocalDateTimeRange instances
      // representing the start and end of each period on the timescale
      ArrayList<LocalDateTimeRange> timescale = new TimescaleUtility().createTimescale(startDate, units, count);

      TimephasedUtility timephasedUtility = new TimephasedUtility();

      for (ResourceAssignment assignment : file.getResourceAssignments())
      {
         ArrayList<Duration> durationList = timephasedUtility.segmentWork(assignment.getEffectiveCalendar(), assignment.getTimephasedWork(), units, timescale);
         System.out.println(assignment);
         for (int index=0; index < timescale.size(); index++)
         {
            System.out.println(timescale.get(index) + "\t" + durationList.get(index));
         }
      }
   }
}
