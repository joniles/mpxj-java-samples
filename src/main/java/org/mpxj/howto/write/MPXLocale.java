package org.mpxj.howto.write;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpx.MPXWriter;

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
