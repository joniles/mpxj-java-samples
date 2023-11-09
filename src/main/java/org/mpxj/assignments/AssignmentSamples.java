package org.mpxj.assignments;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.reader.UniversalProjectReader;

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
