package hr.fer.zemris.java.webserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;

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
		// … start server thread if not already running …
		// … init threadpool by Executors.newFixedThreadPool(...); …
	}
	
	/**
	 * Metoda za zaustavljanje servera.
	 */
	protected synchronized void stop() {
		// … signal server thread to stop running …
		// … shutdown threadpool …
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
			// given in pesudo-code:
			// open serverSocket on specified port
			// while(true) {
			// Socket client = serverSocket.accept();
			// ClientWorker cw = new ClientWorker(client);
			// submit cw to threadpool for execution
			// }
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
			 // obtain output stream from socket
			 // Then read complete request header from your client in separate method...
			 List<String> request = readRequest();
			 // If header is invalid (less then a line at least) return response status 400
			 String firstLine = request.get(0);
			 // Extract (method, requestedPath, version) from firstLine
			 // if method not GET or version not HTTP/1.0 or HTTP/1.1 return response status 400
			 // Go through headers, and if there is header “Host: xxx”, assign host property
			 // to trimmed value after “Host:”; else, set it to server’s domainName
			 // If xxx is of form some-name:number, just remember “some-name”-part
			 String path; String paramString;
			 // (path, paramString) = split requestedPath to path and parameterString
			 // parseParameters(paramString); ==> your method to fill map parameters
			 // requestedPath = resolve path with respect to documentRoot
			 // if requestedPath is not below documentRoot, return response status 403 forbidden
			 // check if requestedPath exists, is file and is readable; if not, return status 404
			 // else extract file extension
			 // find in mimeTypes map appropriate mimeType for current file extension
			 // (you filled that map during the construction of SmartHttpServer from mime.properties)
			 // if no mime type found, assume application/octet-stream
			 // create a rc = new RequestContext(...); set mime-type; set status to 200
			 // If you want, you can modify RequestContext to allow you to add additional headers
			 // so that you can add “Content-Length: 12345” if you know that file has 12345 bytes
			 // open file, read its content and write it to rc (that will generate header and send
			 // file bytes to client)
		}
		
		private List<String> readRequest() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
