package org.mpxj.howto.write;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.sdef.SDEFWriter;

import java.nio.charset.StandardCharsets;

public class SDEFCharset
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      SDEFWriter writer = new SDEFWriter();
      writer.setCharset(StandardCharsets.UTF_8);
      writer.write(project, fileName);
   }
}
