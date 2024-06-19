package org.mpxj.subprojects;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;

import java.io.File;
import java.util.List;

public class SubprojectTraverseExample {
    public static void main(String[] argv) throws Exception {
        SubprojectTraverseExample samples = new SubprojectTraverseExample();
        samples.expandSubprojectAndTraverse();
    }

    private void expandSubprojectAndTraverse() throws Exception
    {
        File workingDirectory = new File("/Users/joniles/Downloads/NewSubprojectTestCopy2x");
        ProjectFile file = new UniversalProjectReader().read(new File(workingDirectory, "MasterProject1.mpp"));
        file.getProjectConfig().setSubprojectWorkingDirectory(workingDirectory);
        file.expandSubprojects(true);
        printTasks("", file.getChildTasks());
        System.out.println();
        printPredecessors(file.getChildTasks());
    }

    private void printTasks(String prefix, List<Task> tasks)
    {
        for (Task task : tasks)
        {
            System.out.println(prefix + getTaskName(task));
            printTasks(prefix + " ", task.getChildTasks());
        }
    }

    private void printPredecessors(List<Task> tasks)
    {
        for (Task task : tasks)
        {
            task.getPredecessors().forEach(System.out::println);
            printPredecessors(task.getChildTasks());
        }
    }

    private String getTaskName(Task task)
    {
        String externalTaskLabel = task.getExternalTask() ? " [EXTERNAL TASK]" : "";
        String externalProjectLabel = task.getExternalProject() ? " [EXTERNAL PROJECT]" : "";
        return task.getName() + externalTaskLabel + externalProjectLabel;
    }
}
