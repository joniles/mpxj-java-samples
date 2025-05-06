package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraXERFileReader;

public class XERProjectID
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setProjectID(123);
      ProjectFile file = reader.read("my-sample.xer");
   }
}
