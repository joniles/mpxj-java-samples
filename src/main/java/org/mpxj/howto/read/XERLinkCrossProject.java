package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraXERFileReader;

import java.util.List;

public class XERLinkCrossProject
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      reader.setLinkCrossProjectRelations(true);
      List<ProjectFile> files = reader.readAll("my-sample.xer");
   }
}
