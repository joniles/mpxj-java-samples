package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraPMFileReader;

import java.util.List;

public class PMXMLReadAll
{
   public void read() throws Exception
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      List<ProjectFile> files = reader.readAll("my-sample.xml");
   }
}
