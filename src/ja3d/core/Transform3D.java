package ja3d.core;

import utils.Vector3D;

public class Transform3D {

	//  0  1  2  3
	//  4  5  6  7
	//  8  9 10 11
	// 12 13 14 15
	private double[] m = new double[16];
	
	public Transform3D() {
		m[0] = m[5] = m[10] = m[15] = 1;
	}
	
	public void identity() {
		for (int i = 0; i < m.length; i++) {
			m[i] = 0;
		}
		m[0] = m[5] = m[10] = m[15] = 1;
	}
	
	// the resulting transform is TranslationXYZ * RotationZ * RotationY * RotationX * scaleXYZ
	// so the rotation order is XYZ.
	
//	[rotationZ]              [rotationY]           [rotationX]               [scale]
//	cosRZ -sinRZ 0           cosRY 0   sinRY       1     0      0            scaleX      0      0
//	sinRZ  cosRZ 0               0 1       0       0 cosRX -sinRX                 0 scaleY      0
//	   0       0 1          -sinRY 0   cosRY       0 sinRX  cosRX                 0      0 scaleZ
	public void compose(double x, double y, double z, 
			double rotationX, double rotationY, double rotationZ,
			double scaleX, double scaleY, double scaleZ) {
		
		double cosX = Math.cos(rotationX);
		double sinX = Math.sin(rotationX);
		double cosY = Math.cos(rotationY);
		double sinY = Math.sin(rotationY);
		double cosZ = Math.cos(rotationZ);
		double sinZ = Math.sin(rotationZ);
		
		double cosZsinY = cosZ*sinY;
		double sinZsinY = sinZ*sinY;
		double cosYscaleX = cosY*scaleX;
		double sinXscaleY = sinX*scaleY;
		double cosXscaleY = cosX*scaleY;
		double cosXscaleZ = cosX*scaleZ;
		double sinXscaleZ = sinX*scaleZ;
		
		m[0] = cosZ*cosYscaleX;
		m[1] = cosZsinY*sinXscaleY - sinZ*cosXscaleY;
		m[2] = cosZsinY*cosXscaleZ + sinZ*sinXscaleZ;
		m[3] = x;
		
		m[4] = sinZ*cosYscaleX;
		m[5] = sinZsinY*sinXscaleY + cosZ*cosXscaleY;
		m[6] = sinZsinY*cosXscaleZ - cosZ*sinXscaleZ;
		m[7] = y;
		
		m[8] = -sinY*scaleX;
		m[9] = cosY*sinXscaleY;
		m[10] = cosY*cosXscaleZ;
		m[11] = z;
	}

	// this * invert is entity.
	public void invert() {
		double ta = m[0], tb = m[1], tc = m[2], td = m[3];
		double te = m[4], tf = m[5], tg = m[6], th = m[7];
		double ti = m[8], tj = m[9], tk = m[10], tl = m[11];
		double det = 1/(-tc*tf*ti + tb*tg*ti + tc*te*tj - ta*tg*tj - tb*te*tk + ta*tf*tk);
		m[0] = (-tg*tj + tf*tk)*det;
		m[1] = (tc*tj - tb*tk)*det;
		m[2] = (-tc*tf + tb*tg)*det;
		m[3] = (td*tg*tj - tc*th*tj - td*tf*tk + tb*th*tk + tc*tf*tl - tb*tg*tl)*det;
		m[4] = (tg*ti - te*tk)*det;
		m[5] = (-tc*ti + ta*tk)*det;
		m[6] = (tc*te - ta*tg)*det;
		m[7] = (tc*th*ti - td*tg*ti + td*te*tk - ta*th*tk - tc*te*tl + ta*tg*tl)*det;
		m[8] = (-tf*ti + te*tj)*det;
		m[9] = (tb*ti - ta*tj)*det;
		m[10] = (-tb*te + ta*tf)*det;
		m[11] = (td*tf*ti - tb*th*ti - td*te*tj + ta*th*tj + tb*te*tl - ta*tf*tl)*det;
	}
	
	// c = a * b
	public static Transform3D multiply(Transform3D a, Transform3D b, Transform3D c) {
		assert(c != a && c != b);
		c.m[0] = a.m[0]*b.m[0] + a.m[1]*b.m[4] + a.m[2]*b.m[8];
		c.m[1] = a.m[0]*b.m[1] + a.m[1]*b.m[5] + a.m[2]*b.m[9];
		c.m[2] = a.m[0]*b.m[2] + a.m[1]*b.m[6] + a.m[2]*b.m[10];
		c.m[3] = a.m[0]*b.m[3] + a.m[1]*b.m[7] + a.m[2]*b.m[11] + a.m[3];
		c.m[4] = a.m[4]*b.m[0] + a.m[5]*b.m[4] + a.m[6]*b.m[8];
		c.m[5] = a.m[4]*b.m[1] + a.m[5]*b.m[5] + a.m[6]*b.m[9];
		c.m[6] = a.m[4]*b.m[2] + a.m[5]*b.m[6] + a.m[6]*b.m[10];
		c.m[7] = a.m[4]*b.m[3] + a.m[5]*b.m[7] + a.m[6]*b.m[11] + a.m[7];
		c.m[8] = a.m[8]*b.m[0] + a.m[9]*b.m[4] + a.m[10]*b.m[8];
		c.m[9] = a.m[8]*b.m[1] + a.m[9]*b.m[5] + a.m[10]*b.m[9];
		c.m[10] = a.m[8]*b.m[2] + a.m[9]*b.m[6] + a.m[10]*b.m[10];
		c.m[11] = a.m[8]*b.m[3] + a.m[9]*b.m[7] + a.m[10]*b.m[11] + a.m[11];
		return c;
	}
	
	public void copy(Transform3D source) {
		m = source.m.clone();
	}
	
	public Vector3D transform(Vector3D v, Vector3D r) {
		assert(r != v);
		r.x = m[0] * v.x + m[1] * v.y + m[2] * v.z + m[3];
		r.y = m[4] * v.x + m[5] * v.y + m[6] * v.z + m[7];
		r.z = m[8] * v.x + m[9] * v.y + m[10] * v.z + m[11];
		return r;
	}
	
	public Vector3D transformWithoutTranslate(Vector3D v, Vector3D r) {
		assert(r != v);
		r.x = m[0] * v.x + m[1] * v.y + m[2] * v.z;
		r.y = m[4] * v.x + m[5] * v.y + m[6] * v.z;
		r.z = m[8] * v.x + m[9] * v.y + m[10] * v.z;
		return r;
	}
	
	public double d() {
		return m[3];
	}
	
	public double h() {
		return m[7];
	}
	
	public double l() {
		return m[11];
	}
}
