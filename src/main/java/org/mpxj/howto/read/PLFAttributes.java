package org.mpxj.howto.read;

import net.sf.mpxj.primavera.StructuredTextParser;
import net.sf.mpxj.primavera.StructuredTextRecord;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class PLFAttributes
{
   public void read() throws Exception
   {
      StructuredTextParser parser = new StructuredTextParser();
      StructuredTextRecord record = parser.parse(new FileInputStream("test.plf"));

      record.getAttribute("attribute_name");

      Map<String,String> attributes = record.getAttributes();
      attributes.get("attribute_name");

      String recordNumber = record.getRecordNumber();
      String recordName = record.getRecordName();

      List<StructuredTextRecord> childRecords = record.getChildren();

      StructuredTextRecord child = record.getChild("child_name");
   }
}
