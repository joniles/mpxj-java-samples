package org.mpxj.howto.read;

import org.mpxj.primavera.StructuredTextParser;
import org.mpxj.primavera.StructuredTextRecord;

import java.io.FileInputStream;

public class PLF
{
   public void read() throws Exception
   {
      StructuredTextParser parser = new StructuredTextParser();
      StructuredTextRecord record = parser.parse(new FileInputStream("test.plf"));
   }
}