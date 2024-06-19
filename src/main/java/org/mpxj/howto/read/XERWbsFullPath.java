package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;

public class XERWbsFullPath
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setWbsIsFullPath(false);
      ProjectFile file = reader.read("my-sample.xer");
   }
}
