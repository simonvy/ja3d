package utils;

public class Vector3D {

	public double x;
	public double y;
	public double z;
	public double w;
	
	public static final Vector3D X_AXIS = new Vector3D(1, 0, 0);
	public static final Vector3D Y_AXIS = new Vector3D(0, 1, 0);
	public static final Vector3D Z_AXIS = new Vector3D(0, 0, 1);
	
	public Vector3D(double x, double y, double z) {
		setTo(x, y, z);
	}

	public Vector3D() {
		setTo(0, 0, 0);
	}
	
	public double getLength() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public double getLengthSquared() {
		return x * x + y * y + z * z;
	}
	
	public double normalize() {
		double length = Math.sqrt(x * x + y * y + z * z);
		if (length > 0) {
			x /= length;
			y /= length;
			z /= length;
		}
		return length;
	}
	
	public void setTo(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static double dotProduct(Vector3D a, Vector3D b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public static Vector3D crossProduct(Vector3D a, Vector3D b, Vector3D c) {
		assert(c != a && c != b);
		c.x = a.y * b.z - a.z * b.y;
		c.y = a.z * b.x - a.x * b.z;
		c.z = a.x * b.y - a.y * b.x;
		return c;
	}
	
	// c = a + b * factor
	public static Vector3D ma(Vector3D a, Vector3D b, double factor, Vector3D c) {
		c.x = a.x + b.x * factor;
		c.y = a.y + b.y * factor;
		c.z = a.z + b.z * factor;
		return c;
	}
	
	// c = a - b
	public static Vector3D substract(Vector3D a, Vector3D b, Vector3D c) {
		c.x = a.x - b.x;
		c.y = a.y - b.y;
		c.z = a.z - b.z;
		return c;
	}
	
	// c = a + b
	public static Vector3D add(Vector3D a, Vector3D b, Vector3D c) {
		c.x = a.x + b.x;
		c.y = a.y + b.y;
		c.z = a.z + b.z;
		return c;
	}
	
	public static Vector3D scale(Vector3D a, double scale, Vector3D b) {
		b.x = a.x * scale;
		b.y = a.y * scale;
		b.z = a.z * scale;
		return b;
	}
}
