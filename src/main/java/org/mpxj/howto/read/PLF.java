package org.mpxj.howto.read;

import net.sf.mpxj.primavera.StructuredTextParser;
import net.sf.mpxj.primavera.StructuredTextRecord;

import java.io.FileInputStream;

public class PLF
{
   public void read() throws Exception
   {
      StructuredTextParser parser = new StructuredTextParser();
      StructuredTextRecord record = parser.parse(new FileInputStream("test.plf"));
   }
}