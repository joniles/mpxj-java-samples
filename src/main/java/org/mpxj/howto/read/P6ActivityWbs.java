package org.mpxj.howto.read;

import net.sf.mpxj.primavera.PrimaveraDatabaseReader;

public class P6ActivityWbs
{
   public void read() throws Exception
   {
      PrimaveraDatabaseReader reader = new PrimaveraDatabaseReader();
      reader.setMatchPrimaveraWBS(false);
   }
}
