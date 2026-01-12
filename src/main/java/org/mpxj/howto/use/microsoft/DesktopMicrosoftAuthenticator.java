package org.mpxj.howto.use.microsoft;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates OAuth authentication for Microsoft run locally on the desktop.
 * The code below will allow you to log in to Microsoft via a browser on your desktop,
 * and return an access token suitable for use with
 * the Microsoft hosted Project Server/PWA/Project Online API.
 * Note that this code is just to demonstrate the basic settings and steps required
 * to get an OAuth access token suitable for use with the PWA API.
 * It is highly recommended that you use a library like Microsoft's MSAL to provide a
 * production-grade OAuth flow.
 */
public class DesktopMicrosoftAuthenticator
{
   /**
    * Prerequisites:
    * - you have set up an App Registration in Entra, and have a Client ID and Client Secret
    * - you have assigned AllSites.FullControl, ProjectWebApp.FullControl delegated Sharepoint permissions to this App Registration
    * - you have added a localhost redirect URL for the App Registration in Entra
    *
    * The four command line arguments expected are:
    * - Resource: this should be https://example.sharepoint.com if your PWA instance is hosted at https://example.sharepoint.com/sites/pwa
    * - ClientID: from your App Registration in Entra
    * - ClientSecret: from your App Registration in Entra
    * - Redirect URL: e.g. this must be for localhost, http://localhost:8000/test and must match the redirect URL you set up in Entra
    *
    * @param argv command line arguments
    */
   public static void main(String[] argv) throws Exception
   {
      if (argv.length != 4)
      {
         System.out.println("Usage: MicrosoftAuthenticator <Resource> <Client ID> <Client Secret> <Redirect URL>");
         return;
      }

      DesktopMicrosoftAuthenticator auth = new DesktopMicrosoftAuthenticator();
      auth.setResource(argv[0]);
      auth.setClientID(argv[1]);
      auth.setClientSecret(argv[2]);
      auth.setRedirectUrl(argv[3]);
      TokenResponse tokenResponse = auth.authenticate();

      System.out.println("Access token: " + tokenResponse.getAccessToken());
   }

   public DesktopMicrosoftAuthenticator()
   {
      m_mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      m_mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
   }
   
   public String getAuthorizeUrl()
   {
      return m_authorizeUrl;
   }

   public void setAuthorizeUrl(String authorizeUrl)
   {
      this.m_authorizeUrl = authorizeUrl;
   }

   public String getTokenUrl()
   {
      return m_tokenUrl;
   }

   public void setTokenUrl(String tokenUrl)
   {
      this.m_tokenUrl = tokenUrl;
   }

   public String getResource()
   {
      return m_resource;
   }

   public void setResource(String resource)
   {
      this.m_resource = resource;
   }

   public String getClientID()
   {
      return m_clientID;
   }

   public void setClientID(String clientID)
   {
      this.m_clientID = clientID;
   }

   public String getClientSecret()
   {
      return m_clientSecret;
   }

   public void setClientSecret(String clientSecret)
   {
      this.m_clientSecret = clientSecret;
   }

   public String getRedirectUrl()
   {
      return m_redirectUrl;
   }

   public void setRedirectUrl(String redirectUrl)
   {
      this.m_redirectUrl = redirectUrl;
   }

   public String getBrowserMessage()
   {
      return m_browserMessage;
   }

   public void setBrowserMessage(String browserMessage)
   {
      this.m_browserMessage = browserMessage;
   }

