package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestContext {
	public static class RCCookie {
		/**
		 * Readonly ime kolačića
		 */
		private String name;
		
		/**
		 * Readonly vrijednost kolačića
		 */
		private String value;
		
		/**
		 * Readonly domena kolačića
		 */
		private String domain;
		
		/**
		 * Readonly putanja kolačića
		 */
		private String path;
		
		/**
		 * Readonly maksimalna starost kolačića
		 */
		private Integer maxAge;
		
		/**
		 * Konstruktor koji prima vrijednosti
		 * svih varijabli.
		 * 
		 * @param name
		 * @param value
		 * @param domain
		 * @param path
		 * @param maxAge
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}
		
		/**
		 * Getter za varijablu name
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter za varijablu value
		 * 
		 * @return value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Getter za varijablu domain
		 * 
		 * @return domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Getter za varijablu path
		 * 
		 * @return path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Getter za varijablu maxAge
		 * 
		 * @return maxAge
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}
	
	/**
	 * 
	 */
	private OutputStream outputStream;
	private Charset charset;
	
	/**
	 * Write-only
	 */
	private String encoding;
	
	/**
	 * Write-only
	 */
	private int statusCode;
	
	/**
	 * Write-only
	 */
	private String statusText;
	
	/**
	 * Write-only
	 */
	private String mimeType;
	private Long contentLength;
	
	/**
	 * Read-only
	 */
	private Map<String,String> parameters;
	private Map<String,String> temporaryParameters;
	private Map<String,String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated;
	
	/**
	 * Setter za varijablu encoding
	 * 
	 * @param encoding
	 * @throws RuntimeException ako je header
	 * 			već generiran
	 */
	public void setEncoding(String encoding) {
		if(headerGenerated) {
			throw new RuntimeException("Header already generated.");
		}
		this.encoding = encoding;
	}
	
	/**
	 * Setter za varijablu statusCode
	 * 
	 * @param statusCode
	 * @throws RuntimeException ako je header
	 * 			već generiran
	 */
	public void setStatusCode(int statusCode) {
		if(headerGenerated) {
			throw new RuntimeException("Header already generated.");
		}
		this.statusCode = statusCode;
	}
	
	/**
	 * Setter za varijablu statusText
	 * 
	 * @param statusText
	 * @throws RuntimeException ako je header
	 * 			već generiran
	 */
	public void setStatusText(String statusText) {
		if(headerGenerated) {
			throw new RuntimeException("Header already generated.");
		}
		this.statusText = statusText;
	}
	
	/**
	 * Setter za varijablu mimeType
	 * 
	 * @param mimeType
	 * @throws RuntimeException ako je header
	 * 			već generiran
	 */
	public void setMimeType(String mimeType) {
		if(headerGenerated) {
			throw new RuntimeException("Header already generated.");
		}
		this.mimeType = mimeType;
	}
	
	/**
	 * Setter za varijablu contentLength
	 * 
	 * @param contentLength
	 * @throws RuntimeException ako je header
	 * 			već generiran
	 */
	public void setContentLength(Long contentLength) {
		if(headerGenerated) {
			throw new RuntimeException("Header already generated.");
		}
		this.contentLength = contentLength;
	}
	
	/**
	 * Getter za varijablu parameters
	 * 
	 * @return parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Getter za varijablu temporaryParameters
	 * 
	 * @return temporaryParameters
	 */
	public Map<String, String> getTemporaryParameters() {
		return temporaryParameters;
	}
	
	/**
	 * Setter za varijablu temporaryParameters
	 * 
	 * @param temporaryParameters
	 */
	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Getter za varijablu persistentParameters
	 * 
	 * @return persistentParameters
	 */
	public Map<String, String> getPersistentParameterss() {
		return persistentParameters;
	}
	
	/**
	 * Setter za varijablu persistentParameters
	 * 
	 * @param persistentParameters
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}
	
	/**
	 * Konstruktor 
	 * @param outputStream
	 * @param parameters
	 * @param persistentParameters
	 * @param outputCookies
	 */
	public RequestContext(
			OutputStream outputStream,
			Map<String,String> parameters,
			Map<String,String> persistentParameters,
			List<RCCookie> outputCookies) {
		if(outputStream == null) {
			throw new NullPointerException("OutputStream cannot be null.");
		}
		
		this.outputStream = outputStream;
		this.parameters = parameters == null ? new HashMap<>() : parameters;
		this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
		this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
		
		// default vrijednosti:
		encoding = "UTF-8";
		statusCode = 200;
		statusText = "OK";
		mimeType = "text/html";
		contentLength = null;
		headerGenerated = false;
	}
	
	/**
	 * Metoda dohvaća vrijednost parametra
	 * sa zadanim imenom (ili null ako ne postoji).
	 * 
	 * @param name
	 * @return vrijednost parametra
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Metoda dohvaća imena svih postavljenih
	 * parametara.
	 * 
	 * @return Set s imenima parametara
	 */
	public Set<String> getParameterNames() {
		return parameters.keySet();
	}
	
	/**
	 * Metoda dohvaća vrijednost trajnog parametra
	 * sa zadanim imenom (ili null ako ne postoji).
	 * 
	 * @param name
	 * @return vrijednost parametra
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Metoda dohvaća imena svih postavljenih
	 * trajnih parametara.
	 * 
	 * @return Set s imenima parametara
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Metoda postavlja vrijednost trajnog
	 * parametra sa zadanim imenom na zadanu vrijednost.
	 * 
	 * @param name
	 * @param value
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Metoda uklanja trajni parametar sa zadanim
	 * imenom ako takav postoji.
	 * 
	 * @param name
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Metoda dohvaća vrijednost privremenog parametra
	 * sa zadanim imenom. Ako takav ne postoji vraća null.
	 * 
	 * @param name
	 * @return vrijednost parametra
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Metoda dohvaća imena svih postavljenih
	 * privremenih parametara.
	 * 
	 * @return Set s imenima parametara
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Metoda vraća jedinstvenu oznaku
	 * sesije za trenutnog korisnika.
	 * 
	 * @return oznaka sesije
	 */
	public String getSessionID() {
		return "";
	}
	
	/**
	 * Metoda postavlja vrijednost privremenog
	 * parametra sa zadanim imenom na zadanu vrijednost.
	 * 
	 * @param name
	 * @param value
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Metoda uklanja privremeni parametar sa zadanim
	 * imenom ako takav postoji.
	 * 
	 * @param name
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Metoda dodaje novi kolačić u outputCookies.
	 * 
	 * @param rcCookie
	 */
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
	}

	/**
	 * Metoda upisuje primljene podatke u 
	 * outputStream.
	 * 
	 * @param data
	 * @return modificirani requestContext
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}
	
	/**
	 * Metoda upisuje primljene podatke u 
	 * outputStream.
	 * 
	 * @param data
	 * @return modificirani requestContext
	 * @throws IOException
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if(!headerGenerated) {
            writeHeader();
        }
		outputStream.write(data, offset, len);
		return this;
	}
	
	/**
	 * Metoda prebacuje primljene podatke u 
	 * byte array i zatim ih upisuje u  
	 * outputStream.
	 * 
	 * @param data
	 * @return modificirani requestContext
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			writeHeader();
		}
		return write(text.getBytes(charset));
	}

	/**
	 * Pomoćna metoda koja generira header s 
	 * valjanim informacijama i ubacuje ga u outputStream.
	 * 
	 * @throws IOException ako encoding nije valjan
	 */
	private void writeHeader() throws IOException {
		try {
			charset = Charset.forName(encoding);
        } catch (UnsupportedCharsetException ex) {
            throw new IOException(ex.getMessage());
        }
		
		StringBuilder sb = new StringBuilder();
		
		// prva linija: HTTP/1.1 statusCode statusMessage
		sb.append("HTTP/1.1 ");
		sb.append(statusCode);
		sb.append(" ");
		sb.append(statusText);
		sb.append("\r\n");
		
		// druga linija: Content-Type: mimeType
		sb.append("Content-Type: ");
		sb.append(mimeType);
		if(mimeType.startsWith("text/")) {
			sb.append("; charset=");
			sb.append(encoding);
		}
		sb.append("\r\n");
		
		// treća linija: Content-Length: contentLength
		if(contentLength != null) {
			sb.append("Content-Length ");
			sb.append(contentLength);
			sb.append("\r\n");
		}
		
		// info o svakom kolačiću
		for(RCCookie cookie : outputCookies) {
			sb.append("Set-Cookie: ");
			sb.append(cookie.getName());
			sb.append("=\"");
			sb.append(cookie.getValue());
			sb.append("\"");
			if(cookie.getDomain() != null) {
				sb.append(";  Domain=");
				sb.append(cookie.getDomain());
			}
			
			if(cookie.getPath() != null) {
				sb.append(";  Path=");
				sb.append(cookie.getPath());
			}
			
			if(cookie.getMaxAge() != null) {
				sb.append(";  Max-Age=");
				sb.append(cookie.getMaxAge());
			}
			sb.append("\r\n");
		}
		
		// prazna linija za kraj headera
		sb.append("\r\n");
		
		headerGenerated = true;
		outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
	}
}
