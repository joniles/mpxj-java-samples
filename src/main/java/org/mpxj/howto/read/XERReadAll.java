package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class XERReadAll
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      InputStream is = new FileInputStream("my-sample.xer");
      List<ProjectFile> files = reader.readAll(is);
   }
}
