package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.mpx.MPXWriter;

import java.util.Locale;

public class MPXLocale
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      MPXWriter writer = new MPXWriter();
      writer.setLocale(Locale.GERMAN);
      writer.write(project, fileName);
   }
}
