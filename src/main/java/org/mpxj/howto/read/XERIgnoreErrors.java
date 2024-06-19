package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;

public class XERIgnoreErrors
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setIgnoreErrors(false);
      ProjectFile project = reader.read("my-sample.xer");
   }
}
