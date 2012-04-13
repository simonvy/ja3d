package utils;

import ja3d.core.Transform3D;

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

	// axis should be normalized vector.
	public void appendRotation(double angle, Vector3D axis) {
		append(rotationMatrix(angle, axis));
	}

	public void appendTranslation(double x, double y, double z) {
		append(translationMatrix(x, y, z));	
	}

	// this = b * this
	public void append(Matrix3D b) {
		double ta = m[0], tb = m[1], tc = m[2], td = m[3];
		double te = m[4], tf = m[5], tg = m[6], th = m[7];
		double ti = m[8], tj = m[9], tk = m[10], tl = m[11];
		
		m[0] = b.m[0] * ta + b.m[1] * te + b.m[2] * ti;
		m[1] = b.m[0] * tb + b.m[1] * tf + b.m[2] * tj;
		m[2] = b.m[0] * tc + b.m[1] * tg + b.m[2] * tk;
		m[3] = b.m[0] * td + b.m[1] * th + b.m[2] * tl + b.m[3];
		
		m[4] = b.m[4] * ta + b.m[5] * te + b.m[6] * ti;
		m[5] = b.m[4] * tb + b.m[5] * tf + b.m[6] * tj;
		m[6] = b.m[4] * tc + b.m[5] * tg + b.m[6] * tk;
		m[7] = b.m[4] * td + b.m[5] * th + b.m[6] * tl + b.m[7];
		
		m[8]  = b.m[8] * ta + b.m[9] * te + b.m[10] * ti;
		m[9]  = b.m[8] * tb + b.m[9] * tf + b.m[10] * tj;
		m[10] = b.m[8] * tc + b.m[9] * tg + b.m[10] * tk;
		m[11] = b.m[8] * td + b.m[9] * th + b.m[10] * tl + b.m[11];
	}

	// this matrix is TranslationXYZ * RotationZ * RotationY * RotationX * scaleXYZ
	// decompose this matrix into [translation, rotation, scale] array.
	// refer to Transform3D compose method.
	public Vector3D[] decompose() {
		Vector3D t = new Vector3D(m[3], m[7], m[11]);
		// assume all scales are positive.
		double sx = Math.sqrt(m[0] * m[0] + m[4] * m[4] + m[8] * m[8]);
		double sy = Math.sqrt(m[1] * m[1] + m[5] * m[5] + m[9] * m[9]);
		double sz = Math.sqrt(m[2] * m[2] + m[6] * m[6] + m[10] * m[10]);
		assert(sx > 0 && sy > 0 && sz > 0);
		Vector3D s = new Vector3D(sx, sy, sz);
		//
		double rx = Math.atan2(m[9] / sy, m[10] / sz);
		double ry = Math.asin(-m[8] / sx);
		double rz = Math.atan2(m[4], m[0]);
		Vector3D r = new Vector3D(rx, ry, rz);
		//
		return new Vector3D[] {t, r, s};
	}
	
	private static Matrix3D rotationMatrix(double angle, Vector3D axis) {
		Matrix3D m = new Matrix3D();
		
		double rad = angle * Math.PI / 180;
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
		
		m.m[4] = sz + sxy;
		m.m[5] = ncos + y * y * scos;
		m.m[6] = -sx + syz;
		
		m.m[8] = -sy + sxz;
		m.m[9] = sx + syz;
		m.m[10] = ncos + z * z * scos;
		
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
	
	public Vector3D transform(Vector3D v, Vector3D r) {
		assert(r != v);
		r.x = m[0] * v.x + m[1] * v.y + m[2] * v.z + m[3];
		r.y = m[4] * v.x + m[5] * v.y + m[6] * v.z + m[7];
		r.z = m[8] * v.x + m[9] * v.y + m[10] * v.z + m[11];
		return r;
	}
	
	public static void main(String[] args) {
		Vector3D axis = new Vector3D(10, -2, 1.5);
		axis.normalize();

		// ONLY accept transformation sequence Translation * Rotation * Scale.
		Matrix3D m = new Matrix3D();
		
//		------------------------------
//		9.411764705882353 -6.675133382788935 -1.5855608832249344 -8.69518470937032 
//		-0.42713919095847264 0.7529411764705894 -29.951333827889368 -88.7752583216854 
//		3.352049706173016 18.838144120553697 0.6352941176470605 42.93422030022159 
//		------------------------------
//		-8.69518470937032 -88.7752583216854 42.93422030022159
//		1.5483175692829043 -0.3418227790326487 -0.04535241918323327
//		10.0 20.0 29.999999999999996
//		------------------------------
//		9.411764705882351 -6.6751333827889345 -1.5855608832249315 -8.69518470937032 
//		-0.4271391909584726 0.7529411764705873 -29.951333827889364 -88.7752583216854 
//		3.352049706173016 18.838144120553697 0.6352941176470576 42.93422030022159 
//		------------------------------
		
//		OK
		m.appendScale(10, 20, 30);
		m.appendRotation(90, axis);
		m.appendTranslation(1, 2, 3);

//		OK
//		m.appendScale(10, 20, 30);
//		m.appendTranslation(1, 2, 3);
//		m.appendRotation(90, axis);
		
//		OK
//		m.appendTranslation(1, 2, 3);
//		m.appendScale(10, 20, 30);
//		m.appendRotation(90, axis);

		
//		------------------------------
//		9.411764705882353 -3.3375666913944677 -0.5285202944083114 1.1510704398684826 
//		-0.8542783819169453 0.7529411764705894 -19.96755588525958 -59.2510636847545 
//		10.056149118519048 28.257216180830547 0.6352941176470605 68.47646383312131 
//		------------------------------
//		1.1510704398684826 -59.2510636847545 68.47646383312131
//		1.5387859797841592 -0.816438892034485 -0.09051903660734228
//		13.799900054949484 28.463600231258912 19.98464961785627
//		------------------------------
//		9.411764705882353 -20.563911515816674 -2.2697768414791195 1.1510704398684826 
//		-0.8542783819169452 2.7812451974046093 -19.85050316245319 -59.2510636847545 
//		10.056149118519048 19.482473057763958 0.43801556570923894 68.47646383312131 
//		------------------------------
		
//		FAIL
//		m.appendTranslation(1, 2, 3);
//		m.appendRotation(90, axis);
//		m.appendScale(10, 20, 30);
		
//		FAIL
//		m.appendRotation(90, axis);
//		m.appendTranslation(1, 2, 3);
//		m.appendScale(10, 20, 30);
		
//		FAIL
//		m.appendRotation(90, axis);
//		m.appendScale(10, 20, 30);
//		m.appendTranslation(1, 2, 3);
		
		
		print(m.m);
		
		Vector3D[] vs = m.decompose();
		for (Vector3D v : vs) {
			System.out.println(v.x + " " + v.y + " " + v.z);
		}
		
		Transform3D t3d = new Transform3D();
		t3d.compose(vs[0].x, vs[0].y, vs[0].z, 
				vs[1].x, vs[1].y, vs[1].z, vs[2].x, vs[2].y, vs[2].z);
		
		
//		print (t3d.m);
	}
	
	private static void print(double[] m) {
		System.out.println("------------------------------");
		System.out.print(m[0] + " ");
		System.out.print(m[1] + " ");
		System.out.print(m[2] + " ");
		System.out.print(m[3] + " ");
		System.out.println();
		System.out.print(m[4] + " ");
		System.out.print(m[5] + " ");
		System.out.print(m[6] + " ");
		System.out.print(m[7] + " ");
		System.out.println();
		System.out.print(m[8] + " ");
		System.out.print(m[9] + " ");
		System.out.print(m[10] + " ");
		System.out.print(m[11] + " ");
		System.out.println();
		System.out.println("------------------------------");
	}
	
}
