package org.mpxj.howto.use.universal;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.phoenix.PhoenixReader;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.reader.UniversalProjectReader;
import net.sf.mpxj.sdef.SDEFReader;

import java.io.File;

public class ProxyExample
{
   public void process(File file) throws Exception
   {
      UniversalProjectReader upr = new UniversalProjectReader();

      // Retrieve the proxy
      try (UniversalProjectReader.ProjectReaderProxy proxy
              = upr.getProjectReaderProxy(file))
      {
         // Retrieve the reader class
         ProjectReader reader = proxy.getProjectReader();

         // Determine if we want to continue processing this file type.
         // In this example we are ignoring SDEF files.
         if (reader instanceof SDEFReader)
         {
            return;
         }

         // Provide configuration for specific reader types.
         // In this example we are changing the behavior of the Phoenix reader.
         if (reader instanceof PhoenixReader)
         {
            ((PhoenixReader)reader).setUseActivityCodesForTaskHierarchy(false);
         }

         // Finally, we read the schedule
         ProjectFile project = proxy.read();

         // Now we can work with the schedule data...
      }
   }
}
