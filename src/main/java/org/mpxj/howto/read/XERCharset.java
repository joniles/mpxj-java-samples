package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;

import java.nio.charset.Charset;

public class XERCharset
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setCharset(Charset.forName("GB2312"));
      ProjectFile project = reader.read("my-sample.xer");
   }
}
