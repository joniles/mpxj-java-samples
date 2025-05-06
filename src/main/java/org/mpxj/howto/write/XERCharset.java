package org.mpxj.howto.write;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraXERFileWriter;

import java.nio.charset.Charset;

public class XERCharset
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      PrimaveraXERFileWriter writer = new PrimaveraXERFileWriter();
      writer.setCharset(Charset.forName("GB2312"));
      writer.write(project, fileName);
   }
}
