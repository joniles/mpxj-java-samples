package org.mpxj.howto.use.fields;

import net.sf.mpxj.*;
import net.sf.mpxj.reader.UniversalProjectReader;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class FieldSamples {

    public static void main(String[] argv)  {
        FieldSamples samples = new FieldSamples();
        samples.basicOperations();
        samples.startVariance();
        samples.getCachedValue();
        samples.fieldType();
        //samples.customFields();
    }

    private void basicOperations()
    {
        System.out.println("Basic Operations");

        // EXAMPLE: Set up the sample project
        ProjectFile file = new ProjectFile();
        Task task = file.addTask();

        // EXAMPLE: Set and retrieve the name
        task.setName("Task 1");

        String name = task.getName();
        System.out.println("Task name: " + name);

        // EXAMPLE: Set the start date
        LocalDateTime startDate = LocalDateTime.of(2022, 5, 10, 8, 0);
        task.setStart(startDate);

        System.out.println("Start date: " + task.getStart());

        // EXAMPLE: field enumerations
        task = file.addTask();
        task.set(TaskField.NAME, "Task 2");

        name = (String)task.get(TaskField.NAME);
        System.out.println("Task name: " + name);

        startDate = LocalDateTime.of(2022, 5, 11, 8, 0);
        task.set(TaskField.START, startDate);

        System.out.println("Start date: " + task.getStart());
        System.out.println();
    }

    private void startVariance()
    {
        System.out.println("Start Variance");

        // EXAMPLE: Set up the sample project
        ProjectFile file = new ProjectFile();

        // We need at least a default calendar to calculate variance
        file.setDefaultCalendar(file.addDefaultBaseCalendar());

        // Create tasks
        Task task1 = file.addTask();
        Task task2 = file.addTask();

        // Set up example dates
        LocalDateTime baselineStart = LocalDateTime.of(2022, 5, 1, 8, 0);
        LocalDateTime startDate = LocalDateTime.of(2022,5, 10, 8, 0);

        // Update task1 using methods
        task1.setStart(startDate);
        task1.setBaselineStart(baselineStart);

        // Update task2 using TaskField enumeration
        task2.set(TaskField.START, startDate);
        task2.set(TaskField.BASELINE_START, baselineStart);

        // Show the variance being retrieved by method and TaskField enumeration
        System.out.println("Task 1");
        System.out.println("Start Variance from method: "
           + task1.getStartVariance());
        System.out.println("Start Variance from get: "
           + task1.get(TaskField.START_VARIANCE));
        System.out.println();

        System.out.println("Task 2");
        System.out.println("Start Variance from method: "
           + task2.getStartVariance());
        System.out.println("Start Variance from get: "
           + task2.get(TaskField.START_VARIANCE));

        System.out.println();
    }

    private void getCachedValue()
    {
        System.out.println("Get Cached Value");

        // EXAMPLE: Set up the sample project with a default calendar
        ProjectFile file = new ProjectFile();
        file.setDefaultCalendar(file.addDefaultBaseCalendar());

        // Set up example dates
        LocalDateTime baselineStart = LocalDateTime.of(2022, 5, 1, 8, 0);
        LocalDateTime startDate = LocalDateTime.of(2022,5, 10, 8, 0);

        // Create a task
        Task task = file.addTask();
        task.setStart(startDate);
        task.setBaselineStart(baselineStart);

        System.out.println("Start Variance using getCachedValue(): "
           + task.getCachedValue(TaskField.START_VARIANCE));
        System.out.println("Start Variance using get(): "
           + task.get(TaskField.START_VARIANCE));
        System.out.println("Start Variance using getCachedValue(): "
           + task.getCachedValue(TaskField.START_VARIANCE));

        System.out.println();
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

        Set<FieldType> allFields = file.getPopulatedFields();
    }

    private void fieldType()
    {
        System.out.println("Field Type");

        FieldType type = TaskField.START_VARIANCE;
        System.out.println("name(): " + type.name());
        System.out.println("getName(): " + type.getName());
        System.out.println("getFieldTypeClass(): " + type.getFieldTypeClass());
        System.out.println("getDataType(): " + type.getDataType());

        System.out.println();
    }


    private String getValueAsText(FieldContainer container, FieldType type)
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
                result = new DecimalFormat("£0.00").format(value);
                break;
            }

            case DATE:
            {
                result = DateTimeFormatter.ofPattern("dd/MM/yyyy").format((LocalDateTime)value);
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
        FieldType fieldType = container.getFieldTypeByAlias(
           FieldTypeClass.TASK,
           "Number of Widgets Required");

        // Use the type we've retrieved to retrieve the field value from a task
        Task task = file.getTaskByID(Integer.valueOf(1));
        Object value = task.get(fieldType);

        fieldType = file.getTasks().getFieldTypeByAlias("Number of Widgets Required");

        value = task.getFieldByAlias("Number of Widgets Required");
    }

    public void userDefinedFields() throws Exception
    {
        ProjectFile project = new UniversalProjectReader().read("example.mpp");
        
        for (UserDefinedField field : project.getUserDefinedFields())
        {
            System.out.println("name(): " + field.name());
            System.out.println("getName(): " + field.getName());
            System.out.println("getFieldTypeClass(): " + field.getFieldTypeClass());
            System.out.println("getDataType():" + field.getDataType());
        }
    }
}
