package utils;

public class Matrix3D {

	//  0  1  2  3
	//  4  5  6  7
	//  8  9 10 11
	// 12 13 14 15
	private double[] m;
	
	public Matrix3D(double[] components) {
		this.m = components.clone();
	}

	public Matrix3D() {
		m = new double[16];
		m[0] = m[5] = m[10] = m[15] = 1;
	}

	public void appendScale(double x, double y, double z) {
		append(scaleMatrix(x, y, z));
	}

	public void appendRotation(double rad, Vector3D axis) {
		append(rotationMatrix(rad, axis));
	}

	public void appendTranslation(double x, double y, double z) {
		append(translationMatrix(x, y, z));	
	}

	public void append(Matrix3D b) {
		double ta = m[0], tb = m[1], tc = m[2], td = m[3];
		double te = m[4], tf = m[5], tg = m[6], th = m[7];
		double ti = m[8], tj = m[9], tk = m[10], tl = m[11];  
		m[0] = ta*b.m[0] + tb*b.m[4] + tc*b.m[8];
		m[1] = ta*b.m[1] + tb*b.m[5] + tc*b.m[9];
		m[2] = ta*b.m[2] + tb*b.m[6] + tc*b.m[10];
		m[3] = ta*b.m[3] + tb*b.m[7] + tc*b.m[11] + td;
		m[4] = te*b.m[0] + tf*b.m[4] + tg*b.m[8];
		m[5] = te*b.m[1] + tf*b.m[5] + tg*b.m[9];
		m[6] = te*b.m[2] + tf*b.m[6] + tg*b.m[10];
		m[7] = te*b.m[3] + tf*b.m[7] + tg*b.m[11] + th;
		m[8] = ti*b.m[0] + tj*b.m[4] + tk*b.m[8];
		m[9] = ti*b.m[1] + tj*b.m[5] + tk*b.m[9];
		m[10] = ti*b.m[2] + tj*b.m[6] + tk*b.m[10];
		m[11] = ti*b.m[3] + tj*b.m[7] + tk*b.m[11] + tl;
	}

	// decompose this matrix into [translation, rotation, scale] array.
	// TODO
	public Vector3D[] decompose() {
		return new Vector3D[] {
				new Vector3D(),
				new Vector3D(),
				new Vector3D()
		};
	}
	
	private static Matrix3D rotationMatrix(double rad, Vector3D axis) {
		Matrix3D m = new Matrix3D();
		
		double x = axis.x;
		double y = axis.y;
		double z = axis.z;
		
		double ncos = Math.cos(rad);
		double nsin = Math.sin(rad);
		double scos = 1 - ncos;
		
		double sxy = x * y * scos;
		double sxz = x * z * scos;
		double syz = y * z * scos;
		double sx = nsin * x;
		double sy = nsin * y;
		double sz = nsin * z;
		
		m.m[0] = ncos + x * x * scos;
		m.m[1] = -sz + sxy;
		m.m[2] = sy + sxz;
		m.m[3] = 0;
		
		m.m[4] = sz + sxy;
		m.m[5] = ncos + y * y * scos;
		m.m[6] = -sx + syz;
		m.m[7] = 0;
		
		m.m[8] = -sy + sxz;
		m.m[9] = sx + syz;
		m.m[10] = ncos + z * z * scos;
		m.m[11] = 0;
		
		return m;
	}
	
	private static Matrix3D scaleMatrix(double x, double y, double z) {
		Matrix3D m = new Matrix3D();
		m.m[0] = x;
		m.m[5] = y;
		m.m[10] = z;
		return m;
	}
	
	private static Matrix3D translationMatrix(double x, double y, double z) {
		Matrix3D m = new Matrix3D();
		m.m[3] = x;
		m.m[7] = y;
		m.m[11] = z;
		return m;
	}
}
