package org.mpxj.howto.read;

import org.mpxj.primavera.PrimaveraPMFileReader;

import java.io.FileInputStream;
import java.util.Map;

public class PMXMLListProjects
{
   public void read() throws Exception
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      FileInputStream is = new FileInputStream("my-sample.xml");
      Map<Integer, String> projects = reader.listProjects(is);
      System.out.println("ID\tName");
      for (Map.Entry<Integer, String> entry : projects.entrySet())
      {
         System.out.println(entry.getKey()+"\t"+entry.getValue());
      }
   }
}
