package org.mpxj.howto.read;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.primavera.PrimaveraPMFileReader;

import java.util.List;

public class PMXMLLinkCrossProject
{
   public void read() throws Exception
   {
      PrimaveraPMFileReader reader = new PrimaveraPMFileReader();
      reader.setLinkCrossProjectRelations(true);
      List<ProjectFile> files = reader.readAll("my-sample.xml");
   }
}
