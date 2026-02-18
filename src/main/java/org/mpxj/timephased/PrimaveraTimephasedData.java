package org.mpxj.timephased;

import org.mpxj.*;
import org.mpxj.mpp.TimescaleUnits;
import org.mpxj.reader.UniversalProjectReader;
import org.mpxj.utility.TimescaleUtility;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Generate an HTML file which approximates the "Resource Assignments" timephased data view in P6.
 */
public class PrimaveraTimephasedData
{
   public static void main(String[] argv) throws Exception
   {
      if (argv.length != 2)
      {
         System.out.println("Usage: PrimaveraTimephasedData <project file name> <output html file name>");
         return;
      }

      new PrimaveraTimephasedData().process(argv[0], argv[1]);
   }

   public void process(String inputProjectFile, String outputHtmlFile) throws Exception
   {
      m_file = new UniversalProjectReader().read(inputProjectFile);

      m_file.getResourceAssignments().forEach(this::addMissingTimephasedData);

      TimescaleUtility timescaleUtility = new TimescaleUtility();
      m_projectStart = m_file.getProjectProperties().getStartDate();
      m_projectFinish = m_file.getProjectProperties().getFinishDate();

      if (m_projectStart.getDayOfWeek() != DayOfWeek.SUNDAY)
      {
         LocalDateTime newProjectStart = m_projectStart.with(DayOfWeek.SUNDAY);
         if (newProjectStart.isAfter(m_projectStart))
         {
            newProjectStart = newProjectStart.minusWeeks(1);
         }
         m_projectStart = newProjectStart;
      }

      long days = m_projectStart.until(m_projectFinish, ChronoUnit.DAYS);
      m_timescale = timescaleUtility.createTimescale(m_projectStart, (int)days, TimescaleUnits.DAYS);


      FileOutputStream os = new FileOutputStream(outputHtmlFile);
      PrintStream ps = new PrintStream(os);
      ps.println("<html>");

      ps.println("<head>");
      ps.println("<style>");
      ps.println("table {border: 1px solid; border-collapse: collapse;}");
      ps.println("th {border: 1px solid;}");
      ps.println("td {border: 1px solid; min-width: 1em;}");
      ps.println("td.nonworking { background-color: #DDDDDD; }");
      ps.println("tr.child-resource { background-color: #FFFF00; }");
      ps.println("tr.parent-resource { background-color: #40FF6D; }");
      ps.println("</style>");
      ps.println("</head>");

      ps.println("<body>");

      ps.println("<h1>Planned</h1>");
      writeTimescale(ps, ResourceField.PLANNED_WORK, AssignmentField.PLANNED_WORK);

      ps.println("<h1>Actual</h1>");
      writeTimescale(ps, ResourceField.ACTUAL_WORK, AssignmentField.ACTUAL_WORK);

      ps.println("<h1>Remaining</h1>");
      writeTimescale(ps, ResourceField.REMAINING_WORK, AssignmentField.REMAINING_WORK);

      ps.println("</body>");
      ps.println("</html>");
   }

   private void writeTimescale(PrintStream ps, ResourceField resourceField, AssignmentField assignmentField)
   {
      ps.println("<table>");
      ps.println("<thead>");

      ps.println("<tr>");
      ps.println("<th>Activity ID</th><th>Activity Name</th>");
      LocalDateTime date = m_projectStart;
      while (date.isBefore(m_projectFinish))
      {
         ps.println("<th colspan='7'>" + DATE_FORMAT.format(date) + "</th>");
         date = date.plusWeeks(1);
      }
      ps.println("</tr>");

      ps.println("<tr>");
      ps.println("<th/><th/>");
      date = m_projectStart;
      while (date.isBefore(m_projectFinish))
      {
         ps.println("<th>" + DAY_FORMAT.format(date).charAt(0) + "</th>");
         date = date.plusDays(1);
      }
      ps.println("</tr>");

      for (Resource resource : m_file.getChildResources())
      {
         writeResource(ps, resource, resourceField, assignmentField);
      }

      ps.println("</thead>");
      ps.println("<tbody>");
      ps.println("</tbody>");
      ps.println("</table>");
   }

