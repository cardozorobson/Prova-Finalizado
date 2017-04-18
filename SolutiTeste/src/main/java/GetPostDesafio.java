/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;


/**
 *
 * @author robsoncardozo
 */
public class GetPostDesafio {
    
    public static String text = "";
    
    public static String recebeJson;//Recebe o valor que vem do get, sem o parametro
    
    
    static URL url;

    static URLConnection con;

    public static String GetDesafio() throws Exception {
        // Ignora a cadeia de certificados (SOLUÇÃO PARA O ERRO SSL)
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        int ch;
        
        
        url = new URL("https://api-prova.lab.ca.inf.br:9445/desafio");
        con = url.openConnection();
        Reader reader = new InputStreamReader(con.getInputStream());
        while (true) {
            ch = reader.read();
            if (ch == -1) {
                break;
            }
            
            text = text + (char) ch;
            
        }
            
      JSONObject parseJson = (JSONObject)new JSONParser().parse(text);
        
      recebeJson =  (String) parseJson.get("desafio");
      
      return text;
        
        
    }

  
    
     public static byte [] convertStringToByteArray()
    {

        String stringToConvert = recebeJson;

        byte[] theByteArray = stringToConvert.getBytes();

        return theByteArray;

    }
    
    
}
