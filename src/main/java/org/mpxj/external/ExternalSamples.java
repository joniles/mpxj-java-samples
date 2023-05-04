package org.mpxj.external;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

public class ExternalSamples {
    public static void main(String[] argv) throws Exception {
        ExternalSamples samples = new ExternalSamples();
        samples.subprojects();
    }

    private void subprojects() throws Exception
    {
ProjectFile file = new UniversalProjectReader().read("sample.mpp");
for (Task task : file.getTasks())
{
    if (task.getExternalProject())
    {
        System.out.println(task.getName() + " is a subproject");
        System.out.println("The path to the file is: " + task.getSubprojectFile());
        System.out.println("The GUID of this project is: " + task.getSubprojectGUID());
        System.out.println("The offset used when displaying Unique ID values is: " + task.getSubprojectTasksUniqueIDOffset());
    }
}
    }
}
