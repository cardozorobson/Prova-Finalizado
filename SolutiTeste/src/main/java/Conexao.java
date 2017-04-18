
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.parser.ParseException;

public class Conexao {

    public static String recebeValidacao = "";
    public static StringBuilder sb = new StringBuilder();
    public static String receiveJson = "";
    public static String separaJson = "";
    public static String id = "";
    public static String session_id = "";

    public Conexao() {

    }

    
      //Método que realiza uma requisição POST de Json para retornar uma String.
     
    public String requisicaoPost(final String urlString, final JSONObject json) {

        String retorno = "";

        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Cookie", session_id);
            urlConnection.setRequestMethod("POST");

            urlConnection.connect();

            //Obtendo objeto Json:
            JSONObject jsonParam = json;

            try (OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream())) {
                out.write(jsonParam.toString());
            }

            int HttpResult = urlConnection.getResponseCode();

            System.out.println(HttpResult);

            BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                recebeValidacao += inputLine;
                //System.out.println(inputLine);

            }

            br.close();

            System.out.println(urlConnection.getResponseMessage());
            return urlConnection.getResponseMessage();

        } catch (IOException e) {

            System.err.println(e.toString());
            return "Erro na conexão.";

        }
    }

    public String requisicaoGet(final String urlFinal) throws NoSuchAlgorithmException, KeyManagementException, ParseException {

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

        String retorno = "";

        StringBuilder sb = new StringBuilder();
        String http = "https://api-prova.lab.ca.inf.br:9445/desafio";
        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(http);
            urlConnection = (HttpURLConnection) url.openConnection();
            session_id = urlConnection.getHeaderField("Set-Cookie");
            //System.out.println(session_id);

            urlConnection.connect();

            int HttpResult = urlConnection.getResponseCode();

            if (HttpResult == HttpURLConnection.HTTP_OK) {

                try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                    String line = "";
                    
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("");
                    }
                }

                JSONObject obj = new JSONObject(sb.toString()); //CRIA UM JSONOBJECT COM O RETORNO E SEPARA.
                separaJson = obj.getString("desafio");//JOGA O VALOR SEPARADO EM UMA STRING
                //System.out.println(separaJson);

                return sb.toString();

            } else {

                
                return "Requisição não foi feita com sucesso. Verifique sua conexão com a internet.";

            }

        } catch (IOException e) {

            System.err.println(e.toString());
            return "Erro na conexão.";

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        
    }

    public static byte[] convertStringToByteArray() {

        String stringToConvert = separaJson;

        byte[] theByteArray = stringToConvert.getBytes();

        return theByteArray;

    }
    
    public String imprimeResultado()
    {
        return recebeValidacao;
    }

}
