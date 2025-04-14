package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraXERFileReader;

public class XERWbsFullPath
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setWbsIsFullPath(false);
      ProjectFile file = reader.read("my-sample.xer");
   }
}