   public TokenResponse authenticate() throws IOException, URISyntaxException, InterruptedException
   {
      URL redirectUrl = new URL(m_redirectUrl);
      m_server = HttpServer.create(new InetSocketAddress(redirectUrl.getPort()), 0);
      m_server.createContext(redirectUrl.getPath(), this::handleAuthenticationResponse);
      m_server.setExecutor(null);
      m_server.start();

      // Request the authorization code
      Map<String, String> map = new HashMap<>();
      map.put("resource", m_resource);
      map.put("response_type", "code");
      map.put("state", "");
      map.put("client_id", m_clientID);
      map.put("redirect_uri", m_redirectUrl);
      URI requestURI = new URI(m_authorizeUrl+"?"+createQueryString(map));

      // Wait until the browser requests the redirect URL
      synchronized(m_server)
      {
         Desktop.getDesktop().browse(requestURI);
         m_server.wait();
      }
      m_server.stop(0);


      // We now have the authorization code, exchange this for an access token
      URL url = new URL(m_tokenUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      map.clear();
      map.put("grant_type", "authorization_code");
      map.put("code", m_code);
      map.put("redirect_uri", m_redirectUrl);
      map.put("client_id", m_clientID);
      map.put("client_secret", m_clientSecret);

      String body = createQueryString(map);
      byte[] postData = body.getBytes(StandardCharsets.UTF_8);
      connection.setRequestProperty("charset", "utf-8");
      connection.setRequestProperty("Content-Length", Integer.toString(postData.length));

      try(DataOutputStream wr = new DataOutputStream(connection.getOutputStream()))
      {
         wr.write(postData) ;
      }

      connection.connect();

      return m_mapper.readValue(connection.getInputStream(), TokenResponse.class);
   }

   private void handleAuthenticationResponse(HttpExchange t) throws IOException
   {
      byte[] response = m_browserMessage.getBytes();
      t.sendResponseHeaders(200, response.length);
      OutputStream os = t.getResponseBody();
      os.write(response);
      os.close();

      Map<String, List<String>> map = splitQuery(t.getRequestURI());
      m_code = map.get("code").get(0);

      synchronized(m_server)
      {
         m_server.notify();
      }
   }

   // From: https://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection
   private static Map<String, List<String>> splitQuery(URI url) throws UnsupportedEncodingException {
      final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
      final String[] pairs = url.getQuery().split("&");
      for (String pair : pairs) {
         final int idx = pair.indexOf("=");
         final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
         if (!query_pairs.containsKey(key)) {
            query_pairs.put(key, new LinkedList<>());
         }
         final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
         query_pairs.get(key).add(value);
      }
      return query_pairs;
   }

   private String urlEncodeUTF8(String value)
   {
      try
      {
         return URLEncoder.encode(value, "UTF-8");
      }

      catch (UnsupportedEncodingException e)
      {
         throw new RuntimeException(e);
      }
   }

   private String createQueryString(Map<String, String> map)
   {
      return map.entrySet().stream()
         .map(p -> urlEncodeUTF8(p.getKey()) + "=" + urlEncodeUTF8(p.getValue()))
         .reduce((p1, p2) -> p1 + "&" + p2)
         .orElse("");
   }

   public static class TokenResponse
   {
      public String getAccessToken()
      {
         return m_accessToken;
      }

      public void setAccessToken(String access_token)
      {
         m_accessToken = access_token;
      }

      public long getExpiresIn()
      {
         return m_expiresIn;
      }

      public void setExpiresIn(long expiresIn)
      {
         m_expiresIn = expiresIn;
      }

      public long getExpiresOn()
      {
         return m_expiresOn;
      }

      public void setExpiresOn(long expiresOn)
      {
         m_expiresOn = expiresOn;
      }

      public long getExtExpiresIn()
      {
         return m_extExpiresIn;
      }

      public void setExtExpiresIn(long extExpiresIn)
      {
         this.m_extExpiresIn = extExpiresIn;
      }

      public String getIdToken()
      {
         return m_idToken;
      }

      public void setIdToken(String idToken)
      {
         this.m_idToken = idToken;
      }

      public long getNotBefore()
      {
         return m_notBefore;
      }

      public void setNotBefore(long notBefore)
      {
         this.m_notBefore = notBefore;
      }

      public String getRefreshToken()
      {
         return m_refreshToken;
      }

      public void setRefreshToken(String refreshToken)
      {
         this.m_refreshToken = refreshToken;
      }

      public String getResource()
      {
         return m_resource;
      }

      public void setResource(String resource)
      {
         this.m_resource = resource;
      }

      public String getScope()
      {
         return m_scope;
      }

      public void setScope(String scope)
      {
         this.m_scope = scope;
      }

      public String getTokenType()
      {
         return m_tokenType;
      }

      public void setTokenType(String tokenType)
      {
         this.m_tokenType = tokenType;
      }

      private String m_accessToken;
      private long m_expiresIn;
      private long m_expiresOn;
      private long m_extExpiresIn;
      private String m_idToken;
      private long m_notBefore;
      private String m_refreshToken;
      private String m_resource;
      private String m_scope;
      private String m_tokenType;
   }

   private final ObjectMapper m_mapper = new ObjectMapper();
   private HttpServer m_server;
   private String m_code;

   private String m_authorizeUrl = "https://login.microsoftonline.com/common/oauth2/authorize";
   private String m_tokenUrl = "https://login.microsoftonline.com/common/oauth2/token";
   private String m_browserMessage = "Authentication successful. You can now close this window";

   private String m_resource;
   private String m_clientID;
   private String m_clientSecret;
   private String m_redirectUrl;
}
