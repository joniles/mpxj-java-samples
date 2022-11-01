package org.mpxj.dates;

import net.sf.mpxj.common.DateHelper;
import net.sf.mpxj.mpp.MPPUtility;
import org.mpxj.fields.FieldSamples;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateSamples {

   public static void main(String[] argv) throws Exception {
      DateSamples samples = new DateSamples();
      samples.handlingTimeZones();
   }

   private void handlingTimeZones() throws Exception
   {
      // MPP files use a couple of timestamp representations,
      // but they're all based on an elapsed duration since an epoch timestamp,
      // which in this case is 1983-12-31 00:00:00 BUT IN YOUR CURRENT TIMEZONE.
      // MPP files don't know about timezones, so if you open an MPP file in
      // London and New York they'll both show the start timestamp for a task starting at 9am,
      // even though the task being performed in London started at 9am, which
      // would actually be 4am in New York.

      // MPXJ does two things to create a Date instance for you.
      // First it converts the MS Project representation into a number of milliseconds
      // from the Java timestamp epoch, then it converts that to UTC based on your
      // current timezone (as returned by TimeZone.getTimeZone).
      // Here's an example: this value has already been converted to
      // milliseconds from the Java epoch in UTC.
      long taskStartTimestamp = 1665651600000l;

      // This gives us: Thu Oct 13 09:00:00 BST 2022
      TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
      System.out.println("Using " + TimeZone.getDefault().getID() + ": " + DateHelper.getTimestampFromLong(taskStartTimestamp));

      // This gives us: Thu Oct 13 09:00:00 PDT 2022
      TimeZone.setDefault(TimeZone.getTimeZone("US/Pacific"));
      System.out.println("Using " + TimeZone.getDefault().getID() + ": " + DateHelper.getTimestampFromLong(taskStartTimestamp));

      // Notice that we get the same timestamp "value" (Thu Oct 13 09:00:00 2022)
      // just the timezone component changes, which matches the behavior with MS Project -
      // we'll see the same start date/time regardless of the time zone the MPP
      // file was created in or where it is viewed.

      // This is fine if MPXJ is being used as part of a desktop application:
      // the dates it reports will make sense for an end user.

      // However, if MPXJ is being used on a server (perhaps part of a SaaS application)
      // where the default server timezone is unrelated to the end user's timezone
      // things can get tricky.

      // So here we are on our server, which is configured to use Paris time.
      // This gives us: Using Europe/Paris: Thu Oct 13 09:00:00 CEST 2022
      TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
      Date parisDate = DateHelper.getTimestampFromLong(taskStartTimestamp);
      System.out.println("Using " + TimeZone.getDefault().getID() + ": " + parisDate);

      // Again, we're getting the same timestamp "value" (Thu Oct 13 09:00:00 2022), just in the Paris timezone.
      // The project plan and the user who wants to see this date are actually in California,
      // If we're _just_ displaying the value to the user, we can ignore the timezone:
      // so we get 2022-10-13 09:00
      System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(parisDate));

      // If need to work with a Date instance which correctly represents the date/time in the user's
      // timezone, we'll need to do some work to transform the date.

      TimeZone sourceZone = TimeZone.getDefault();
      TimeZone targetZone = TimeZone.getTimeZone("US/Pacific");

      Date result = switchTimezone(parisDate, sourceZone, targetZone);

      TimeZone.setDefault(targetZone);
      System.out.println("Using " + TimeZone.getDefault().getID() + ": " + parisDate);
   }

   public Date switchTimezone(Date date, TimeZone sourceZone, TimeZone targetZone)
   {
      long utc = date.getTime();
      utc += sourceZone.getRawOffset();
      if (sourceZone.inDaylightTime(date))
      {
         utc -= sourceZone.getDSTSavings();
      }

      utc -= targetZone.getRawOffset();
      Date result = new Date(utc);
      if (targetZone.inDaylightTime(result))
      {
         utc += targetZone.getDSTSavings();
         result = new Date(utc);
      }

      return result;
   }
}
