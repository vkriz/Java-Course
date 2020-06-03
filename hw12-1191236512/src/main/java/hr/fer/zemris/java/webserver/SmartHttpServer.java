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
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Razred predstavlja jednostavni web server.
 * 
 * @author Valentina Križ
 *
 */
public class SmartHttpServer {
	/**
	 * Metoda od koje počinje izvođenje
	 * programa. Očekuje se jedan parametar
	 * komandne linije koji predstavlja port.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Exactly one argument expected.");
			return;
		}
		
		new SmartHttpServer(args[0]).start();
	}
	
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
	 * Istek sesije u sekundama
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
	 * Bazen dretvi
	 */
	private ExecutorService threadPool;
	
	/**
	 * Glavni direktorij
	 */
	private Path documentRoot;
	
	/**
	 * Mapa workera
	 */
	private Map<String, IWebWorker> workersMap;
	
	/**
	 * Mapa u kojoj se pamte sesije
	 */
	private Map<String, SessionMapEntry> sessions = 
			new HashMap<String, SmartHttpServer.SessionMapEntry>();
	
	/**
	 * Generator random brojeva
	 */
	private Random sessionRandom = new Random();
	
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
		setWorkers(serverProperties.getProperty("server.workers"));
	}

	/**
	 * Pomoćna metoda za postavljanje
	 * radnih dretvi. Prima ime datoteke
	 * u kojoj su spremljene postavke za 
	 * dretve.
	 * 
	 * @param fileName
	 */
	private void setWorkers(String fileName) {
		workersMap = new HashMap<>();
		try {
			FileInputStream in = new FileInputStream(fileName);
			String data = new String(
					in.readAllBytes(), 
					StandardCharsets.US_ASCII
			);
			in.close();
			
			List<String> lines = new ArrayList<>(Arrays.asList(data.split("\r?\n")));
			
			for(String line : lines) {
				String[] lineParts = line.split("=");
				if(lineParts.length != 2) {
					throw new IllegalArgumentException("Invalid file format.");
				}
				
				String path = lineParts[0].trim();
				String fqcn = lineParts[1].trim();
				Class<?> referenceToClass;
				try {
					referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
					Object newObject = referenceToClass.newInstance();
					IWebWorker iww = (IWebWorker)newObject;
					
					workersMap.put(path, iww);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				} 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		threadPool = Executors.newFixedThreadPool(workerThreads);
		
		// pokretanje dretve za čišćenje sessions
		new DaemonicThread().run();
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
						new InetSocketAddress(InetAddress.getByName(address), port)
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
	
	/**
	 * Razred predstavlja jednog workera
	 * za klijenta koji se koristi za upravljanje
	 * zahtjevima.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Spojna točka
		 */
		private Socket csocket;
		
		/**
		 * Dolazni podaci
		 */
		private InputStream istream;
		
		/**
		 * Odlazni podaci
		 */
		private OutputStream ostream;
		
		/**
		 * Verzija protokola
		 */
		private String version;
		
		/**
		 * Metoda
		 */
		private String method;
		
		/**
		 * Host
		 */
		private String host;
		
		/**
		 * Parametri
		 */
		private Map<String,String> params = new HashMap<String, String>();
		
		/**
		 * Privremeni parametri
		 */
		private Map<String,String> tempParams = new HashMap<String, String>();
		
		/**
		 * Trajni parametri
		 */
		private Map<String,String> permPrams = new HashMap<String, String>();
		
		/**
		 * Kolačići
		 */
		private List<RCCookie> outputCookies =
								new ArrayList<RequestContext.RCCookie>();
		
		/**
		 * Id sesije
		 */
		private String SID;
		
		/**
		 * Kontekst zahtjeva
		 */
		private RequestContext context;
		
		/**
		 * Konstruktor
		 * 
		 * @param csocket spojna točka
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			try {
				istream = new BufferedInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
				byte[] request = readRequest(istream);
				if(request==null) {
					sendError(ostream, 400, "Bad request");
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
					sendError(ostream, 400, "Bad request");
					ostream.flush();
		            csocket.close();
					return;
				}

				String method = firstLine[0].toUpperCase();
				if(!method.equals("GET")) {
					sendError(ostream, 405, "Method Not Allowed");
					ostream.flush();
		            csocket.close();
					return;
				}
				
				String version = firstLine[2].toUpperCase();
				if(!version.equals("HTTP/1.1")) {
					sendError(ostream, 505, "HTTP Version Not Supported");
					ostream.flush();
		            csocket.close();
					return;
				}
				
				for(String header : headers) {
					if(header.startsWith("Host:")) {
						host = header.split(":")[1].trim();
					}
				}
				
				checkSession(headers);
				
				String[] path = firstLine[1].split("\\?");
				
				if(path.length == 2) {
					parseParams(path[1]);
				}
				
				try {
					internalDispatchRequest(path[0], true);
				} catch (Exception e) {
					e.printStackTrace();
					ostream.flush();
					csocket.close();
				}

			} catch (IOException ex) {
				System.out.println("Error: " + ex.getMessage());
			} 
			
			try {
				ostream.flush();
				csocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		/**
		 * Metoda za provjeru sesije. Prima listu
		 * stringova koji predstavljaju zaglavlje,
		 * provjerava postoji li već sesija vezana za
		 * trenutnog klijenta.
		 * 
		 * @param headers
		 */
		private synchronized void checkSession(List<String> headers) {
			String sidCandidate = null;
			
			for(String header : headers) {
				if(!header.startsWith("Cookie:")) {
					continue;
				}
				
				String[] cookies = header.substring(8).split(";");
				for(String cookie : cookies) {
					if(cookie.startsWith("sid=")) {
						// vrijednost bez navodnika
						sidCandidate = cookie.trim().substring(5, cookie.length() - 1);
					}
				}
			}
			
			// ako smo pronašli kandidata
			if(sidCandidate != null) {
				SessionMapEntry session = sessions.get(sidCandidate);
				if(session != null) {
					// ako nije isti host ili je istekao, napravi novi sid
					if(!session.host.equals(host)) {
						sidCandidate = null;
					} else if(session.validUntil < System.currentTimeMillis() / 1000) {
						sessions.remove(sidCandidate);
						sidCandidate = null;
					} else {
						// inače produži validUntil
						session.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
					}
				} else {
					// ako ne postoji pripadni SessionMapEntry napravi novi sid
					sidCandidate = null;
				}
			} 

			if(sidCandidate == null) {
				// ako nema kandidata, napravi novi sid
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < 20; ++i) {
					int rand = sessionRandom.nextInt(25) + 65;
					sb.append(Character.toString((char) rand));
				}
				
				sidCandidate = sb.toString();
				
				SessionMapEntry newSession = new SessionMapEntry(
						sidCandidate, 
						host, 
						System.currentTimeMillis() / 1000 + sessionTimeout, 
						new ConcurrentHashMap<>());	
				sessions.put(sidCandidate, newSession);
			
				RCCookie cookie = new RCCookie("sid", sidCandidate, null, host, "/");
				cookie.setHttpOnly(true);
				outputCookies.add(cookie);
			}
			
			SessionMapEntry entry = sessions.get(sidCandidate);
			permPrams = entry.map;
		}
		
		/**
		 * Pomoćna metoda za parsiranje parametara 
		 * dobivenih u stringu oblika
		 * name1=value1&...&namen=valuen.
		 * Imena i odgovarajuće vrijednosti parametara 
		 * se spremaju u mapu params.
		 * 
		 * @param string
		 */
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
		
		/**
		 * Pomoćna metoda za slanje odgovora
		 * (bez rijela).
		 * 
		 * @param cos outputstream
		 * @param statusCode status kod
		 * @param statusText status tekst
		 * @throws IOException
		 */
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
		
		/**
		 * Metoda za interno preusmjeravanje zahtjeva.
		 * Prima putanju do datoteke i boolean varijablu
		 * koja govori da li je metoda pozvana unutar samog
		 * servera ili od strane klijenta.
		 * Metoda klijentu ograničava pristup određenim resursima
		 * na serveru.
		 * 
		 * @param urlPath
		 * @param directCall
		 * @throws Exception
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			int lastDot = urlPath.lastIndexOf('.');
			String extension = urlPath.substring(lastDot + 1);
			
			if(context == null) {
				context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
			}
			
			if(urlPath.startsWith("/private") && directCall) {
				sendError(ostream, 404, "File not found.");
				ostream.flush();
	            csocket.close();
				return;
			}
			
			if(urlPath.startsWith("/ext")) {
				Class<?> referenceToClass;
				try {
					referenceToClass = this.getClass().getClassLoader().loadClass(
							"hr.fer.zemris.java.webserver.workers." + urlPath.substring(5));
					
					Object newObject = referenceToClass.newInstance();
					IWebWorker iww = (IWebWorker)newObject;
					iww.processRequest(context);
					
					ostream.flush();
		            csocket.close();
					return;
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				} 
			}
			
			if(workersMap.containsKey(urlPath)) {
				workersMap.get(urlPath).processRequest(context);
				ostream.flush();
	            csocket.close();
				return;
			}
			
			Path requestedPath = documentRoot.resolve(urlPath.substring(1));
			
			if(!Files.isReadable(requestedPath)) {
				sendError(ostream, 404, "File not found.");
				ostream.flush();
	            csocket.close();
				return;
			}
			
			if(extension.equals("smscr")) {
				try(InputStream in = new FileInputStream(requestedPath.toString())) {
					String data = new String(in.readAllBytes(), StandardCharsets.UTF_8);
					new SmartScriptEngine(
							new SmartScriptParser(data).getDocumentNode(),
							context
							).execute();
				} catch(IOException ex) {
				}
				ostream.flush();
	            csocket.close();
				return;
			}
			
			String mimeType = mimeTypes.get(extension);
			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}
			
			context.setMimeType(mimeType);
			context.setStatusCode(200);
			
			try(InputStream in = new FileInputStream(requestedPath.toString())) {
				byte[] bytes = in.readAllBytes();
				context.setContentLength((long) new String(bytes, StandardCharsets.UTF_8).length());
				context.write(bytes);
			} catch(IOException ex) {
				ex.printStackTrace();
			}
			
			ostream.flush();
            csocket.close();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
	}
	
	/**
	 * Pomoćni razred, predstavlja jedan
	 * zapis u mapi u kojoj se pamte sve
	 * sesije.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class SessionMapEntry {
		/**
		 * Id sesije
		 */
		String sid;
		
		/**
		 * Host
		 */
		String host;
		
		/**
		 * Do kada je sesija aktivna
		 */
		long validUntil;
		
		/**
		 * Podaci vezani za sesiju
		 */
		Map<String, String> map;
		
		/**
		 * Kontruktor, prima vrijednosti
		 * svih parametara.
		 * 
		 * @param sid
		 * @param host
		 * @param validUntil
		 * @param map
		 */
		public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = map;
		}
	}
	
	/**
	 * Dretva čiji je posao brisanje nepotrebnih
	 * informacija o sesijama. Podaci se brišu svakih
	 * 5 minuta.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private class DaemonicThread extends Thread {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			while(true) {
				Set<String> keySet = sessions.keySet();
				
				for(String key : keySet) {
					SessionMapEntry session = sessions.get(key);
					if(session.validUntil < System.currentTimeMillis() / 1000) {
						sessions.remove(key);
					}
				}
				
				try {
					sleep(300000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
