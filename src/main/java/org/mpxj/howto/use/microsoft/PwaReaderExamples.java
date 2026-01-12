package org.mpxj.howto.use.microsoft;

import org.mpxj.ProjectFile;
import org.mpxj.pwa.PwaProject;
import org.mpxj.pwa.PwaReader;

import java.util.List;
import java.util.UUID;

public class PwaReaderExamples
{
   public void read()
   {
      // The URL for your Project Server instance
      String projectServerUrl = "https://example.sharepoint.com/sites/pwa";

      // We're assuming you have already authenticated as a user and have an access token
      String accessToken = "my-access-token-from-oauth";

      // Create a reader
      PwaReader reader = new PwaReader(projectServerUrl, accessToken);

      // Retrieve the projects available and print their details
      List<PwaProject> projects = reader.getProjects();
      for (PwaProject project : projects)
      {
         System.out.println("ID: " + project.getProjectId() + " Name: " + project.getProjectName());
      }

      // Get the ID of the first project on the list
      UUID projectID =projects.get(0).getProjectId();

      // Now read the project
      ProjectFile project = reader.readProject(projectID);
   }
}
