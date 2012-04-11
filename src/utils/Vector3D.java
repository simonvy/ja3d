package utils;

public class Vector3D {

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D() {
		// TODO Auto-generated constructor stub
	}

	public double x;
	public double y;
	public double z;
	
	public double w;

	public double getLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void clear() {
		x = y = z = w = 0;
	}
	
	public double getLengthSquare() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void normalize() {
		// TODO Auto-generated method stub
		
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
