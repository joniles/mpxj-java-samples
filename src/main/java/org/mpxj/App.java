package org.mpxj;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.reader.UniversalProjectReader;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception
    {
        System.out.println( "Hello World!" );
        UniversalProjectReader reader = new UniversalProjectReader();
        ProjectFile file = reader.read("/Users/joniles/Downloads/three-tasks.mpp");
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
