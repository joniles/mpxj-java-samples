package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.mpx.MPXReader;

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
