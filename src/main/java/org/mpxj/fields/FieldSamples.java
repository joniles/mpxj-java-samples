package org.mpxj.fields;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.TaskField;
import org.mpxj.calendars.CalendarSamples;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FieldSamples {

    public static void main(String[] argv) throws Exception {
        FieldSamples samples = new FieldSamples();
        samples.dateTest();
        //samples.basicOperations();
    }

    private void dateTest() throws Exception
    {
        SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new java.util.Date(119, 11, 31, 1, 0);
        System.out.println(date.toString());
        System.out.println(dateFormat.format(date));
        System.out.println();

        Calendar cal = Calendar.getInstance();
        cal.set(2019, 11, 31, 1, 0, 0);
        date = cal.getTime();
        System.out.println(date.toString());
        System.out.println(dateFormat.format(date));
        System.out.println();

        date = dateFormat.parse("2019-12-31 01:00:00");
        System.out.println(date.toString());
        System.out.println(dateFormat.format(date));
    }

    private void basicOperations() throws Exception
    {
        // Set up the sample project
        ProjectFile file = new ProjectFile();
        Task task = file.addTask();

        // Set and retrieve the name
        task.setName("Task 1");

        String name = task.getName();
        System.out.println("Task name:" + name);

        // Set the start date
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = df.parse("10/05/2022");
        task.setStart(startDate);

        System.out.println("Start date: " + df.format(task.getStart()));

        task = file.addTask();
        task.set(TaskField.NAME, "Task 2");

        name = (String)task.getCurrentValue(TaskField.NAME);
        System.out.println("Task name:" + name);

        startDate = df.parse("11/05/2022");
        task.set(TaskField.START, startDate);

        System.out.println("Start date: " + df.format(task.getStart()));

    }
}
