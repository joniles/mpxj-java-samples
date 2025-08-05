package org.mpxj.assignments;

import org.mpxj.ProjectFile;
import org.mpxj.Resource;
import org.mpxj.ResourceAssignment;
import org.mpxj.reader.UniversalProjectReader;

import java.util.List;

public class AssignmentSamples {

   public static void main(String[] args) throws Exception
   {
      new AssignmentSamples().listAssignments("sample.mpp");
   }

   public void listAssignments(String fileName) throws Exception
   {
      UniversalProjectReader reader = new UniversalProjectReader();
      ProjectFile file = reader.read(fileName);
      for (Resource resource : file.getResources())
      {
         testResource(resource);
      }
   }

   private static void testResource(Resource resource)
   {
      List<ResourceAssignment> assignments = resource.getTaskAssignments();
      for (ResourceAssignment assignment : assignments)
      {
         System.out.println(assignment.getTask().getName() + " [" + assignment.getUnits() + "%]");
      }
   }
}
