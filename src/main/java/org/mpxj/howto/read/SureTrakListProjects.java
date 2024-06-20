package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.suretrak.SureTrakDatabaseReader;

import java.util.List;

public class SureTrakListProjects
{
   public void read() throws Exception
   {
      // Find a list of the project names
      String directory = "my-suretrak-directory";
      List<String> projectNames = SureTrakDatabaseReader.listProjectNames(directory);

      // Tell the reader which project to work with
      SureTrakDatabaseReader reader = new SureTrakDatabaseReader();
      reader.setProjectName(projectNames.get(0));

      // Read the project
      ProjectFile project = reader.read(directory);
   }
}
