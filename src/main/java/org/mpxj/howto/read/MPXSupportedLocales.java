package org.mpxj.howto.read;

import org.mpxj.mpx.MPXReader;

import java.util.Locale;

public class MPXSupportedLocales
{
   public void read() throws Exception
   {
      Locale[] locales = MPXReader.getSupportedLocales();
   }
}
