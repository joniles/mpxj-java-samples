package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.sage.SageReader;

public class ScheduleGridIgnoreErrors
{
   public void read() throws Exception
   {
      SageReader reader = new SageReader();
      reader.setIgnoreErrors(false);
      ProjectFile project = reader.read("my-sample.schedule_grid");
   }
}
