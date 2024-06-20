package org.mpxj.howto.write;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.planner.PlannerWriter;

import java.nio.charset.Charset;

public class PlannerCharset
{
   public void write(ProjectFile project, String fileName) throws Exception
   {
      PlannerWriter writer = new PlannerWriter();
      writer.setCharset(Charset.forName("GB2312"));
      writer.write(project, fileName);
   }
}
