package org.mpxj.howto.read;

import org.mpxj.FieldType;
import org.mpxj.primavera.PrimaveraXERFileReader;

import java.util.Map;

public class XERAttributeMaps
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      Map<FieldType, String> resourceFieldMap = reader.getResourceFieldMap();
      Map<FieldType, String> wbsFieldMap = reader.getWbsFieldMap();
      Map<FieldType, String> activityFieldMap = reader.getActivityFieldMap();
      Map<FieldType, String> assignmentFieldMap = reader.getAssignmentFieldMap();
   }
}
