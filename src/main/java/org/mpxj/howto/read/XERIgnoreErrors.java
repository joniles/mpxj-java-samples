package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraXERFileReader;

public class XERIgnoreErrors
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setIgnoreErrors(false);
      ProjectFile project = reader.read("my-sample.xer");
   }
}
