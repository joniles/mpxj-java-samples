package org.mpxj.howto.read;

import net.sf.mpxj.mpd.MPDDatabaseReader;
import java.sql.Connection;
import java.util.Map;

public class MPDUsingReader
{
   public void read(Connection connection) throws Exception
   {
      MPDDatabaseReader reader = new MPDDatabaseReader();
      reader.setConnection(connection);
      Map<Integer, String> projects = reader.listProjects();
      for (Map.Entry<Integer, String> entry : projects.entrySet())
      {
         System.out.println("Project name: " + entry.getValue());
         reader.setProjectID(entry.getKey());
         reader.read();
      }
   }
}
