package org.mpxj.howto.read;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mpxj.MPXJException;
import org.mpxj.ProjectFile;
import org.mpxj.opc.OpcExportType;
import org.mpxj.opc.OpcProject;
import org.mpxj.opc.OpcProjectBaseline;
import org.mpxj.opc.OpcReader;

public class OPC
{
   public void read() throws MPXJException
   {
      OpcReader reader = new OpcReader("my-opc-host.oraclecloud.com", "my-username", "my-password");
      List<OpcProject> opcProjects = reader.getProjects();

      // In this example, we'll just use the first project returned
      OpcProject opcProject = opcProjects.get(0);
      ProjectFile mpxjProject = reader.readProject(opcProject);
   }

   public void createOpcProjectAndRead() throws MPXJException
   {
      OpcProject opcProject = new OpcProject();
      opcProject.setWorkspaceId(123);
      opcProject.setProjectId(456);

      OpcReader reader = new OpcReader("my-opc-host.oraclecloud.com", "my-username", "my-password");
      ProjectFile mpxjProject = reader.readProject(opcProject);
   }

   public void exportProject() throws IOException
   {
      OpcReader reader = new OpcReader("my-opc-host.oraclecloud.com", "my-username", "my-password");
      List<OpcProject> opcProjects = reader.getProjects();

      // In this example, we'll just use the first project returned
      OpcProject opcProject = opcProjects.get(0);
      reader.exportProject(opcProject, "export-file.xer", OpcExportType.XER, false);

      reader.exportProject(opcProject, "export-file.xml", OpcExportType.XML, false);

      reader.exportProject(opcProject, "export-file.zip", OpcExportType.XML, true);


      // Output destination specified by a File instance
      File file = new File("export-file.xer");
      reader.exportProject(opcProject, file, OpcExportType.XER, false);

      // Output sent to an OutputStream
      try (OutputStream out = Files.newOutputStream(Paths.get("export-file.xer")))
      {
         reader.exportProject(opcProject, out, OpcExportType.XER, false);
      }
   }

   public void baselines1(OpcReader reader, OpcProject opcProject) throws MPXJException
   {
      List<OpcProjectBaseline> baselines = reader.getProjectBaselines(opcProject);

      // We're assuming that the project has more than one baseline.
      // We'll just request data from the first baseline.
      List<OpcProjectBaseline> requiredBaselines = Collections.singletonList(baselines.get(0));
      ProjectFile mpxjProject = reader.readProject(opcProject, requiredBaselines);

   }

   public void baseliness(OpcReader reader, OpcProject opcProject) throws MPXJException
   {
      List<OpcProjectBaseline> requiredBaselines = new ArrayList<>();

      OpcProjectBaseline baseline1 = new OpcProjectBaseline();
      baseline1.setProjectBaselineId(789);
      requiredBaselines.add(baseline1);

      OpcProjectBaseline baseline2 = new OpcProjectBaseline();
      baseline2.setProjectBaselineId(790);
      requiredBaselines.add(baseline2);

      ProjectFile mpxjProject = reader.readProject(opcProject, requiredBaselines);

   }


}
