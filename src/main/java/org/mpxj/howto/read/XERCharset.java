package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraXERFileReader;

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
