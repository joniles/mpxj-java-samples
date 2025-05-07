package org.mpxj.howto.read;

import org.mpxj.primavera.PrimaveraXERFileReader;

import java.io.FileInputStream;
import java.util.Map;

public class XERListProjects
{
   public void read() throws Exception
   {
      PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
      FileInputStream is = new FileInputStream("my-sample.xer");
      Map<Integer, String> projects = reader.listProjects(is);
      System.out.println("ID\tName");
      for (Map.Entry<Integer, String> entry : projects.entrySet())
      {
         System.out.println(entry.getKey()+"\t"+entry.getValue());
      }
   }
}
