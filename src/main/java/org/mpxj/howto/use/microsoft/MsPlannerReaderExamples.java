package org.mpxj.howto.use.microsoft;

import java.util.List;
import java.util.UUID;

import org.mpxj.ProjectFile;
import org.mpxj.msplanner.MsPlannerProject;
import org.mpxj.msplanner.MsPlannerReader;

public class MsPlannerReaderExamples
{
   public void read()
   {
      // The URL for your organisation's Dynamics server instance
      String dynamicsServerUrl = "https://example.api.crm11.dynamics.com";

      // We're assuming you have already authenticated as a user and have an access token
      String accessToken = "my-access-token-from-oauth";

      // Create a reader
      MsPlannerReader reader = new MsPlannerReader(dynamicsServerUrl, accessToken);

      // Retrieve the projects available and print their details
      List<MsPlannerProject> projects = reader.getProjects();
      for (MsPlannerProject project : projects)
      {
         System.out.println("ID: " + project.getProjectId() + " Name: " + project.getProjectName());
      }

      // Get the ID of the first project on the list
      UUID projectID =projects.get(0).getProjectId();

      // Now read the project
      ProjectFile project = reader.readProject(projectID);
   }
}
