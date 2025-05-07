package org.mpxj.fields;

import org.mpxj.*;
import org.mpxj.common.FieldLists;
import org.mpxj.reader.UniversalProjectReader;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDefinedFields
{

   private void userDefinedFieldValues() throws Exception
   {
      // Read our schedule data
      ProjectFile file = new UniversalProjectReader().read("my.schedule.file");

      // Determine what UDF or custom fields we have.
      // First find all custom fields which have been configured.
      Set<FieldType> fields = file.getTasks().getCustomFields().stream().map(CustomField::getFieldType).filter(Objects::nonNull).collect(Collectors.toSet());

      // Add all user defined fields...
      fields.addAll(file.getUserDefinedFields().stream().filter(f -> f.getFieldTypeClass() == FieldTypeClass.TASK).collect(Collectors.toSet()));

      // Finally, add all custom fields with values.
      // This will pick up any custom field in use which don't have user configuration
      // (for example, in Microsoft Project the user is working with Text1, and hasn't
      // changed its name or added any other configuration).
      fields.addAll(file.getTasks().getPopulatedFields().stream().filter(FieldLists.CUSTOM_FIELDS::contains).collect(Collectors.toSet()));

      for (Task task : file.getTasks())
      {
         Map<FieldType, Object> values = fields.stream().collect(Collectors.toMap(f -> f, task::get));
      }
   }
}
