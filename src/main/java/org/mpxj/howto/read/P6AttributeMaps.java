package org.mpxj.howto.read;

import org.mpxj.FieldType;
import org.mpxj.primavera.PrimaveraDatabaseReader;

import java.util.Map;

public class P6AttributeMaps
{
   public void read() throws Exception
   {
      PrimaveraDatabaseReader reader = new PrimaveraDatabaseReader();
      Map<FieldType, String> resourceFieldMap = reader.getResourceFieldMap();
      Map<FieldType, String> wbsFieldMap = reader.getWbsFieldMap();
      Map<FieldType, String> activityFieldMap = reader.getActivityFieldMap();
      Map<FieldType, String> assignmentFieldMap = reader.getAssignmentFieldMap();
   }
}
