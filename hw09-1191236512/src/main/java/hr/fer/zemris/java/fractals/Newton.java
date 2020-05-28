package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Razred omogućava crtanje Newtonovog fraktala.
 * Program od korisnika očekuje unos nultočaka polinoma i zatim
 * crta fraktal pomoću Newton-Raphson-ovih iteracija.
 * Za kraj unosa nultočaka potrebno je unesti "done".
 * 
 * @author Valentina Križ
 *
 */
public class Newton {
	private static final double convergenceTreshold = 0.001;
	private static final int maxIter = 16 * 16 * 16;
	private static final double rootTreshold = 0.002;
	private static ComplexRootedPolynomial polynomial;
	private static ComplexPolynomial derived;
	
	/**
	 * Pomoćna metoda za parsiranje imaginarnog dijela broja.
	 * 
	 * @param s string u kojemu je zapisan imaginarni dio broja
	 * @return vrijednost imaginarnog dijela
	 */
	private static double parseImaginary(String s) {
		// u slučaju da je imaginarni dio samo "-i" ili "i" vrijednost je -1, odnosno 1
		double imaginary = (s.startsWith("-")) ? -1 : 1;
		
		if(!s.equals("i") && !s.equals("-i")) {
			imaginary *= Double.parseDouble(s.substring(2, s.length()));
		}
		return imaginary;
	}
	
