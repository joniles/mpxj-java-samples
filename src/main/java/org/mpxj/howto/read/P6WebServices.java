package org.mpxj.howto.read;

import org.mpxj.MPXJException;
import org.mpxj.ProjectFile;
import org.mpxj.primavera.webservices.WebServicesExportType;
import org.mpxj.primavera.webservices.WebServicesProject;
import org.mpxj.primavera.webservices.WebServicesReader;

import java.util.List;

public class P6WebServices
{
   public static void main(String[] argv) throws Exception
   {
      new P6WebServices().read();
   }

   public void read() throws MPXJException
   {
      WebServicesReader reader = new WebServicesReader(
         "https://my-p6-host/p6ws",
         "my-database-name",
         "my-user-name",
         "my-password");
      List<WebServicesProject> wsProjects = reader.getProjects();

      // In this example, we'll just use the first project returned
      WebServicesProject wsProject = wsProjects.get(0);
      ProjectFile mpxjProject = reader.readProject(wsProject);
   }

   public void createWebServicesProjectAndRead() throws MPXJException
   {
      WebServicesProject wsProject = new WebServicesProject();
      wsProject.setObjectId(123);

      WebServicesReader reader = new WebServicesReader(
         "https://my-p6-host/p6ws",
         "my-database-name",
         "my-user-name",
         "my-password");

      ProjectFile mpxjProject = reader.readProject(wsProject);
   }

   public void readWithoutBaseline() throws MPXJException
   {
      WebServicesReader reader = new WebServicesReader(
         "https://my-p6-host/p6ws",
         "my-database-name",
         "my-user-name",
         "my-password");
      List<WebServicesProject> wsProjects = reader.getProjects();

      // In this example, we'll just use the first project returned
      WebServicesProject wsProject = wsProjects.get(0);
      ProjectFile mpxjProject = reader.readProject(wsProject, false);
   }


   public void export() throws Exception
   {
      WebServicesReader reader = new WebServicesReader(
         "https://my-p6-host/p6ws",
         "my-database-name",
         "my-user-name",
         "my-password");
      List<WebServicesProject> wsProjects = reader.getProjects();

      // In this example, we'll just use the first project returned
      WebServicesProject wsProject = wsProjects.get(0);
      reader.exportProject(wsProject, "export.xml", WebServicesExportType.XML, true, false);
   }

}
