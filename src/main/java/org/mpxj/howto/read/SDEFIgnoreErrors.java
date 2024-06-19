package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.sdef.SDEFReader;

public class SDEFIgnoreErrors
{
   public void read() throws Exception
   {
      SDEFReader reader = new SDEFReader();
      reader.setIgnoreErrors(false);
      ProjectFile project = reader.read("my-sample.sdef");
   }
}