	/**
	 * Pomoćna metoda za parsiranje imaginarnih brojeva.
	 * 
	 * @param s string s kompleksnim brojem
	 * @return generirani kompleksni broj
	 * @throws NumberFormatException ako je predan string
	 * 			koji ne sadrži ispravan kompleksni broj
	 */
	public static Complex parse(String s) {
		// izbacimo sve razmake iz stringa
		// originalni string s ostavljamo za ispis ako string nije dobrog formata
		String newS = s.replaceAll("\\s+","");
			
		// ako string zapocinje s +- ili -+ odmah bacamo iznimku
		if(newS.startsWith("+-") || newS.startsWith("-+")) {
			throw new NumberFormatException(s + " nije kompleksan broj.");
		}
		
		// makni nepotrebni + ako string počinje sa +
		if(newS.startsWith("+")) {
			newS = newS.substring(1);
		}
		
		double imaginary = 0.0;
		double real = 0.0;
		int dividerIndex = Math.max(newS.lastIndexOf("+"), newS.lastIndexOf("-"));
		
		// ako se broj sastoji od samo realnog ili samo imaginarnog dijela
		if(dividerIndex == -1 || dividerIndex == 0) {
			if(newS.contains("i")) {
				// samo imaginarni dio (ili neispravan format)
				try {
					imaginary = parseImaginary(newS);
				} catch (NumberFormatException ex) {
					throw new NumberFormatException(s + " nije kompleksan broj.");
				}
			} else {
				// samo realni dio (ili neispravan format)
				try {
					real = Double.parseDouble(newS);
				} catch (NumberFormatException ex) {
					throw new NumberFormatException(s + " nije kompleksan broj.");
				}
				
			}
		} else {
			// ako broj ima i realni i imaginarni dio (ili neispravan format)
			try {
				real = Double.parseDouble(newS.substring(0, dividerIndex));
				imaginary = parseImaginary(newS.substring(dividerIndex));
			} catch (NumberFormatException ex) {
				throw new NumberFormatException(s + " nije kompleksan broj.");
			}
		}
		
		return new Complex(real, imaginary);
	}
	
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		int rootCounter = 1;
		List<Complex> rootsList = new LinkedList<>();
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.printf("Root %d> ", rootCounter);
			++rootCounter;
			String s = sc.nextLine();
			if(s.equals("done")) {
				break;
			}
			try {
				rootsList.add(parse(s));
			} catch(NumberFormatException ex) {
				System.out.println(ex.getMessage());
			}
			
		}
		
		System.out.println("Image of fractal will appear shortly. Thank you.");
		
		sc.close();
		
		Complex[] roots = new Complex[rootsList.size()];
		int cnt = 0;
		
		for(Complex root : rootsList) {
			roots[cnt] = root;
			++cnt;
		}
		
		polynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
		derived = polynomial.toComplexPolynom().derive();
		
		FractalViewer.show(new FractalProducer());
	}
	
	/**
	 * Razred predstavlja posao koji obavlja jedna
	 * dretva za prikaz dijela fraktala.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class FractalJob implements Callable<Void> {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		short[] data;
		
		/**
		 * Konstruktor koji postavlja parametre na zadane vrijednosti. 
		 * 
		 * @param reMin
		 * @param reMax
		 * @param imMin
		 * @param imMax
		 * @param width
		 * @param height
		 * @param yMin
		 * @param yMax
		 * @param data
		 */
		public FractalJob(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, short[] data) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
		}

		/**
		 * Metoda za pokretanje izvođenja posla.
		 * Svaka dretva pomoću Raphson-ovih iteracija određuje
		 * boju u koju treba obojati svaki piksel koji se nalazi
		 * u njenoj pruzi.
		 */
		@Override
		public Void call() throws Exception {
			// offset za upisivanje rezultata na dobro mjesto
			int offset = yMin * width;
			
			// za svaku točku u pruzi
			for(int y = yMin; y <= yMax; ++y) {
				for(int x = 0; x < width; ++x) {
					// preslikaj točku u kompleksnu ravninu
					Complex zn =  mapToComplexPlain(x, y, 0, width, yMin, yMax, reMin, reMax, imMin, imMax);
					double module = 0;
					int iter = 0;
					
					// iteriraj sve dok nije zadovoljen jedan od uvjeta
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = znold.sub(zn).module();
						++iter;
					} while(module > convergenceTreshold && iter <  maxIter);
					
					// pronađi najbližu nultočku
					int index = polynomial.indexOfClosestRootFor(zn, rootTreshold);
					data[offset++] = (short) (index + 1);
				}
			}
			return null;
		}

		/**
		 * Metoda za preslikavanje točke rasteru u kompleksnu ravninu.
		 * 
		 * @param x x-koordinata točke
		 * @param y y-koordinata točke
		 * @param xMin minimalna vrijednost x osi
		 * @param xMax maksimalna vrijednost x osi
		 * @param yMin minimalna vrijednost y osi
		 * @param yMax maksimalna vrijenost y osi
		 * @param reMin minimalna vrijednost na realnoj osi
		 * @param reMax maksimalna vrijednost na realnoj osi
		 * @param imMin minimalna vrijednost na imaginarnoj osi
		 * @param imMax maksimalna vrijednost na imaginarnoj osi
		 * @return kompleksni broj koji odgovara zadanoj točki
		 */
		private Complex mapToComplexPlain(int x, int y, int xMin, int xMax, int yMin, int yMax,
				double reMin, double reMax, double imMin, double imMax) {
			
			// minimalna vrijednost na realnoj osi + pomak
			double re = reMin + (double)x / (xMax - 1) * (reMax - reMin);
			// minimalna vrijednost na imaginarnoj osi + pomak
			double im = imMin + (double)(height - 1 - y) / (height - 1) * (imMax - imMin);
			
			return new Complex(re, im);
		}
		
	}
	
	/**
	 * Razred za generiranje fraktala koji koristi
	 * paralelizaciju za ubrzanje rada.
	 * Svaka dretva generira jednu horizontalnu prugu 
	 * na cjelokupnoj slici.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class FractalProducer implements IFractalProducer {
		private static ExecutorService pool;
		
		/**
		 * Konstruktor, stvara novi pool dretvi.
		 */
		public FractalProducer() {
			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new DaemonicThreadFactory());
		}
		
		/**
		 * Metoda za generiranje fraktala.
		 * 
		 * @param reMin minimalna vrijednost na realnoj osi
		 * @param reMax maksimalna vrijednost na realnoj osi
		 * @param imMin minimalna vrijednost na imaginarnoj osi
		 * @param imMax maksimalna vrijednost na imaginarnoj osi
		 * @param width širina prozora
		 * @param height visina prozora
		 * @param requestNo
		 * @param observer
		 * @param cancel
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			
			int m = polynomial.toComplexPolynom().order() + 1;
			short[] data = new short[width * height];
			
			// podjela na 8 * broj procesora pruga
			final int numOfStripes = 8 * Runtime.getRuntime().availableProcessors();
			int ysPerStripe = height / numOfStripes;
			
			List<Future<Void>> results = new ArrayList<>();
			
			for(int i = 0; i < numOfStripes; i++) {
				// odredi minimalni i maksimalni y za trenutnu prugu
				int yMin = i * ysPerStripe;
				int yMax = (i + 1) * ysPerStripe - 1;
				
				// ako je zadnja pruga pokupi sve do kraja
				if(i == numOfStripes - 1) {
					yMax = height - 1;
				}
				
				// pokreni novu dretvu na zadanoj pruzi
				FractalJob job = new FractalJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax, data);
				results.add(pool.submit(job));
			}
			for(Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}
						
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)m, requestNo);
		}

	}
	
	/**
	 * Razred koji predstavlja tvornicu dretvi.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class DaemonicThreadFactory implements ThreadFactory {

		/**
		 * Metoda za stvaranje novih dretvi sa zastavicom daemon
		 * postavljenom na true.
		 */
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = Executors.defaultThreadFactory().newThread(r);
			thread.setDaemon(true);
			
			return thread;
		}
		
	}
}
