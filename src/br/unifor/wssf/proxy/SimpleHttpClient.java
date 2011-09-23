package br.unifor.wssf.proxy;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient {

	FileWriter writer;
	private int responseLength;

	public void setProxy(String host, String port) {
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", port);
	}
	
	public int getResponseLength() {
		return responseLength;
	}
	

	/**
	 * @param args
	 */
	public String requisitar(String urlString, String serverSelectionPolicy, int timeOut) throws IOException {
		
		URL url = new URL(urlString);

		// cria o objeto httpurlconnection
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// seta o metodo
		con.setRequestProperty("Request-Method", "GET");
		con.setRequestProperty("ServerSelectionPolicy", serverSelectionPolicy);
		
		con.setUseCaches(false); //IMPORTANTE: Desabilita o cache

		// seta a variavel para ler o resultado
		con.setDoInput(true);
		con.setDoOutput(false);
		
		con.setReadTimeout(timeOut);
		
		// conecta com a url destino
		con.connect();
		
		if (con.getResponseCode() == 200) {
			// abre a conexao pra input
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"iso-8859-1"));

			// le ate o final
			//StringBuffer newData = new StringBuffer(1000);
			String s = "";
			int bytesIn = 0;
			while (null != ((s = br.readLine()))) {
				bytesIn = s.length();
				responseLength += bytesIn;
			}
			br.close();
		}

		// imprime o numero do resultado
		return "Resultado: " + con.getResponseCode() + "/"
				+ con.getResponseMessage();
	}
	
	public static void main(String[] args) {
		SimpleHttpClient c;
		String clientRequest = "http://mozilla.c3sl.ufpr.br/releases/firefox/releases/6.0/win32/pt-BR/Firefox%20Setup%206.0.exe";
		try {
			c = new SimpleHttpClient();
			c.setProxy("localhost", "8080");
			long start = System.currentTimeMillis();
			String message = c.requisitar(clientRequest,"NoPolicy",300000);
			long end = System.currentTimeMillis();
			long duracao = (end - start);
			System.out.println("    - Fim da requisição: " + message);
			System.out.println("    - Duração da requisição: " + duracao + " ms.");
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}

}
