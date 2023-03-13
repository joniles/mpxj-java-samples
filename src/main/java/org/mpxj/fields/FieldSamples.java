package org.mpxj.fields;

import net.sf.mpxj.*;
import net.sf.mpxj.reader.UniversalProjectReader;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class FieldSamples {

    public static void main(String[] argv) throws Exception {
        FieldSamples samples = new FieldSamples();
        //samples.basicOperations();
        //samples.startVariance();
        //samples.fieldType();
        //samples.getCachedValue();
        samples.customFields();
    }

    private void basicOperations() throws Exception
    {
        // Set up the sample project
        ProjectFile file = new ProjectFile();
        Task task = file.addTask();

        // Set and retrieve the name
        task.setName("Task 1");

        String name = task.getName();
        System.out.println("Task name: " + name);

        // Set the start date
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = df.parse("10/05/2022");
        task.setStart(startDate);

        System.out.println("Start date: " + df.format(task.getStart()));

        task = file.addTask();
        task.set(TaskField.NAME, "Task 2");

        name = (String)task.get(TaskField.NAME);
        System.out.println("Task name: " + name);

        startDate = df.parse("11/05/2022");
        task.set(TaskField.START, startDate);

        System.out.println("Start date: " + df.format(task.getStart()));
    }

    private void startVariance() throws Exception
    {
        // Set up the sample project
        ProjectFile file = new ProjectFile();

        // We need at least a default calendar to calculate variance
        file.setDefaultCalendar(file.addDefaultBaseCalendar());

        // Create tasks
        Task task1 = file.addTask();
        Task task2 = file.addTask();

        // Set up example dates
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date baselineStart = df.parse("01/05/2022");
        Date startDate = df.parse("10/05/2022");

        // Update task1 using methods
        task1.setStart(startDate);
        task1.setBaselineStart(baselineStart);

        // Update task2 using TaskField enumeration
        task2.set(TaskField.START, startDate);
        task2.set(TaskField.BASELINE_START, baselineStart);

        // Show the variance being retrieved by method and TaskField enumeration
        System.out.println("Task 1");
        System.out.println("Start Variance from method: " + task1.getStartVariance());
        System.out.println("Start Variance from get: " + task1.get(TaskField.START_VARIANCE));
        System.out.println();

        System.out.println("Task 2");
        System.out.println("Start Variance from method: " + task2.getStartVariance());
        System.out.println("Start Variance from get: " + task2.get(TaskField.START_VARIANCE));
        System.out.println();
    }

    private void getCachedValue() throws Exception
    {
        // Set up the sample project with a default calendar
        ProjectFile file = new ProjectFile();
        file.setDefaultCalendar(file.addDefaultBaseCalendar());

        // Set up example dates
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date baselineStart = df.parse("01/05/2022");
        Date startDate = df.parse("10/05/2022");

        // Create a task
        Task task = file.addTask();
        task.setStart(startDate);
        task.setBaselineStart(baselineStart);

        System.out.println("Start Variance using getCachedValue(): " + task.getCachedValue(TaskField.START_VARIANCE));
        System.out.println("Start Variance using get(): " + task.get(TaskField.START_VARIANCE));
        System.out.println("Start Variance using getCachedValue(): " + task.getCachedValue(TaskField.START_VARIANCE));
    }

    private void indexedFields()
    {
        ProjectFile file = new ProjectFile();
        Task task = file.addTask();

        task.setText(1, "This is Text 1");
        String text1 = task.getText(1);
        System.out.println("Text 1 is: " + text1);

        task.set(TaskField.TEXT1, "This is also Text 1");
        text1 = (String)task.get(TaskField.TEXT1);
        System.out.println("Text 1 is: " + text1);
    }

    private void populatedFields() throws Exception
    {
        ProjectFile file = new UniversalProjectReader().read("example.mpp");
        Set<FieldType> populatedProjectFields = file.getProjectProperties().getPopulatedFields();
        Set<FieldType> populatedTaskFields = file.getTasks().getPopulatedFields();
        Set<FieldType> populatedResourceFields = file.getResources().getPopulatedFields();
        Set<FieldType> populatedAssignmentFields = file.getResourceAssignments().getPopulatedFields();
    }

    private void fieldType()
    {
        FieldType type = TaskField.START_VARIANCE;
        System.out.println("name(): " + type.name());
        System.out.println("getName(): " + type.getName());
        System.out.println("getFieldTypeClass(): " + type.getFieldTypeClass());
        System.out.println("getDataType(): " + type.getDataType());
    }


    private String getStringValue(FieldContainer container, FieldType type)
    {
        Object value = container.get(type);
        if (value == null)
        {
            return "";
        }

        String result;
        switch (type.getDataType())
        {
            case CURRENCY:
            {
                result = new DecimalFormat("£0.00").format((Number)value);
                break;
            }

            case DATE:
            {
                result = new SimpleDateFormat("dd/MM/yyyy").format((Date)value);
                break;
            }

            case BOOLEAN:
            {
                result = ((Boolean)value).booleanValue() ? "Yes" : "No";
                break;
            }

            default:
            {
                result = value.toString();
                break;
            }
        }

        return result;
    }

    private void customFields() throws Exception
    {
        ProjectFile file = new UniversalProjectReader().read("example.mpp");
        // Let's see what has been configured in our sample file
        CustomFieldContainer container = file.getCustomFields();
        for (CustomField field : container)
        {
            FieldType type = field.getFieldType();
            String typeClass = type.getFieldTypeClass().toString();
            String typeName = type.name();
            String alias = field.getAlias();
            System.out.println(typeClass + "." + typeName + "\t" + alias);
        }

        // Retrieve the CustomField entry for Text 1, if it has one
        CustomField fieldConfiguration = container.get(TaskField.TEXT1);

        // Retrieve the field type for a task field with a particular alias
        FieldType fieldType = container.getFieldTypeByAlias(FieldTypeClass.TASK, "Number of Widgets Required");

        // Use the type we've retrieved to retrieve the field value from a task
        Task task = file.getTaskByID(Integer.valueOf(1));
        Object value = task.get(fieldType);

        fieldType = file.getTasks().getFieldTypeByAlias("Number of Widgets Required");

        value = task.getFieldByAlias("Number of Widgets Required");
    }
}
