package hr.fer.zemris.java.raytracer.model;

/**
 * Razred predstavlja sferu kao grafički objekt.
 * 
 * @author Valentina Križ
 *
 */
public class Sphere extends GraphicalObject {
	private Point3D center;
	private double radius;
	private double kdr;
	private double kdg;
	private double kdb;
	private double krr;
	private double krg;
	private double krb;
	private double krn;
	
	/**
	 * Konstruktor koji postavlja parametre sfere na zadane
	 * vrijednosti.
	 * Parametri kd* su parametri za difuznu komponentu,
	 * kr* za reflekcijsku komponentu, a krn je faktor sjaja.
	 * 
	 * @param center
	 * @param radius
	 * @param kdr
	 * @param kdg
	 * @param kdb
	 * @param krr
	 * @param krg
	 * @param krb
	 * @param krn
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/**
	 * Metoda za određivanje najbližeg presjeka
	 * sfere i zrake.
	 * 
	 * @param ray zraka
	 * @return najbliži presjek
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D L = center.sub(ray.start);
		double tc = L.scalarProduct(ray.direction);
		
		// nema presjeka
		if(tc < 0) {
			return null;
		}
		
		double d = Math.sqrt(L.scalarProduct(L) - tc * tc);
		
		// nema presjeka 
		if(d > radius) {
			return null;
		}
		
		double t1c = Math.sqrt(radius * radius - d * d);
		double t1 = tc - t1c;
		
		Point3D point = ray.start.add(ray.direction.scalarMultiply(t1));
		double distance = point.sub(ray.start).norm();
		boolean outer = true;
		
		return new RayIntersection(point, distance, outer) {
			@Override
			public Point3D getNormal() {
				return getPoint().sub(center);
			}

			@Override
			public double getKdb() {
				return kdb;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrr() {
				return krr;
			}
		};
	}

}
