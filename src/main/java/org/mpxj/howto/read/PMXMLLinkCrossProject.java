package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraPMFileReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class PMXMLLinkCrossProject
{
   public void read() throws Exception
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setLinkCrossProjectRelations(true);
      InputStream is = new FileInputStream("my-sample.xml");
      List<ProjectFile> files = reader.readAll(is);
   }
}
