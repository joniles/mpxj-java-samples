package org.mpxj.howto.read;

import org.mpxj.ProjectFile;
import org.mpxj.primavera.PrimaveraPMFileReader;

public class PMXMLProjectID
{
   public void read() throws Exception
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setProjectID(123);
      ProjectFile file = reader.read("my-sample.xml");
   }
}
