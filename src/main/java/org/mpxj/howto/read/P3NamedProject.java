package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.p3.P3DatabaseReader;

import java.util.List;

public class P3NamedProject
{
   public void read() throws Exception
   {
      // Find a list of the project names
      String directory = "my-p3-directory";
      List<String> projectNames = P3DatabaseReader.listProjectNames(directory);

      // Tell the reader which project to work with
      P3DatabaseReader reader = new P3DatabaseReader();
      reader.setProjectName(projectNames.get(0));

      // Read the project
      ProjectFile project = reader.read(directory);
   }
}
