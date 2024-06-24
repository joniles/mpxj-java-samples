package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpd.MPDFileReader;

import java.io.File;
import java.util.Map;

public class MPDFileWithReader
{
   public void read() throws Exception
   {
      File file = new File("my-sample.mpd");
      MPDFileReader reader = new MPDFileReader();

      // Retrieve the project details
      Map<Integer, String> projects = reader.listProjects(file);

      // Look up the project we want to read from the map.
      // For this example we'll just use a hard-coded value.
      Integer projectID = Integer.valueOf(1);

      // Set the ID f the project we want to read
      reader.setProjectID(projectID);

      // Read the project
      ProjectFile project = reader.read(file);
   }
}
