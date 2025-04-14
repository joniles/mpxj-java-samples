package org.mpxj.howto.read;

import org.mpxj.DataType;
import org.mpxj.FieldType;
import org.mpxj.TaskField;
import org.mpxj.primavera.PrimaveraXERFileReader;

import java.util.Map;

public class XERRegisterType
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      Map<String, DataType> fieldTypeMap = reader.getFieldTypeMap();
      fieldTypeMap.put("an_example_id", DataType.INTEGER);
      Map<FieldType, String> activityFieldMap = reader.getActivityFieldMap();
      activityFieldMap.put(TaskField.NUMBER2, "an_example_id");
   }
}
