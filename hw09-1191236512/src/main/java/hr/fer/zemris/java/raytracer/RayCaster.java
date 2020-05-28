package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Razred omogućava renderiranje 3D objekata pomoću
 * algoritma bacanja zrake (ray-caster algoritam) uz
 * Phongov model osvjetljenja.
 * 
 * @author Valentina Križ
 *
 */
public class RayCaster {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(),
				new Point3D(10,0,0),
				new Point3D(0,0,0),
				new Point3D(0,0,10),
				20, 20);
	}
	
	/**
	 * Metoda za pokretanje renderiranja 3D objekata.
	 * 
	 * @return IRayTracerProducer objekt za renderiranje
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
						double horizontal, double vertical, int width, int height,
						long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width*height];
				short[] green = new short[width*height];
				short[] blue = new short[width*height];
				
				Point3D normViewUp = viewUp.normalize();
				
				// postavljanje koordinatnog sustava
				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = normViewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(normViewUp))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2)).add(yAxis.scalarMultiply(vertical / 2));
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;
				// za svaku točku odredi u koju boju ju treba obojati
				for(int y = 0; y < height; y++) {
					for(int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply((double)x / (width - 1) * horizontal))
												.sub(yAxis.scalarMultiply((double)y / (height - 1) * vertical));
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Metoda baca zraku i određuje boju u koju boja piksel.
	 * 
	 * @param scene
	 * @param ray
	 * @param rgb
	 */
	protected static void tracer(Scene scene, Ray ray, short[] rgb) {
		// ako neće postojati presjek oboji u crno
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		
		// odredi najbliži presjek zrake i svih objekata u sceni
		RayIntersection closest = findClosestIntersection(scene, ray);
		
		// ako presjek postoji, odredi u koju boju treba obojati piksel koji zraka pogodi
		if(closest != null) {
			determineColorFor(scene, closest, rgb, ray);
		}
	}

	/**
	 * Metoda za određivanje boje točke koju zraka
	 * pogodi nakon odbijanja o neki od objekata u sceni.
	 * 
	 * @param scene
	 * @param closest
	 * @param rgb
	 * @param ray
	 */
	private static void determineColorFor(Scene scene, RayIntersection closest, short[] rgb, Ray ray) {
		// ambijentna komponenta
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;
		
		// dohvati sve izvore svjetlosti
		List<LightSource> lights = scene.getLights();
		
		// za svaki izvor svjetlosti
		for(LightSource light : lights) {
			// odredi zraku koja ide iz izvora svjetlosti do presjeka 
			Ray r = Ray.fromPoints(light.getPoint(), closest.getPoint());
			
			// pronađi najbliži presjek te zrake i svih objekata u sceni
			RayIntersection s = findClosestIntersection(scene, r);
			
			// ako postoji presjek i bliži je, neki objekt zaklanja izvor svjetlosti, zanemari ga
			if(s != null && light.getPoint().sub(s.getPoint()).norm() + 10e-10 < light.getPoint().sub(closest.getPoint()).norm()) {
				continue;
			} else {
				// inače dodaj difuznu i zrcalnu komponentu tog izvora svjetlosti
				addComponents(rgb, s, light, ray);
			}
		}
	}

	/**
	 * Metoda dodaje difuznu i refleksivnu komponentu
	 * u boju točke.
	 * 
	 * @param rgb trenutna boja
	 * @param s točka presjeka
	 * @param light izvor svjetlosti
	 * @param ray zraka
	 */
	private static void addComponents(short[] rgb, RayIntersection s, LightSource light, Ray ray) {
		// vektor normale na površinu objekta (normiran)
		Point3D n = s.getNormal().normalize();
		// vektor zrake iz točke presjeka prema izvoru svjetlosti (normiran)
		Point3D l = light.getPoint().sub(s.getPoint()).normalize();
		// kosinus kuta između n i l, pomoću skalarnog produkta
		double nl = n.scalarProduct(l);
		
		// difuzna komponenta za svaku boju
		// intenzitet točkastog izvora * koeficijent refleksije + kosinus kuta između n i l
		// ako je nl < 0 promatrana točka nije vidljiva pa je boja crna
		rgb[0] += light.getR() * s.getKdr() * Math.max(0, nl);
		rgb[1] += light.getG() * s.getKdg() * Math.max(0, nl);
		rgb[2] += light.getB() * s.getKdb() * Math.max(0, nl);
		
		// vektor reflektirane zrake
		Point3D r = (n.scalarMultiply(2 * nl)).sub(l).normalize();
		// vektor usmjeren iz točke površine prema promatraču (oku)
		Point3D v = ray.start.sub(s.getPoint()).normalize();
		
		double rv = r.scalarProduct(v);
		
		// zrcalna komponenta za svaku boju
		if(rv >= 0) {
			double cos = Math.pow(rv, s.getKrn());
			rgb[0] += light.getR() * s.getKrr() * cos;
			rgb[1] += light.getG() * s.getKrg() * cos;
			rgb[2] += light.getB() * s.getKrb() * cos;
		}
	}

	/**
	 * Metoda za pronalazak najbližeg presjeka zrake sa svim objektima u sceni.
	 * 
	 * @param scene
	 * @param ray
	 * @return najbliži presjek
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		List<GraphicalObject> objects = scene.getObjects();
		RayIntersection closest = null;
		
		for(GraphicalObject object : objects) {
			RayIntersection intersection = object.findClosestRayIntersection(ray);
			
			// ako je prvi pronađeni presjek ili bliži od do sad najbližeg
			if(intersection != null && (closest == null || intersection.getDistance() < closest.getDistance())) {
				closest = intersection;
			}
		}
		
		return closest;
	}

}
