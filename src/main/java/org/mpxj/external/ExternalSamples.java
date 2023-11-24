package org.mpxj.external;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.Relation;
import net.sf.mpxj.reader.UniversalProjectReader;

import java.io.File;
import java.util.List;

public class ExternalSamples {
    public static void main(String[] argv) throws Exception {
        ExternalSamples samples = new ExternalSamples();
        samples.expandSubprojectAndTraverse();
    }

    private void identifyExternalProjects() throws Exception
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

    private void identifyExternalTasks() throws Exception
    {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        for (Task task : file.getTasks())
        {
            if (task.getExternalTask())
            {
                System.out.println(task.getName() + " is an external predecessor");
                System.out.println("The path to the file containing this task is: " + task.getSubprojectFile());
                System.out.println("The ID of the task in this file is: " + task.getSubprojectTaskID());
                System.out.println("The Unique ID of the task in this file is: " + task.getSubprojectTaskUniqueID());
            }
        }
    }

    private void openingExternalProjects() throws Exception {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        Task externalProject = file.getTaskByID(Integer.valueOf(1));
        String filePath = externalProject.getSubprojectFile();
        ProjectFile externalProjectFile = new UniversalProjectReader().read(filePath);
    }

    private void openingExternalProjectsUsingMpxj() throws Exception {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
        ProjectFile externalProjectFile = externalProjectTask.getSubprojectObject();
    }

    private void openingExternalProjectsUsingMpxjWithWorkingDir() throws Exception {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        file.getProjectConfig().setSubprojectWorkingDirectory(new File("/path/to/directory"));
        Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
        ProjectFile externalProjectFile = externalProjectTask.getSubprojectObject();
    }

    private void expandExternalProjectTask() throws Exception {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
        System.out.println("Task has child tasks: " + externalProjectTask.hasChildTasks());
        externalProjectTask.expandSubproject();
        System.out.println("Task has child tasks: " + externalProjectTask.hasChildTasks());
    }

    private void expandAllExternalProjects() throws Exception {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        file.expandSubprojects(false);

        Task externalProjectTask = file.getTaskByID(Integer.valueOf(1));
        System.out.println("Task has child tasks: " + externalProjectTask.hasChildTasks());
        externalProjectTask.expandSubproject();
        System.out.println("Task has child tasks: " + externalProjectTask.hasChildTasks());
    }

    private void resourcePoolFile() throws Exception
    {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        String path = file.getProjectProperties().getResourcePoolFile();
    }

    private void resourcePoolObject() throws Exception
    {
        ProjectFile file = new UniversalProjectReader().read("sample.mpp");
        ProjectFile resourcePool = file.getProjectProperties().getResourcePoolObject();
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
