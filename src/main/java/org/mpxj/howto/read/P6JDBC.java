package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraDatabaseReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class P6JDBC
{
   public void read() throws Exception
   {
      //
      // Load the JDBC driver
      //
      String driverClass="com.microsoft.sqlserver.jdbc.SQLServerDriver";
      Class.forName(driverClass);

      //
      // Open a database connection. You will need to change
      // these details to match the name of your server, database, user and password.
      //
      String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=my-database-name;user=my-user-name;password=my-password;";
      Connection c = DriverManager.getConnection(connectionString);
      PrimaveraDatabaseReader reader = new PrimaveraDatabaseReader();
      reader.setConnection(c);

      //
      // Retrieve a list of the projects available in the database
      //
      Map<Integer,String> projects = reader.listProjects();

      //
      // At this point you'll select the project
      // you want to work with.
      //

      //
      // Now open the selected project using its ID
      //
      int selectedProjectID = 1;
      reader.setProjectID(selectedProjectID);
      ProjectFile projectFile = reader.read();
   }
}
