package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpx.MPXReader;

import java.util.Locale;

public class MPXWithLocale
{
   public void read() throws Exception
   {
      MPXReader reader = new MPXReader();
      reader.setLocale(Locale.GERMAN);
      ProjectFile project = reader.read("my-sample.mpx");
   }
}
