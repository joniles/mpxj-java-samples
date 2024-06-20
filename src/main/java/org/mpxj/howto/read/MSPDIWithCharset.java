package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.MSPDIReader;

import java.nio.charset.Charset;

public class MSPDIWithCharset
{
   public void read() throws Exception
   {
      MSPDIReader reader = new MSPDIReader();

      reader.setCharset(Charset.forName("GB2312"));
      ProjectFile project = reader.read("my-sample.xml");
   }
}
