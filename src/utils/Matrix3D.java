package utils;

public class Matrix3D {

	public Matrix3D(double[] components) {
		// 0 4  8 12 
		// 1 5  9 13
		// 2 6 10 14
		// 3 7 11 15
		// TODO Auto-generated constructor stub
	}

	public Matrix3D() {
		// TODO Auto-generated constructor stub
	}

	public void appendScale(double sx, double sy, double sz) {
		// TODO Auto-generated method stub
		
	}

	public void appendRotation(double angle, Vector3D axis) {
		// TODO Auto-generated method stub
		
	}

	public void appendTranslation(double f, double g, double h) {
		// TODO Auto-generated method stub
		
	}

	public void append(Matrix3D matrix3d) {
		// TODO Auto-generated method stub
		
	}

	public Vector3D[] decompose() {
		return new Vector3D[] {
				new Vector3D(),
				new Vector3D(),
				new Vector3D()
		};
	}

}
