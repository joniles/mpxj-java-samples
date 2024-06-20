package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;

public class XERMatchWbs
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setMatchPrimaveraWBS(false);
      ProjectFile file = reader.read("my-sample.xer");
   }
}
