package br.edu.ifrn.suap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class SuapClient {
	
	private static final String AUTHORIZE_URL= "https://suap.ifrn.edu.br/o/authorize/";
	private static final String TOKEN_URL= "https://suap.ifrn.edu.br/o/token/";
	private static final String REDIRECT_URL= "http://localhost:8000";
	
	private String id;
	private String secret;
	private ServerSocket server;
	private String token;

	public SuapClient(String id, String secret){
		this.id = id;
		this.secret = secret;
	}
	
	public boolean isAuthorized() {
		return this.token != null;
	}	

	public void authorize() throws IOException{

		if(this.server!=null) {
			this.server.close();
			this.server = null;
		}
		
		server = new ServerSocket(8000);
		String url = AUTHORIZE_URL+"?response_type=code&grant_type=authorization-code=&client_id="+this.id;
		Runtime.getRuntime().exec("sensible-browser "+url);
		
		while (true) {
			try (Socket socket = server.accept()) {
				
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				String line = reader.readLine();
				String code = null;
				String token = null;
				String httpResponse;
				while (!line.isEmpty()) {
					if(line.indexOf("GET /?code=")==0){
						code = line.split("=")[1].split(" ")[0];
						break;
					}
					line = reader.readLine();
				}
				if(code!=null){
					String params = "client_id="+this.id+"&client_secret="+this.secret+"&code="+code+"&redirect_uri="+REDIRECT_URL+"&grant_type=authorization_code";
					HttpClient client = HttpClient.newHttpClient();
					HttpRequest request = HttpRequest.newBuilder().uri(URI.create(TOKEN_URL)).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(params)).build();
					HttpResponse<String> response = client.send(request,
					HttpResponse.BodyHandlers.ofString());
					token = response.body();
					httpResponse = "HTTP/1.1 200 OK\r\n\r\nSuccess!!!";

				} else {
					httpResponse = "HTTP/1.1 200 OK\r\n\r\nFailed!!!";
				
				}
				socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
				socket.getOutputStream().flush();
				socket.getOutputStream().close();
				this.token = token;
			} catch (Exception e) {
				e.printStackTrace();
				server.close();
				this.token = null;
			}
		}
	}
	
	public String get(String url, String params) throws IOException {
		return null;
	}
	
	public String post(String url, String params) throws IOException {
		return null;
	}
	
	public String put(String url, String params) throws IOException {
		return null;
	}
	
	public String delete(String url, String params) throws IOException {
		return null;
	}

	public static void main(String args[]) throws IOException {
		SuapClient client = new SuapClient("XXXXX", "XXXXX");
		if(!client.isAuthorized()) client.authorize();
		System.out.println(client.get("/me", null));
	}

}