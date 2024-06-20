package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraDatabaseFileReader;

import java.io.File;
import java.util.Map;

public class P6Sqlite
{
   public void read() throws Exception
   {
      PrimaveraDatabaseFileReader reader = new PrimaveraDatabaseFileReader();

      //
      // Retrieve a list of the projects available in the database
      //
      File file = new File("PPMDBSQLite.db");
      Map<Integer,String> projects = reader.listProjects(file);

      //
      // At this point you'll select the project
      // you want to work with.
      //

      //
      // Now open the selected project using its ID
      //
      int selectedProjectID = 1;
      reader.setProjectID(selectedProjectID);
      ProjectFile projectFile = reader.read("PPMDBSQLite.db");
   }
}
