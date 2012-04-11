package ja3d.core;

import utils.Vector3D;

public class Transform3D {

	public double a = 1;
	public double b = 0;
	public double c = 0;
	public double d = 0;
	
	public double e = 0;
	public double f = 1;
	public double g = 0;
	public double h = 0;
	
	public double i = 0;
	public double j = 0;
	public double k = 1;
	public double l = 0;
	
	public void identity() {
		a = 1;
		b = 0;
		c = 0;
		d = 0;
		
		e = 0;
		f = 1;
		g = 0;
		h = 0;
		
		i = 0;
		j = 0;
		k = 1;
		l = 0;
	}
	
	// rotation order XYZ
	// it is the multiply of three rotation/scale matrix plus one translation matrix.
	
//	[rotationZ/scaleZ]           [rotationY/scaleY]           [rotationX/scaleX]
//	cosRZ -sinRZ      0          cosRY      0  -sinRY         scaleX     0      0
//	sinRZ  cosRZ      0              0 scaleY      0               0 cosRX -sinRX
//	   0       0 scaleZ          sinRY      0   cosRY              0 sinRX  cosRX
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
		
		a = cosZ*cosYscaleX;
		b = cosZsinY*sinXscaleY - sinZ*cosXscaleY;
		c = cosZsinY*cosXscaleZ + sinZ*sinXscaleZ;
		d = x;
		
		e = sinZ*cosYscaleX;
		f = sinZsinY*sinXscaleY + cosZ*cosXscaleY;
		g = sinZsinY*cosXscaleZ - cosZ*sinXscaleZ;
		h = y;
		
		i = -sinY*scaleX;
		j = cosY*sinXscaleY;
		k = cosY*cosXscaleZ;
		l = z;
	}

	// this * invert is entity.
	public void invert() {
		double ta = a, tb = b, tc = c, td = d;
		double te = e, tf = f, tg = g, th = h;
		double ti = i, tj = j, tk = k, tl = l;
		double det = 1/(-tc*tf*ti + tb*tg*ti + tc*te*tj - ta*tg*tj - tb*te*tk + ta*tf*tk);
		a = (-tg*tj + tf*tk)*det;
		b = (tc*tj - tb*tk)*det;
		c = (-tc*tf + tb*tg)*det;
		d = (td*tg*tj - tc*th*tj - td*tf*tk + tb*th*tk + tc*tf*tl - tb*tg*tl)*det;
		e = (tg*ti - te*tk)*det;
		f = (-tc*ti + ta*tk)*det;
		g = (tc*te - ta*tg)*det;
		h = (tc*th*ti - td*tg*ti + td*te*tk - ta*th*tk - tc*te*tl + ta*tg*tl)*det;
		i = (-tf*ti + te*tj)*det;
		j = (tb*ti - ta*tj)*det;
		k = (-tb*te + ta*tf)*det;
		l = (td*tf*ti - tb*th*ti - td*te*tj + ta*th*tj + tb*te*tl - ta*tf*tl)*det;
	}
	
	public void combine(Transform3D transformA, Transform3D transformB) {
		a = transformA.a*transformB.a + transformA.b*transformB.e + transformA.c*transformB.i;
		b = transformA.a*transformB.b + transformA.b*transformB.f + transformA.c*transformB.j;
		c = transformA.a*transformB.c + transformA.b*transformB.g + transformA.c*transformB.k;
		d = transformA.a*transformB.d + transformA.b*transformB.h + transformA.c*transformB.l + transformA.d;
		e = transformA.e*transformB.a + transformA.f*transformB.e + transformA.g*transformB.i;
		f = transformA.e*transformB.b + transformA.f*transformB.f + transformA.g*transformB.j;
		g = transformA.e*transformB.c + transformA.f*transformB.g + transformA.g*transformB.k;
		h = transformA.e*transformB.d + transformA.f*transformB.h + transformA.g*transformB.l + transformA.h;
		i = transformA.i*transformB.a + transformA.j*transformB.e + transformA.k*transformB.i;
		j = transformA.i*transformB.b + transformA.j*transformB.f + transformA.k*transformB.j;
		k = transformA.i*transformB.c + transformA.j*transformB.g + transformA.k*transformB.k;
		l = transformA.i*transformB.d + transformA.j*transformB.h + transformA.k*transformB.l + transformA.l;
	}
	
	public void copy(Transform3D source) {
		a = source.a;
		b = source.b;
		c = source.c;
		d = source.d;
		e = source.e;
		f = source.f;
		g = source.g;
		h = source.h;
		i = source.i;
		j = source.j;
		k = source.k;
		l = source.l;
	}
	
	public Vector3D transform(Vector3D v, Vector3D r) {
		r.x = a * v.x + b * v.y + c * v.z + d;
		r.y = e * v.x + f * v.y + g * v.z + h;
		r.z = i * v.x + j * v.y + k * v.z + l;
		return r;
	}
}
