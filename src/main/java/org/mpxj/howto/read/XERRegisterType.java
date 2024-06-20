package org.mpxj.howto.read;

import net.sf.mpxj.DataType;
import net.sf.mpxj.FieldType;
import net.sf.mpxj.TaskField;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;

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