   private void writeResource(PrintStream ps, Resource resource, ResourceField resourceField, AssignmentField assignmentField)
   {
      List<ResourceAssignment> assignments = resource.getTaskAssignments();
      if (assignments.isEmpty() && resource.getChildResources().isEmpty())
      {
         return;
      }

      String cssClass = resource.getChildResources().isEmpty() ? "child-resource" : "parent-resource";
      ps.println("<tr class='"+ cssClass +"'>");
      ps.println("<td colspan='2'>" + resource.getName() + "</td>");
      writeWorkColumns(ps, resource.getCalendar(), resource.getTimephasedDurationValues(resourceField, m_timescale, TimeUnit.HOURS));
      ps.println("</tr>");

      for (ResourceAssignment assignment : assignments)
      {
         ps.println("<tr>");
         ps.println("<td>" + assignment.getTask().getActivityID() + "</td>");
         ps.println("<td>" + assignment.getTask().getName() + "</td>");
         ProjectCalendar calendar = assignment.getEffectiveCalendar();
         writeWorkColumns(ps, calendar, assignment.getTimephasedDurationValues(assignmentField, m_timescale, TimeUnit.HOURS));
         ps.println("</tr>");
      }

      resource.getChildResources().forEach(r -> writeResource(ps, r, resourceField, assignmentField));
   }

   private void writeWorkColumns(PrintStream ps, ProjectCalendar calendar, List<Duration> work)
   {
      for (int index=0; index < m_timescale.size(); index++)
      {
         LocalDateTimeRange range = m_timescale.get(index);
         Duration duration = work.get(index);
         String cssClass = calendar == null || calendar.isWorkingDay(range.getStart().getDayOfWeek()) ? "working" : "nonworking";

         if (duration == null || duration.getDuration() == 0)
         {
            ps.println("<td class='" + cssClass + "'/>");
         }
         else
         {
            ps.println("<td class='" + cssClass + "'>" + (int)duration.getDuration() + "</td>");
         }
      }
   }

   private void addMissingTimephasedData(ResourceAssignment assignment)
   {
      List<TimephasedWork> plannedWork = assignment.getRawTimephasedPlannedWork();
      if (plannedWork.isEmpty())
      {
         // Generate default timephased data if none is present
         double workingHours = assignment.getEffectiveCalendar().getWork(assignment.getPlannedStart(), assignment.getPlannedFinish(), TimeUnit.HOURS).getDuration();
         double plannedHours = assignment.getPlannedWork().convertUnits(TimeUnit.HOURS, assignment.getEffectiveCalendar()).getDuration();

         TimephasedWork item = new TimephasedWork();
         item.setStart(assignment.getPlannedStart());
         item.setFinish(assignment.getPlannedFinish());
         item.setTotalAmount(assignment.getPlannedWork());
         item.setAmountPerHour(Duration.getInstance(plannedHours/ workingHours, TimeUnit.HOURS));
         plannedWork.add(item);
      }

      if (assignment.getActualStart() != null) {
         List<TimephasedWork> actualWork = assignment.getRawTimephasedActualRegularWork();
         ProjectCalendar calendar = assignment.getEffectiveCalendar();
         if (actualWork.isEmpty()) {
            // Generate default timephased data if none is present
            LocalDateTime finish = assignment.getActualFinish();
            if (finish == null) {
               finish = calendar.getDate(assignment.getActualStart(), assignment.getActualWork());
            }

            double workingHours = assignment.getEffectiveCalendar().getWork(assignment.getActualStart(), finish, TimeUnit.HOURS).getDuration();
            double actualHours = assignment.getActualWork().convertUnits(TimeUnit.HOURS, assignment.getEffectiveCalendar()).getDuration();

            TimephasedWork item = new TimephasedWork();
            item.setStart(assignment.getActualStart());
            item.setFinish(finish);
            item.setTotalAmount(assignment.getActualWork());
            item.setAmountPerHour(Duration.getInstance(actualHours / workingHours, TimeUnit.HOURS));
            actualWork.add(item);
         }
      }

      if (assignment.getActualFinish() == null)
      {
         List<TimephasedWork> remainingWork = assignment.getRawTimephasedRemainingRegularWork();
         if (remainingWork.isEmpty())
         {
            // Generate default timephased data if none is present
            LocalDateTime start = assignment.getActualStart() == null ? assignment.getStart() : assignment.getRemainingEarlyStart();

            double workingHours = assignment.getEffectiveCalendar().getWork(start, assignment.getFinish(), TimeUnit.HOURS).getDuration();
            double remainingHours = assignment.getRemainingWork().convertUnits(TimeUnit.HOURS, assignment.getEffectiveCalendar()).getDuration();

            TimephasedWork item = new TimephasedWork();
            item.setStart(start);
            item.setFinish(assignment.getFinish());
            item.setTotalAmount(assignment.getRemainingWork());
            item.setAmountPerHour(Duration.getInstance(remainingHours / workingHours, TimeUnit.HOURS));
            remainingWork.add(item);
         }
      }
   }

   private ProjectFile m_file;
   private LocalDateTime m_projectStart;
   private LocalDateTime m_projectFinish;
   private List<LocalDateTimeRange> m_timescale;

   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd");
   private static final DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern("E");
}
