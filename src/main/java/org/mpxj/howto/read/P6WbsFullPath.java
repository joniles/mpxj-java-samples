package org.mpxj.howto.read;

import net.sf.mpxj.primavera.PrimaveraDatabaseReader;

public class P6WbsFullPath
{
   public void read() throws Exception
   {
      PrimaveraDatabaseReader reader = new PrimaveraDatabaseReader();
      reader.setWbsIsFullPath(false);
   }
}
