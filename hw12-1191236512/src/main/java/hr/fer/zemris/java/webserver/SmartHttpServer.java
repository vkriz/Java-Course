package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Razred predstavlja jednostavni web server.
 * 
 * @author Valentina Križ
 *
 */
public class SmartHttpServer {
	/**
	 * Adresa servera
	 */
	private String address;
	
	/**
	 * Domena servera
	 */
	private String domainName;
	
	/**
	 * Port na kojemu server sluša
	 */
	private int port;
	
	/**
	 * Broj radnih dretvi
	 */
	private int workerThreads;
	
	/**
	 * Istek sesije
	 */
	private int sessionTimeout;
	
	/**
	 * Mapa mime tipova
	 */
	private Map<String,String> mimeTypes = new HashMap<String, String>();
	
	/**
	 * Serverska dretva
	 */
	private ServerThread serverThread;
	
	/**
	 * 
	 */
	private ExecutorService threadPool;
	
	/**
	 * 
	 */
	private Path documentRoot;
	
	/**
	 * Konstruktor koji prima ime
	 * datoteke u kojoj su spremljene
	 * postavke servera.
	 * 
	 * @param configFileName
	 */
	public SmartHttpServer(String configFileName) {
		Properties serverProperties = new Properties(); 
        FileInputStream in;
		try {
			in = new FileInputStream(configFileName);
			serverProperties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		address = serverProperties.getProperty("server.address");
		domainName = serverProperties.getProperty("server.domainName");
		port = Integer.parseInt(serverProperties.getProperty("server.port"));
		workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
		documentRoot = Paths.get(serverProperties.getProperty("server.documentRoot"));
		sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout"));
		setMimeTypes(serverProperties.getProperty("server.mimeConfig"));
		start();
	}

	/**
	 * Pomoćna metoda za učitavanje
	 * mime tipova iz datoteke sa zadanim
	 * imenom.
	 * 
	 * @param fileName
	 */
	private void setMimeTypes(String fileName) {
		Properties mimeProperties = new Properties(); 
        FileInputStream in;
		try {
			in = new FileInputStream(fileName);
			mimeProperties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    Set<String> keys = mimeProperties.stringPropertyNames();

		for(String key : keys) {
			mimeTypes.put(key, mimeProperties.getProperty(key));
		}
	}

	/**
	 * Metoda za pokretanje servera.
	 */
	protected synchronized void start() {
		serverThread = new ServerThread();
		serverThread.start();
		threadPool = Executors.newFixedThreadPool(port);
	}
	
	/**
	 * Metoda za zaustavljanje servera.
	 */
	@SuppressWarnings("deprecation")
	protected synchronized void stop() {
		serverThread.stop();
		threadPool.shutdown();
	}
	
	/**
	 * Pomoćni razred koji predstavlja jednu
	 * dretvu servera. Nasljeđuje klasu Thread.
	 * 
	 * @author Valentina Križ
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(
						new InetSocketAddress((InetAddress)null, port)
					);
				
				while(true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
		}
	}
	
	private class ClientWorker implements Runnable {
		private Socket csocket;
		private InputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private String host;
		private Map<String,String> params = new HashMap<String, String>();
		private Map<String,String> tempParams = new HashMap<String, String>();
		private Map<String,String> permPrams = new HashMap<String, String>();
		private List<RCCookie> outputCookies =
								new ArrayList<RequestContext.RCCookie>();
		private String SID;
		
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}
		@Override
		public void run() {
			// obtain input stream from socket
			try {
				InputStream cis = new BufferedInputStream(csocket.getInputStream());
				OutputStream cos = new BufferedOutputStream(csocket.getOutputStream());
				byte[] request = readRequest(cis);
				if(request==null) {
					sendError(cos, 400, "Bad request");
					return;
				}
				String requestStr = new String(
					request, 
					StandardCharsets.US_ASCII
				);
				
				List<String> headers = extractHeaders(requestStr);
				String[] firstLine = headers.isEmpty() ? 
					null : headers.get(0).split(" ");
				if(firstLine==null || firstLine.length != 3) {
					sendError(cos, 400, "Bad request");
					return;
				}

				String method = firstLine[0].toUpperCase();
				if(!method.equals("GET")) {
					sendError(cos, 405, "Method Not Allowed");
					return;
				}
				
				String version = firstLine[2].toUpperCase();
				if(!version.equals("HTTP/1.1")) {
					sendError(cos, 505, "HTTP Version Not Supported");
					return;
				}
				
				for(String header : headers) {
					if(header.startsWith("Host:")) {
						host = header.split(":")[0].trim();
					}
				}
				
				String[] path = firstLine[1].split("\\?");
				
				if(path.length == 2) {
					parseParams(path[1]);
				}
				
				Path documentRoot = Paths.get("./web");
				Path requestedPath = documentRoot.resolve(path[0].substring(1));

				if(!Files.isReadable(requestedPath)) {
					sendError(cos, 404, "File not found");
					return;
				}
				
				int lastDot = requestedPath.toString().lastIndexOf('.');
				String extension = requestedPath.toString().substring(lastDot + 1);
				String mimeType = mimeTypes.get(extension);
				if(mimeType == null) {
					mimeType = "application/octet-stream";
				}
				
				RequestContext rc = new RequestContext(cos, params, permPrams, outputCookies);
				rc.setMimeType(mimeType);
				rc.setStatusCode(200);

			} catch (IOException ex) {
				System.out.println("Error: " + ex.getMessage());
			} 
			
			try {
				csocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		private void parseParams(String string) {
			String[] parts = string.split("&");
			
			for(String part : parts) {
				String[] keyValue = part.split("=");
				
				if(keyValue.length == 2) {
					params.put(keyValue[0], keyValue[1]);
				}
			}
		}
		/**
		 * Zaglavlje predstavljeno kao jedan string splita po enterima
		 * pazeći na višeretčane atribute...
		 * 
		 * @param requestHeader
		 * @return
		 */
		private List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for(String s : requestHeader.split("\n")) {
				if(s.isEmpty()) break;
				char c = s.charAt(0);
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}
		
		/**
		 * Jednostavan automat koji čita zaglavlje HTTP zahtjeva
		 * 
		 * @param is
		 * @return zaglavlje u obliku polja byteova
		 * @throws IOException
		 */
		private byte[] readRequest(InputStream is) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
	l:		while(true) {
				int b = is.read();
				if(b==-1) return null;
				if(b!=13) {
					bos.write(b);
				}
				switch(state) {
				case 0: 
					if(b==13) { state=1; } else if(b==10) state=4;
					break;
				case 1: 
					if(b==10) { state=2; } else state=0;
					break;
				case 2: 
					if(b==13) { state=3; } else state=0;
					break;
				case 3: 
					if(b==10) { break l; } else state=0;
					break;
				case 4: 
					if(b==10) { break l; } else state=0;
					break;
				}
			}
			return bos.toByteArray();
		}
		
		// Pomoćna metoda za slanje odgovora bez tijela...
		private void sendError(OutputStream cos, int statusCode, String statusText) throws IOException {
			cos.write(
					("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
					"Server: simple java server\r\n"+
					"Content-Type: text/plain;charset=UTF-8\r\n"+
					"Content-Length: 0\r\n"+
					"Connection: close\r\n"+
					"\r\n").getBytes(StandardCharsets.US_ASCII)
				);
				cos.flush();
		}
	}
}
