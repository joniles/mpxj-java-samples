package org.mpxj.howto.read;

import org.mpxj.FieldType;
import org.mpxj.TaskField;
import org.mpxj.primavera.PrimaveraXERFileReader;

import java.util.Map;

public class XERAttributeConfig
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      Map<FieldType, String> activityFieldMap = reader.getActivityFieldMap();

      //
      // Store rsrc_id in NUMBER1
      //
      activityFieldMap.put(TaskField.NUMBER1, "rsrc_id");

      //
      // Read an Activity column called an_example_field and store it in TEXT10
      //
      activityFieldMap.put(TaskField.TEXT10, "an_example_field");
   }
}
